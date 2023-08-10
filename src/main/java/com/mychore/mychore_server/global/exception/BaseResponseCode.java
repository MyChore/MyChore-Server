package com.mychore.mychore_server.global.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.util.Arrays;

@Getter
@AllArgsConstructor
public enum BaseResponseCode {
    // success
    SUCCESS("S0001", HttpStatus.OK, "요청에 성공했습니다."),

    // global
    NO_PERMISSION("GL001", HttpStatus.FORBIDDEN, "권한이 없습니다."),
    NULL_REQUEST_PARAM("GL002", HttpStatus.BAD_REQUEST, "쿼리 파라미터가 없습니다."),
    BAD_REQUEST("GL003", HttpStatus.BAD_REQUEST, "잘못된 요청입니다."),
    CONTENT_NULL("GL006", HttpStatus.BAD_REQUEST, "내용을 입력해 주세요."),

    // token
    AUTH_ANNOTATION_IS_NOWHERE("T0001", HttpStatus.UNAUTHORIZED, "토큰을 통해 userId를 추출하는 메서드에는 @Auth 어노테이션을 붙여주세요."),
    EXPIRED_TOKEN("T0002", HttpStatus.FORBIDDEN, "만료된 토큰 값입니다."),
    INVALID_DEVICE_TOKEN("T0003",HttpStatus.BAD_REQUEST, "잘못된 디바이스 토큰값입니다."),
    NOT_FOUND_GROUP_DEVICE_TOKEN("T0004", HttpStatus.NOT_FOUND,"그룹안의 알림을 보낼 수 있는 디바이스토큰이 없습니다."),

    // user
    NOT_FOUND_USER("U0001", HttpStatus.NOT_FOUND, "사용자를 찾을 수 없습니다."),
    ALREADY_EXIST_EMAIL("U0002", HttpStatus.CONFLICT, "이미 가입되어 있는 이메일입니다."),
    NOT_FOUND_EMAIL("U0003", HttpStatus.NOT_FOUND, "해당 이메일로 가입한 사용자를 찾을 수 없습니다."),
    INVALID_NICKNAME("U0004", HttpStatus.BAD_REQUEST, "닉네임 형식이 맞지 않습니다."),
    ALREADY_EXIST_NICKNAME("U0005", HttpStatus.CONFLICT, "이미 사용중인 닉네임입니다."),
    INVALID_GENDER("U0006", HttpStatus.BAD_REQUEST, "잘못된 성별입니다."),
    INVALID_PROVIDER("U0007", HttpStatus.BAD_REQUEST, "잘못된 Provider 입니다."),
    INVALID_NOTI_TYPE("U0008", HttpStatus.BAD_REQUEST, "잘못된 알림 타입입니다."),
    NULL_NICKNAME("U0009", HttpStatus.BAD_REQUEST, "닉네임을 입력해 주세요."),
    NULL_EMAIL("U0010", HttpStatus.BAD_REQUEST, "이메일을 입력해 주세요."),
    NULL_PROVIDER("U0011", HttpStatus.BAD_REQUEST, "Provider를 입력해 주세요."),
    NULL_IS_14OVER("U0012", HttpStatus.BAD_REQUEST, "만 14세 이상 동의 여부를 입력해주세요."),
    NULL_IS_Accept_EMAIL("U0013", HttpStatus.BAD_REQUEST, "이메일 수신 동의 여부를 입력해주세요."),
    NOT_ACCEPT_NOTI("U0014",HttpStatus.BAD_REQUEST,"사용자가 알림을 허용하지 않았습니다."),

    // group
    NOT_FOUND_GROUP("G0001", HttpStatus.NOT_FOUND, "그룹을 찾을 수 없습니다."),
    ALREADY_JOIN_GROUP("G0002", HttpStatus.CONFLICT, "이미 참여하고 있는 그룹입니다."),
    INVALID_INVITATION_CODE("G0003", HttpStatus.BAD_REQUEST, "유효하지 않은 초대코드입니다."),
    NOT_FOUND_FURNITURE("G0004", HttpStatus.NOT_FOUND, "가구를 찾을 수 없습니다."),
    INVALID_FURNITURE_TYPE("G0005", HttpStatus.BAD_REQUEST, "잘못된 가구 타입입니다."),
    NOT_FOUND_ROOM("G0006", HttpStatus.NOT_FOUND, "방을 찾을 수 없습니다."),
    NOT_FOUND_ROOM_FURNITURE("G0007", HttpStatus.NOT_FOUND, "방의 가구를 찾을 수 없습니다."),
    NULL_GROUP_ID("G0008", HttpStatus.BAD_REQUEST, "그룹을 입력해주세요."),
    NULL_ROOM_FURNITURE("G0009", HttpStatus.BAD_REQUEST, "가구를 입력해주세요."),

    // chore
    NOT_FOUND_CHORE("C0001", HttpStatus.NOT_FOUND, "집안일을 찾을 수 없습니다."),
    INVALID_PERIOD("C0002", HttpStatus.BAD_REQUEST, "잘못된 조회기간입니다."),
    NULL_NOTI_TIME("C0003", HttpStatus.BAD_REQUEST, "알림시간을 입력해주세요."),
    INVALID_LAST_DATE("C0004", HttpStatus.BAD_REQUEST, "잘못된 종료일자입니다."),
    INVALID_LOG_DATE("C0005", HttpStatus.BAD_REQUEST, "잘못된 로그일자입니다."),
    NULL_CHORE_USER_ID("C0006", HttpStatus.BAD_REQUEST, "담당자를 입력해주세요."),
    NULL_CHORE_NAME("C0007", HttpStatus.BAD_REQUEST, "집안일 이름을 입력해주세요."),
    NULL_START_DATE("C0008", HttpStatus.BAD_REQUEST, "시작날짜를 입력해주세요."),
    NULL_IS_ACCEPT_NOTI("C0009", HttpStatus.BAD_REQUEST, "알림 수신여부를 입력해주세요."),
    ALREADY_COMPLETE_CHORE("C0010", HttpStatus.CONFLICT,"이미 완료한 집안일입니다."),
    NOT_COMPLETE_CHORE("C0011", HttpStatus.CONFLICT, "완료되지 않은 집안일 입니다."),
    NOT_FOUND_CHORE_LOG("C0012", HttpStatus.NOT_FOUND, "집안일 로그를 찾을 수 없습니다."),

    ;

    public final String code;
    public final HttpStatus status;
    public final String message;

    public static BaseResponseCode findByCode(String code) {
        return Arrays.stream(BaseResponseCode.values())
                .filter(b -> b.getCode().equals(code))
                .findAny().orElseThrow(() -> new BaseException(BAD_REQUEST));
    }
}
