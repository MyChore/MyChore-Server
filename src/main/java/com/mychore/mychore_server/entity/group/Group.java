package com.mychore.mychore_server.entity.group;

import com.mychore.mychore_server.global.constants.FloorType;
import com.mychore.mychore_server.entity.BaseEntity;
import com.mychore.mychore_server.global.entityListener.GroupEntityListener;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.SQLDelete;

@Entity
@DynamicInsert @DynamicUpdate
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Table(name = "floor_group")
@SQLDelete(sql = "UPDATE floor_group SET status = 'inactive', updated_at = current_timestamp WHERE group_id = ?")
@EntityListeners(GroupEntityListener.class)
public class Group extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "group_id")
    private Long id;

    @NonNull
    @Column(length = 8)
    private String inviteCode;

    @NonNull
    @Column(length = 10)
    private String name;

    @NonNull
    @Enumerated(EnumType.STRING)
    private FloorType floorType;

    @Builder
    public Group(String inviteCode, String name, FloorType floorType){
        this.inviteCode = inviteCode;
        this.name = name;
        this.floorType = floorType;
    }

    public void SetId(Long groupId){
        this.id = groupId;
    }

    public void SetName(String name) { this.name = name; }
}
