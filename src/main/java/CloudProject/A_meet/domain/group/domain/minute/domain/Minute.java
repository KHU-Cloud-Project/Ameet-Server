package CloudProject.A_meet.domain.group.domain.minute.domain;

import CloudProject.A_meet.domain.group.domain.meeting.domain.Meeting;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Table(name="minutes")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@ToString
@Entity
@EntityListeners(AuditingEntityListener.class)
public class Minute {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="minute_id", updatable = false)
    private Long minuteId;

    @ManyToOne
    @JoinColumn(name="meeting_id")
    private Meeting meetingId;

    @Column(name="title")
    private String title;

    @Column(name="content")
    private String content;

    @Column(name="file_url")
    private String fileUrl;

    @Column(name="created_at")
    private String createAt;
}
