package com.mychore.mychore_server.entity.user;

import com.mychore.mychore_server.dto.user.request.PatchProfileReq;
import com.mychore.mychore_server.global.constants.Gender;
import com.mychore.mychore_server.global.constants.Provider;
import com.mychore.mychore_server.entity.BaseEntity;
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

    private String imgKey;

    @NonNull
    @Enumerated(EnumType.STRING)
    private Provider provider;

    private String refreshToken;

    private String deviceToken;

    @Builder
    public User(@NonNull String email, @NonNull String nickname, Gender gender, LocalDate birth, String imgKey, @NonNull Provider provider, String deviceToken) {
        this.email = email;
        this.nickname = nickname;
        this.gender = gender;
        this.birth = birth;
        this.imgKey = imgKey;
        this.provider = provider;
        this.deviceToken = deviceToken;
    }

    public void editProfile(PatchProfileReq patchProfileReq) {
        editNickname(patchProfileReq.getNickname());
        editBirth(patchProfileReq.getBirth());
        editGender(patchProfileReq.getGender());
    }

    public void editGender(String gender) {
        this.gender = Gender.getByName(gender);
    }

    public void editBirth(String date) {
        this.birth = LocalDate.parse(date);
    }

    public void editNickname(String nickname) {
        this.nickname = nickname;
    }

    public void editImgKey(String imgKey) {
        this.imgKey = imgKey;
    }

    public void updateRefreshToken(String refreshToken){
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
