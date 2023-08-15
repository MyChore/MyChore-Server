package com.mychore.mychore_server.global.config;

import com.mychore.mychore_server.entity.chore.Chore;
import com.mychore.mychore_server.entity.user.User;
import com.mychore.mychore_server.entity.user.UserAgree;
import com.mychore.mychore_server.global.constants.Repetition;
import com.mychore.mychore_server.global.exception.BaseException;
import com.mychore.mychore_server.global.exception.BaseResponseCode;
import com.mychore.mychore_server.repository.ChoreRepository;
import com.mychore.mychore_server.repository.UserAgreeRepository;
import com.mychore.mychore_server.repository.UserRepository;
import com.mychore.mychore_server.service.NotificationService;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.Trigger;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static com.mychore.mychore_server.global.constants.Constant.ACTIVE_STATUS;

@Component
@RequiredArgsConstructor
@Slf4j
public class ScheduleConfig {

    private String cron = null;
    private final Map<Long, ThreadPoolTaskScheduler> choreScheduledTasks = new ConcurrentHashMap<>();
    private final Map<Long, ThreadPoolTaskScheduler> todayScheduledTasks = new ConcurrentHashMap<>();
    private final NotificationService notificationService;
    private final UserAgreeRepository userAgreeRepository;
    private final UserRepository userRepository;
    private final ChoreRepository choreRepository;

    public ThreadPoolTaskScheduler poolTaskScheduler() {
        ThreadPoolTaskScheduler scheduler = new ThreadPoolTaskScheduler();
        scheduler.setPoolSize(Runtime.getRuntime().availableProcessors() * 2);
        return scheduler;
    }

    //scheduler 시작
    public void startScheduler(Chore chore) {
        if (!choreScheduledTasks.containsKey(chore.getId())) {
            ThreadPoolTaskScheduler scheduler = poolTaskScheduler();
            scheduler.initialize();
            if (chore.getRepetition() != null) {
                changeCronSet(chore);
            }
            log.info("[startScheduler] choreId : " + chore.getId());
            scheduler.schedule(getRunnable(chore), getTrigger());
            choreScheduledTasks.put(chore.getId(), scheduler);
        }
    }

    public void startTodayScheduler(User user) {
        if (!todayScheduledTasks.containsKey(user.getId())) {
            ThreadPoolTaskScheduler scheduler = poolTaskScheduler();
            scheduler.initialize();
            if (!userAgreeRepository.findByUserIdAndStatus(user.getId(), ACTIVE_STATUS)
                    .orElseThrow(() -> new BaseException(BaseResponseCode.NOT_FOUND_USER)).getIsAgreeTodayNoti()) {
                stopTodayScheduler(user);
            } else {
                this.cron = "0 0 9 ? * *";
                log.info("[startTodayScheduler] userId : " + user.getId());
                scheduler.schedule(getTodayRunnable(user), getTrigger());
                todayScheduledTasks.put(user.getId(), scheduler);
            }
        }
    }

    public void changeCronSet(Chore chore) {
        Integer minutes = chore.getNotiTime().getMinute();
        Integer hours = chore.getNotiTime().getHour();
        Integer day = chore.getStartDate().getDayOfMonth();
        String dayOfWeek = chore.getStartDate().getDayOfWeek().toString();

        Repetition repetition = chore.getRepetition();
        if (repetition == Repetition.MONTHLY) {
            this.cron = String.format("0 %d %d %s * *", minutes, hours, day);
        } else if (repetition == Repetition.WEEKLY) {
            this.cron = String.format("0 %d %d ? * %s", minutes, hours, dayOfWeek.substring(0, 3));
        } else {
            this.cron = String.format("0 %d %d * * *", minutes, hours);
        }
    }

    public void stopScheduler(Chore chore) {
        if (choreScheduledTasks.containsKey(chore.getId())) {
            log.info("[stopScheduler] choreId : " + chore.getId());
            choreScheduledTasks.get(chore.getId()).shutdown();
        }
    }

