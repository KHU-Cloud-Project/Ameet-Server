package CloudProject.A_meet.domain.group.domain.meeting.domain;

import CloudProject.A_meet.domain.group.domain.team.domain.Team;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

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

    @Setter
    private LocalDateTime endedAt;

    @Column(nullable = false)
    @Setter
    private String title;

    private Duration duration;

    @Setter
    @Column(length = 2048)
    private String presignedUrl;

    @ElementCollection(fetch = FetchType.EAGER)
    private List<String> participants;

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

    public boolean addParticipant(String nickname) {
        if (this.participants == null) {
            this.participants = new ArrayList<>();
        }
        if (!this.participants.contains(nickname)) {
            this.participants.add(nickname);
            return true;
        }
        return false;
    }

    public List<String> getParticipant() {
        return Objects.requireNonNullElse(this.participants, Collections.emptyList());
    }
}
