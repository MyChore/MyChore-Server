package com.mychore.mychore_server.entity.chore;

import com.mychore.mychore_server.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Builder
@Entity
@AllArgsConstructor
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
    @Column(columnDefinition = "BOOLEAN DEFAULT true", name = "is_complete")
    private Boolean isComplete;

    @NonNull
    @Column(name = "set_date")
    private LocalDate setDate;

    public void updateIsComplete(Boolean bool) {
        this.isComplete = bool;
    }
}
