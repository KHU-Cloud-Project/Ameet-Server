package CloudProject.A_meet.domain.group.domain.note.domain;

import CloudProject.A_meet.domain.group.domain.meeting.domain.Meeting;
import CloudProject.A_meet.global.common.model.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Table(name="note")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@ToString
@Entity
@EntityListeners(AuditingEntityListener.class)
public class Note extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Long noteId;

    @ManyToOne
    @JoinColumn(name="meeting_id")
    private Meeting meetingId;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String content;

    private String fileUrl;
}
