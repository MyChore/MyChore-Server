package com.mychore.mychore_server.entity.chore;

import com.mychore.mychore_server.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class ChoreLog extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "chore_log_id")
    private Long id;

    @NonNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "chore_id")
    private Chore chore;

    @NonNull
    @Column(columnDefinition = "BOOLEAN DEFAULT true")
    private Boolean isComplete = true;
}
