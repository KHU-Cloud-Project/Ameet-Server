package CloudProject.A_meet.domain.group.domain.meeting.domain;

import CloudProject.A_meet.domain.group.domain.team.domain.Team;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.Duration;
import java.time.LocalDateTime;

@Table(name="meetings")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
@ToString
@Builder
@Entity
@EntityListeners(AuditingEntityListener.class)
public class Meeting {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Long meetingId;

    @ManyToOne
    @JoinColumn(name="team_id")
    private Team teamId;

    @Column(nullable = false)
    private LocalDateTime startedAt;

    private LocalDateTime endedAt;

    @Column(nullable = false)
    @Setter
    private String title;

    private Duration duration;

    @Column(length = 2048)
    private String presignedUrl;

    public void setPresignedUrl(String presignedUrl) {
        this.presignedUrl = presignedUrl;
    }

    public void setDuration() {
        if (startedAt != null && endedAt != null) {
            if (endedAt.isBefore(startedAt)) {
                System.out.println("Warning: endedAt is before startedAt.");
            }
            this.duration = Duration.between(startedAt, endedAt);
        } else {
            this.duration = Duration.ZERO;
        }
    }
}
