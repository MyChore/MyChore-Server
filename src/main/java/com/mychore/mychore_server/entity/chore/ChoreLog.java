package com.mychore.mychore_server.entity.chore;

import com.mychore.mychore_server.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class ChoreLog extends BaseEntity {

    @Id @GeneratedValue
    @Column(name = "chore_log_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "chore_id")
    private Chore chore;

    private Boolean isComplete = false;
}
