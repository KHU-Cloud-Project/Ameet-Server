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
public class bot {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="bot_id", updatable = false)
    private Long botId;

    @ManyToOne
    @JoinColumn(name="meeting_id")
    private Meeting meetingId;

    @ManyToOne
    @JoinColumn(name="minute_id")
    private Minute minuteId;

    @Column(name="type")
    private String type;

    @Column(name="content")
    private String content;

    @CreatedDate
    @Column(name="created_at")
    private LocalDateTime createdAt;
}
