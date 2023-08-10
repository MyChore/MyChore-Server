package com.mychore.mychore_server.service;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.MulticastMessage;
import com.mychore.mychore_server.dto.notification.NotiAssembler;
import com.mychore.mychore_server.dto.notification.request.NotiChoreReq;
import com.mychore.mychore_server.dto.notification.request.NotiReq;
import com.mychore.mychore_server.entity.chore.Chore;
import com.mychore.mychore_server.entity.chore.ChoreLog;
import com.mychore.mychore_server.entity.group.Group;
import com.mychore.mychore_server.entity.group.GroupUser;
import com.mychore.mychore_server.entity.user.User;
import com.mychore.mychore_server.entity.user.UserAgree;
import com.mychore.mychore_server.global.exception.BaseException;
import com.mychore.mychore_server.global.exception.BaseResponseCode;
import com.mychore.mychore_server.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Arrays;
import java.util.List;

import static com.mychore.mychore_server.global.constants.Constant.*;
import static com.mychore.mychore_server.global.constants.Constant.Notification.*;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final UserRepository userRepository;
    private final GroupRepository groupRepository;
    private final ChoreRepository choreRepository;
    private final NotiAssembler notiAssembler;
    private final NotificationRepository notificationRepository;
    private final GroupUserRepository groupUserRepository;
    private final ChoreLogRepository choreLogRepository;
    private final UserAgreeRepository userAgreeRepository;

    // FCM AccessToken 발급
    private static String getAccessToken() throws IOException {

        GoogleCredentials googleCredentials = GoogleCredentials
                .fromStream(new ClassPathResource("mychore-firbase-private-key.json").getInputStream())
                .createScoped(Arrays.asList(SCOPES));
        googleCredentials.refreshIfExpired();
        return googleCredentials.getAccessToken().getTokenValue();
    }

    // fcm message 전송
    public void getConnection(String message) {
        try {
            URL url = new URL(BASE_URL + FCM_SEND_ENDPOINT);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            conn.setRequestMethod("POST");
            conn.setRequestProperty("Authorization", "Bearer " + getAccessToken());
            conn.setRequestProperty("Content-Type", "application/json; utf-8");
            conn.setDoOutput(true);

            OutputStream os = conn.getOutputStream();

            os.write(message.getBytes("utf-8"));
            os.flush();
            os.close();

            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "euc-kr"));
            StringBuffer stringBuffer = new StringBuffer();
            String inputLine;

            while ((inputLine = br.readLine()) != null) {
                stringBuffer.append(inputLine);
            }
            br.close();

            System.out.println("Successfully sent message: " + message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // 그룹원 집안일 완료 알림
    public MulticastMessage groupChore(NotiChoreReq requestDTO, Long userId) {

        User user = userRepository.findByIdAndStatus(userId, ACTIVE_STATUS).orElseThrow(() -> new BaseException(BaseResponseCode.NOT_FOUND_USER));
        UserAgree userAgree = userAgreeRepository.findByUserIdAndStatus(userId, ACTIVE_STATUS).orElseThrow(() -> new BaseException(BaseResponseCode.NOT_FOUND_USER));
        if (!userAgree.getIsAgreeDoneNoti()) {
            throw new BaseException(BaseResponseCode.NOT_ACCEPT_NOTI);
        }
        Group group = groupRepository.findGroupByIdAndStatus(requestDTO.getGroupId(), ACTIVE_STATUS).orElseThrow(() -> new BaseException(BaseResponseCode.NOT_FOUND_GROUP));

        List<GroupUser> groupUsers = groupUserRepository.findGroupUsersByGroupAndStatus(group, ACTIVE_STATUS);

        if (!user.getDeviceToken().equals(requestDTO.getDeviceToken())) {
            throw new BaseException(BaseResponseCode.INVALID_DEVICE_TOKEN);
        }
        Chore chore = choreRepository.findById(requestDTO.getChoreId()).orElseThrow((() -> new BaseException(BaseResponseCode.NOT_FOUND_CHORE)));
        ChoreLog choreLog = choreLogRepository.findFirstByChoreOrderByUpdatedAtDesc(chore).orElseThrow(() -> new BaseException(BaseResponseCode.NOT_FOUND_CHORE_LOG));
        if (!choreLog.getIsComplete()) {
            throw new BaseException(BaseResponseCode.NOT_COMPLETE_CHORE);
        }


        // 집안일을 완료한 그룹원을 제외한 list
        List<User> findUsers = groupUsers.stream().map(GroupUser::getUser)
                .filter(findUser -> findUser.getStatus().equals(ACTIVE_STATUS)
                        && !findUser.equals(user)).toList();

        List<String> deviceTokens = findUsers.stream().map(User::getDeviceToken).filter(deviceToken -> !deviceToken.isEmpty()).toList();
        if (deviceTokens.isEmpty()) {
            throw new BaseException(BaseResponseCode.NOT_FOUND_GROUP_DEVICE_TOKEN);
        }

        String body = "그룹원 " + user.getNickname() + "님이" + chore.getName() + "를 완료했습니다!!";

        findUsers.forEach(findUser -> notificationRepository.save(notiAssembler.toEntity(findUser, group, "MyChore", body)));

        return notiAssembler.toEntity(notiAssembler.toEntity(TITLE, body), notiAssembler.toEntity(), deviceTokens);
    }

    // 오늘의 집안일 알림
    public Message todayChores(NotiReq requestDTO, Long userId) {

        User user = userRepository.findByIdAndStatus(userId, ACTIVE_STATUS).orElseThrow(() -> new BaseException(BaseResponseCode.NOT_FOUND_USER));
        UserAgree userAgree = userAgreeRepository.findByUserIdAndStatus(userId, ACTIVE_STATUS).orElseThrow(() -> new BaseException(BaseResponseCode.NOT_FOUND_USER));
        if (!userAgree.getIsAgreeTodayNoti()) {
            throw new BaseException(BaseResponseCode.NOT_ACCEPT_NOTI);
        }
        Group group = groupRepository.findGroupByIdAndStatus(requestDTO.getGroupId(), ACTIVE_STATUS).orElseThrow(() -> new BaseException(BaseResponseCode.NOT_FOUND_GROUP));

        if (!user.getDeviceToken().equals(requestDTO.getDeviceToken())) {
            throw new BaseException(BaseResponseCode.INVALID_DEVICE_TOKEN);
        }
        List<Chore> chores = choreRepository.findAllByUserAndGroup(user, group);

        String body = null;
        if (chores.isEmpty()) {
            body = "오늘은 해야하는 집안일이 없습니다. 푹 쉬세요!";
        } else {
            List<String> list = chores.stream().map(Chore::getName).toList();
            body = "오늘은 " + String.join(COMMA, list) + "가 있는 날입니다! MyChore에서 함께 해봐요.";
        }

        notificationRepository.save(notiAssembler.toEntity(user, group, TITLE, body));

        return notiAssembler.toEntity(notiAssembler.toEntity(TITLE, body), notiAssembler.toEntity(), requestDTO.getDeviceToken());
    }

    // 새로운 구성원 추가 알림
    public MulticastMessage newMember(NotiReq requestDTO, Long userId) {
        User user = userRepository.findByIdAndStatus(userId, ACTIVE_STATUS).orElseThrow(() -> new BaseException(BaseResponseCode.NOT_FOUND_USER));
        UserAgree userAgree = userAgreeRepository.findByUserIdAndStatus(userId, ACTIVE_STATUS).orElseThrow(() -> new BaseException(BaseResponseCode.NOT_FOUND_USER));
        if (!userAgree.getIsAgreeNewUserNoti()) {
            throw new BaseException(BaseResponseCode.NOT_ACCEPT_NOTI);
        }
        if (!user.getDeviceToken().equals(requestDTO.getDeviceToken())) {
            throw new BaseException(BaseResponseCode.INVALID_DEVICE_TOKEN);
        }

        Group group = groupRepository.findGroupByIdAndStatus(requestDTO.getGroupId(), ACTIVE_STATUS).orElseThrow(() -> new BaseException(BaseResponseCode.NOT_FOUND_GROUP));

        List<GroupUser> groupUsers = groupUserRepository.findGroupUsersByGroupAndStatus(group, ACTIVE_STATUS);


        List<User> findUsers = groupUsers.stream().map(GroupUser::getUser)
                .filter(findUser -> findUser.getStatus().equals(ACTIVE_STATUS)
                        && !findUser.equals(user)).toList();

        List<String> deviceTokens = findUsers.stream().map(User::getDeviceToken).filter(deviceToken -> !deviceToken.isEmpty()).toList();
        if (deviceTokens.isEmpty()) {
            throw new BaseException(BaseResponseCode.NOT_FOUND_GROUP_DEVICE_TOKEN);
        }

        String body = group.getName() + " 그룹에 " + user.getNickname() + "님이 들어왔습니다. 환영해주세요!";

        findUsers.forEach(findUser -> notificationRepository.save(notiAssembler.toEntity(findUser, group, TITLE, body)));

        return notiAssembler.toEntity(notiAssembler.toEntity(TITLE, body), notiAssembler.toEntity(), deviceTokens);
    }

    // 그룹 삭제 알림
    public MulticastMessage deleteGroup(NotiReq requestDTO, Long userId) {
        User user = userRepository.findByIdAndStatus(userId, ACTIVE_STATUS).orElseThrow(() -> new BaseException(BaseResponseCode.NOT_FOUND_USER));
        UserAgree userAgree = userAgreeRepository.findByUserIdAndStatus(userId, ACTIVE_STATUS).orElseThrow(() -> new BaseException(BaseResponseCode.NOT_FOUND_USER));
        if (!userAgree.getIsAgreeDeleteNoti()) {
            throw new BaseException(BaseResponseCode.NOT_ACCEPT_NOTI);
        }
        Group group = groupRepository.findGroupByIdAndStatus(requestDTO.getGroupId(), ACTIVE_STATUS).orElseThrow(() -> new BaseException(BaseResponseCode.NOT_FOUND_GROUP));

        List<GroupUser> groupUsers = groupUserRepository.findGroupUsersByGroupAndStatus(group, ACTIVE_STATUS);

        if (!user.getDeviceToken().equals(requestDTO.getDeviceToken())) {
            throw new BaseException(BaseResponseCode.INVALID_DEVICE_TOKEN);
        }

        List<User> findUsers = groupUsers.stream().map(GroupUser::getUser)
                .filter(findUser -> findUser.getStatus().equals(ACTIVE_STATUS)
                        && !findUser.equals(user)).toList();

        List<String> deviceTokens = findUsers.stream().map(User::getDeviceToken).filter(deviceToken -> !deviceToken.isEmpty()).toList();
        if (deviceTokens.isEmpty()) {
            throw new BaseException(BaseResponseCode.NOT_FOUND_GROUP_DEVICE_TOKEN);
        }

        String body = "그룹장이 그룹을 나가 " + group.getName() + " 그룹이 삭제되었습니다.";

        findUsers.forEach(findUser -> notificationRepository.save(notiAssembler.toEntity(findUser, group, TITLE, body)));
        return notiAssembler.toEntity(notiAssembler.toEntity(TITLE, body), notiAssembler.toEntity(), deviceTokens);
    }

    // 특정 시간 집안일 알림
    public Message notiChore(NotiChoreReq requestDTO, Long userId) {
        User user = userRepository.findByIdAndStatus(userId, ACTIVE_STATUS).orElseThrow(() -> new BaseException(BaseResponseCode.NOT_FOUND_USER));
        UserAgree userAgree = userAgreeRepository.findByUserIdAndStatus(userId, ACTIVE_STATUS).orElseThrow(() -> new BaseException(BaseResponseCode.NOT_FOUND_USER));
        Group group = groupRepository.findGroupByIdAndStatus(requestDTO.getGroupId(), ACTIVE_STATUS).orElseThrow(() -> new BaseException(BaseResponseCode.NOT_FOUND_GROUP));
        Chore chore = choreRepository.findById(requestDTO.getChoreId()).orElseThrow((() -> new BaseException(BaseResponseCode.NOT_FOUND_CHORE)));

        if (!userAgree.getIsAgreeChoreNoti() || !chore.getIsAcceptNoti()) {
            throw new BaseException(BaseResponseCode.NOT_ACCEPT_NOTI);
        }

        if (!user.getDeviceToken().equals(requestDTO.getDeviceToken())) {
            throw new BaseException(BaseResponseCode.INVALID_DEVICE_TOKEN);
        }


        if (choreLogRepository.findFirstByChoreOrderByUpdatedAtDesc(chore).orElseThrow(() -> new BaseException(BaseResponseCode.NOT_FOUND_CHORE_LOG)).getIsComplete()) {
            throw new BaseException(BaseResponseCode.ALREADY_COMPLETE_CHORE);
        }
        String body = chore.getName() + " 를 해야할 시간입니다! MyChore에서 함께해봐요";

        notificationRepository.save(notiAssembler.toEntity(user, group, TITLE, body));

        return notiAssembler.toEntity(notiAssembler.toEntity(TITLE, body), notiAssembler.toEntity(), requestDTO.getDeviceToken());
    }
}
