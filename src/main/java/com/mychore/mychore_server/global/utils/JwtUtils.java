package com.mychore.mychore_server.global.utils;

import com.mychore.mychore_server.entity.user.User;
import com.mychore.mychore_server.exception.user.TokenExpirationException;
import com.mychore.mychore_server.exception.user.UserNotFoundException;
import com.mychore.mychore_server.repository.UserRepository;
import io.jsonwebtoken.*;
import io.netty.handler.codec.compression.CompressionException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.Objects;

import static com.mychore.mychore_server.global.constants.Constant.ACTIVE_STATUS;
import static com.mychore.mychore_server.global.constants.Constant.COMMA;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtUtils {

    public static final String USER_ID = "userId";
    public static final String NICKNAME = "nickname";
    public static final String SPACE = " ";

    private final UserRepository userRepository;

    @Value("${jwt.secretKey}")
    public String secretKey;

    @Value("${jwt.token-type}")
    public String tokenType;

    @Value("${jwt.access.name}")
    public String accessName;

    @Value("${jwt.refresh.name}")
    public String refreshName;

    @Value("${jwt.access.expiration}")
    public String accessExTime;

    @Value("${jwt.refresh.expiration}")
    public String refreshExTime;

    @Value("${jwt.auth-header}")
    private String header;


    public String createToken(User user) {
        String access_token = this.createAccessToken(user.getId(), user.getNickname());
        String refresh_token = this.createRefreshToken(user.getId(), user.getNickname());
        return access_token + COMMA + refresh_token;
    }

    public String createAccessToken(Long userId, String nickname) {
        Claims claims = Jwts.claims()
                .setSubject(accessName)
                .setIssuedAt(new Date());
        claims.put(USER_ID, userId);
        claims.put(NICKNAME, nickname);
        Date ext = new Date();
        ext.setTime(ext.getTime() + Long.parseLong(Objects.requireNonNull(accessExTime)));
        String accessToken = Jwts.builder()
                .setHeaderParam("typ", "JWT")
                .setClaims(claims)
                .setExpiration(ext)
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
        return tokenType + SPACE + accessToken;
    }

    public String createRefreshToken(Long userId, String nickname) {
        Claims claims = Jwts.claims()
                .setSubject(refreshName)
                .setIssuedAt(new Date());
        claims.put(USER_ID, userId);
        claims.put(NICKNAME, nickname);
        Date ext = new Date();
        ext.setTime(ext.getTime() + Long.parseLong(Objects.requireNonNull(refreshExTime)));
        String refreshToken = Jwts.builder()
                .setHeaderParam("typ", "JWT")
                .setClaims(claims)
                .setExpiration(ext)
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
        User user = userRepository.findByIdAndStatus(userId, ACTIVE_STATUS)
                .orElseThrow(UserNotFoundException::new);
        user.updateRefreshToken(tokenType + SPACE + refreshToken);
        userRepository.save(user);
        return tokenType + SPACE + refreshToken;
    }

    public boolean isValidToken(String justToken) {
        if (justToken != null && justToken.split(SPACE).length == 2)
            justToken = justToken.split(SPACE)[1];
        try {
            Claims claims = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(justToken).getBody();
            return true;
        } catch (ExpiredJwtException exception) {
            log.error("Token Tampered");
            return true;
        } catch (MalformedJwtException exception) {
            log.error("Token MalformedJwtException");
            return false;
        } catch (ClaimJwtException exception) {
            log.error("Token ClaimJwtException");
            return false;
        } catch (UnsupportedJwtException exception) {
            log.error("Token UnsupportedJwtException");
            return false;
        } catch (CompressionException exception) {
            log.error("Token CompressionException");
            return false;
        } catch (RequiredTypeException exception) {
            log.error("Token RequiredTypeException");
            return false;
        } catch (NullPointerException exception) {
            log.error("Token is null");
            return false;
        } catch (Exception exception) {
            log.error("Undefined ERROR");
            return false;
        }
    }

    private Claims getJwtBodyFromJustToken(String justToken) {
        try {
            return Jwts.parser()
                    .setSigningKey(secretKey)
                    .parseClaimsJws(justToken)
                    .getBody();
        } catch (ExpiredJwtException e) {
            throw new TokenExpirationException();
        }
    }

    public boolean isTokenExpired(String justToken) {
        if (justToken != null && justToken.split(SPACE).length == 2)
            justToken = justToken.split(SPACE)[1];
        try {
            Jwts.parser().setSigningKey(secretKey).parseClaimsJws(justToken).getBody();
        } catch (ExpiredJwtException e) {
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return false;
    }

    public String getUserIdFromFullToken(String fullToken) {
        return String.valueOf(getJwtBodyFromJustToken(parseJustTokenFromFullToken(fullToken)).get(USER_ID));
    }

    public String getNicknameFromFullToken(String fullToken) {
        return String.valueOf(getJwtBodyFromJustToken(parseJustTokenFromFullToken(fullToken)).get(NICKNAME));
    }

    // "Bearer eyi35..."에서 토큰값만 추출
    public String parseJustTokenFromFullToken(String fullToken) {
        if (StringUtils.hasText(fullToken)
                && fullToken.startsWith(Objects.requireNonNull(tokenType))
        )
            return fullToken.split(SPACE)[1];
        return null;
    }

    @Transactional
    public String accessExpiration(Long userId) {
        User user = userRepository.findByIdAndStatus(userId, ACTIVE_STATUS)
                .orElseThrow(UserNotFoundException::new);
        String userRefreshToken = user.getRefreshToken();
        if (userRefreshToken == null) throw new TokenExpirationException();
        String refreshNickname = getNicknameFromFullToken(userRefreshToken);
        if (refreshNickname.isEmpty()) throw new TokenExpirationException();

        //토큰이 만료되었을 경우.
        return createAccessToken(userId, refreshNickname);
    }

}
