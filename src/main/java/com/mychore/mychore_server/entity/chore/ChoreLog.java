package com.mychore.mychore_server.entity.chore;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.mychore.mychore_server.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import java.time.LocalDate;

@Builder
@Entity
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@DynamicInsert
@DynamicUpdate
@Getter
public class ChoreLog extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "chore_log_id")
    private Long id;

    @NonNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "chore_id")
    @JsonIgnore
    private Chore chore;

    @NonNull
    private Boolean isComplete;

    @NonNull
    private LocalDate setDate;

    public void updateIsComplete(Boolean bool) {
        this.isComplete = bool;
    }
}
