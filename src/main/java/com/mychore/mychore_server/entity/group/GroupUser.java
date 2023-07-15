package com.mychore.mychore_server.entity.group;

import com.mychore.mychore_server.constant.Role;
import com.mychore.mychore_server.entity.BaseEntity;
import com.mychore.mychore_server.entity.user.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class GroupUser extends BaseEntity {

    @Id @GeneratedValue
    @Column(name = "group_user_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "group_id")
    private Group group;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Enumerated(EnumType.STRING)
    @NonNull
    private Role role;
}
