package com.mychore.mychore_server.service;

import com.mychore.mychore_server.dto.user.UserAssembler;
import com.mychore.mychore_server.dto.user.request.PatchProfileReq;
import com.mychore.mychore_server.dto.user.request.UserLogInReq;
import com.mychore.mychore_server.dto.user.response.UserTokenRes;
import com.mychore.mychore_server.dto.user.request.UserSignUpReq;
import com.mychore.mychore_server.entity.user.User;
import com.mychore.mychore_server.exception.user.EmailAlreadyExistException;
import com.mychore.mychore_server.exception.user.EmailNotExistException;
import com.mychore.mychore_server.exception.user.NicknameAlreadyExistException;
import com.mychore.mychore_server.exception.user.UserNotFoundException;
import com.mychore.mychore_server.global.constants.Provider;
import com.mychore.mychore_server.global.utils.JwtUtils;
import com.mychore.mychore_server.repository.UserAgreeRepository;
import com.mychore.mychore_server.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.mychore.mychore_server.global.constants.Constant.ACTIVE_STATUS;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserAgreeRepository userAgreeRepository;
    private final JwtUtils jwtUtils;
    private final UserAssembler userAssembler;

    // 회원가입
    @Transactional
    public UserTokenRes signUp(UserSignUpReq userSignUpReq) {
        if (userRepository.findByEmailAndStatus(userSignUpReq.getEmail(), ACTIVE_STATUS).isPresent()) {
            throw new EmailAlreadyExistException();
        } else if (!checkNicknameWithSignUp(userSignUpReq.getNickname())) {
            throw new NicknameAlreadyExistException();
        } else {
            User user = userRepository.save(userAssembler.toEntity(userSignUpReq));
            userAgreeRepository.save(userAssembler.toEntity(user, userSignUpReq));
            return UserTokenRes.toDto(jwtUtils.createToken(user));
        }
    }

    // 로그인
    public UserTokenRes login(UserLogInReq userLogInReq){
        User user = userRepository.findByEmailAndProviderAndStatus(userLogInReq.getEmail(), Provider.getByName(userLogInReq.getProvider()), ACTIVE_STATUS)
                .orElseThrow(EmailNotExistException::new);
        return UserTokenRes.toDto(jwtUtils.createToken(user));
    }

    // 프로필 수정
    public void editProfile(Long userId, PatchProfileReq patchProfileReq) {
        User user = userRepository.findByIdAndStatus(userId, ACTIVE_STATUS).orElseThrow(UserNotFoundException::new);
        if (checkNicknameWithEdit(patchProfileReq.getNickname(), userId)){
            user.editProfile(patchProfileReq);
            userRepository.save(user);
        } else throw new NicknameAlreadyExistException();
    }

    // 가입 시 닉네임 중복체크
    public boolean checkNicknameWithSignUp(String nickname){
        userAssembler.isValidNickname(nickname);
        return userRepository.findByNicknameAndStatus(nickname, ACTIVE_STATUS).isEmpty();
    }

    // 프로필 수정 시 닉네임 중복체크
    private boolean checkNicknameWithEdit(String nickname, Long userId){
        userAssembler.isValidNickname(nickname);
        return userRepository.findByNicknameAndStatusAndIdNot(nickname, ACTIVE_STATUS, userId).isEmpty();
    }
}
