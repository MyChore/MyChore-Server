package com.mychore.mychore_server.dto.user;

import com.mychore.mychore_server.dto.user.request.UserSignUpReq;
import com.mychore.mychore_server.dto.user.response.GetProfileRes;
import com.mychore.mychore_server.entity.user.User;
import com.mychore.mychore_server.entity.user.UserAgree;
import com.mychore.mychore_server.global.constants.Gender;
import com.mychore.mychore_server.global.constants.Provider;
import com.mychore.mychore_server.global.exception.BaseException;
import com.mychore.mychore_server.global.exception.BaseResponseCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
@RequiredArgsConstructor
public class UserAssembler {
    public User toEntity(UserSignUpReq userSignUpReq){
        return User.builder()
                .email(userSignUpReq.getEmail())
                .imgKey(userSignUpReq.getImgKey())
                .nickname(userSignUpReq.getNickname())
                .gender(Gender.getByName(userSignUpReq.getGender()))
                .birth(LocalDate.parse(userSignUpReq.getBirth()))
                .provider(Provider.getByName(userSignUpReq.getProvider()))
                .build();
    }

    public UserAgree toEntity(User user, UserSignUpReq userSignUpReq){
        return UserAgree.builder()
                .user(user)
                .is14Over(userSignUpReq.getIs14Over())
                .acceptEmailDate(userSignUpReq.getIsAcceptEmail() ? LocalDateTime.now() : null)
                .build();
    }

    public GetProfileRes toGetProfileDto(User user){
        return GetProfileRes.builder()
                .nickname(user.getNickname())
                .email(user.getEmail())
                .imgKey(user.getImgKey())
                .gender(user.getGender().getGenderName())
                .birth(user.getBirth().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")))
                .build();
    }

    //  1자 이상 8자 이하, 한글, 영어, 숫자, 언더바로 구성
    //  특이사항 : 한글 초성 및 모음은 허가하지 않는다.
    public void isValidNickname(String nickname) {
        String regex = "^[a-zA-Z0-9가-힣_]{1,8}$";
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(nickname);
        if(!m.matches()) throw new BaseException(BaseResponseCode.INVALID_NICKNAME);
    }
}
