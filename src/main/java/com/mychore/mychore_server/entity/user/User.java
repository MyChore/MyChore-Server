package com.mychore.mychore_server.entity.user;

import com.mychore.mychore_server.dto.user.request.PatchProfileReq;
import com.mychore.mychore_server.global.constants.Gender;
import com.mychore.mychore_server.global.constants.Provider;
import com.mychore.mychore_server.entity.BaseEntity;
import com.mychore.mychore_server.global.entityListener.UserEntityListener;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.SQLDelete;

import java.time.LocalDate;

import static com.mychore.mychore_server.global.constants.Constant.INACTIVE_STATUS;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@DynamicInsert
@DynamicUpdate
@Getter
@SQLDelete(sql = "UPDATE user SET status = 'inactive', updated_at = current_timestamp WHERE user_id = ?")
@EntityListeners(UserEntityListener.class)
public class User extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    @NonNull
    @Column(length = 100)
    private String email;

    @NonNull
    @Column(length = 10)
    private String nickname;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    private LocalDate birth;

    private String imgUrl;

    @NonNull
    @Enumerated(EnumType.STRING)
    private Provider provider;

    private String refreshToken;

    private String deviceToken;

    @Builder
    public User(@NonNull String email, @NonNull String nickname, Gender gender, LocalDate birth, String imgUrl, @NonNull Provider provider) {
        this.email = email;
        this.nickname = nickname;
        this.gender = gender;
        this.birth = birth;
        this.imgUrl = imgUrl;
        this.provider = provider;
        this.deviceToken = deviceToken;
    }

    public void editProfile(PatchProfileReq patchProfileReq) {
        this.nickname = patchProfileReq.getNickname();
        this.birth = LocalDate.parse(patchProfileReq.getBirth());
        this.gender = Gender.getByName(patchProfileReq.getGender());
    }

    public void editImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public void updateRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public void removeTokens() {
        this.refreshToken = null;
    }

    public void withdraw() {
        removeTokens();
        this.setStatus(INACTIVE_STATUS);
    }
}