    public void stopTodayScheduler(User user) {
        if (todayScheduledTasks.containsKey(user.getId())) {
            log.info("[stopTodayScheduler] userId : " + user.getId());
            todayScheduledTasks.get(user.getId()).shutdown();
        }
    }

    private Runnable getRunnable(Chore chore) {
        // do something
        return () -> {
            try {
                if (chore.getLastDate() != null && LocalDate.now().isAfter(chore.getLastDate())) {
                    stopScheduler(chore);
                } else {
                    log.info("[특정 집안일 알림 실행] choreId =" + chore.getId());
                    notificationService.notiChore(chore);
                }
            } catch (IOException e) {
                log.error(e.getMessage());
            }
        };
    }

    private Runnable getTodayRunnable(User user) {
        // do something
        return () -> {
            try {
                log.info("[오늘의 집안일 알림 실행] userId =" + user.getId());
                notificationService.todayChores(user);
            } catch (IOException e) {
                log.error(e.getMessage());
            }
        };
    }

    private Trigger getTrigger() {
        // cronSetting
        return new CronTrigger(cron);
    }

    @PostConstruct
    public void init() {
        //오늘의 집안일 알림
        List<UserAgree> userAgrees = userAgreeRepository.findAllByIsAgreeTodayNotiAndStatus(true, ACTIVE_STATUS);
        if (!userAgrees.isEmpty()) {
            for (UserAgree userAgree : userAgrees) {
                if (userAgree.getIsAgreeTodayNoti()) {
                    User user = userRepository.findByIdAndStatus(userAgree.getUser().getId(), ACTIVE_STATUS)
                            .orElseThrow(() -> new BaseException(BaseResponseCode.NOT_FOUND_USER));
                    startTodayScheduler(user);
                }
            }
        }
        //특정 집안일 알림
        List<Chore> chores = choreRepository.findAllByIsAcceptNotiAndStatus(true, ACTIVE_STATUS);
        if (!chores.isEmpty()) {
            for (Chore chore : chores) {
                startScheduler(chore);
            }
        }
    }

    @PreDestroy
    public void destroy() {//오늘의 집안일 알림
        List<UserAgree> userAgrees = userAgreeRepository.findAllByIsAgreeTodayNotiAndStatus(true, ACTIVE_STATUS);
        if (!userAgrees.isEmpty()) {
            for (UserAgree userAgree : userAgrees) {
                User user = userRepository.findByIdAndStatus(userAgree.getUser().getId(), ACTIVE_STATUS)
                        .orElseThrow(() -> new BaseException(BaseResponseCode.NOT_FOUND_USER));
                stopTodayScheduler(user);
            }
        }
        //특정 집안일 알림
        List<Chore> chores = choreRepository.findAllByIsAcceptNotiAndStatus(true, ACTIVE_STATUS);
        if (!chores.isEmpty()) {
            for (Chore chore : chores) {
                stopScheduler(chore);
            }
        }
    }

    public void login(User user) {
        //오늘의 집안일 알림
        UserAgree userAgree = userAgreeRepository.findByUserIdAndStatus(user.getId(), ACTIVE_STATUS).orElseThrow(() -> new BaseException(BaseResponseCode.NOT_FOUND_USER));
        if(userAgree.getIsAgreeTodayNoti())
            startTodayScheduler(user);

        //특정 집안일 알림
        List<Chore> chores = choreRepository.findAllByUserAndIsAcceptNotiAndStatus(user, true, ACTIVE_STATUS);
        if (!chores.isEmpty()) {
            for (Chore chore : chores) {
                startScheduler(chore);
            }
        }
    }

    public void logout(User user) {
        //오늘의 집안일 알림
        UserAgree userAgree = userAgreeRepository.findByUserIdAndStatus(user.getId(), ACTIVE_STATUS).orElseThrow(() -> new BaseException(BaseResponseCode.NOT_FOUND_USER));
        stopTodayScheduler(user);

        //특정 집안일 알림
        List<Chore> chores = choreRepository.findAllByUserAndIsAcceptNotiAndStatus(user, true, ACTIVE_STATUS);
        if (!chores.isEmpty()) {
            for (Chore chore : chores) {
                stopScheduler(chore);
            }
        }
    }
}