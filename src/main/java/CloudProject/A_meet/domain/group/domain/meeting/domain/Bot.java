package CloudProject.A_meet.domain.group.domain.meeting.domain;

import CloudProject.A_meet.domain.group.domain.minute.domain.Minute;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Table(name="bots")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@ToString
@Entity
@EntityListeners(AuditingEntityListener.class)
public class Bot {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Long botId;

    @ManyToOne
    @JoinColumn(name="meeting_id")
    @Column(nullable = false)
    private Meeting meetingId;

    @ManyToOne
    @JoinColumn(name="minute_id")
    private Minute minuteId;

    @Column(nullable = false)
    private String type;

    private String content;

    @CreatedDate
    @Column(nullable = false)
    private LocalDateTime createdAt;
}
