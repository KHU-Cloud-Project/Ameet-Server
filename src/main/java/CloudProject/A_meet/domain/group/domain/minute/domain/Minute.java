package CloudProject.A_meet.domain.group.domain.minute.domain;

import CloudProject.A_meet.domain.group.domain.meeting.domain.Meeting;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.annotation.CreatedDate;
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
    @Column(nullable = false)
    private Long minuteId;

    @ManyToOne
    @JoinColumn(name="meeting_id")
    private Meeting meetingId;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String content;

    private String fileUrl;

    @CreatedDate
    @Column(nullable = false)
    private String createdAt;
}
