package com.mychore.mychore_server.entity.group;

import com.mychore.mychore_server.global.constants.Role;
import com.mychore.mychore_server.entity.BaseEntity;
import com.mychore.mychore_server.entity.user.User;
import com.mychore.mychore_server.global.entityListener.GroupUserEntityListener;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.SQLDelete;

@Entity
@DynamicInsert @DynamicUpdate
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@SQLDelete(sql = "UPDATE group_user SET status = 'inactive', updated_at = current_timestamp WHERE group_user_id = ?")
@EntityListeners(GroupUserEntityListener.class)
public class GroupUser extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "group_user_id")
    private Long id;

    @NonNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "group_id")
    private Group group;

    @NonNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @NonNull
    @Enumerated(EnumType.STRING)
    private Role role;

    @Builder
    public GroupUser(Group group, User user, Role role){
        this.group = group;
        this.user = user;
        this.role = role;
    }
}
