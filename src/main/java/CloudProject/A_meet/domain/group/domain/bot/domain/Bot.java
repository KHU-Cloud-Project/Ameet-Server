package CloudProject.A_meet.domain.group.domain.bot.domain;

import CloudProject.A_meet.domain.group.domain.meeting.domain.Meeting;
import CloudProject.A_meet.domain.group.domain.note.domain.Note;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Table(name="bots")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
@ToString
@Builder
@Entity
@EntityListeners(AuditingEntityListener.class)
public class Bot {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Long botId;

    @ManyToOne
    @JoinColumn(name="meeting_id")
    private Meeting meetingId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private BotType type;

    @Column(length = 1000)
    private String content;

    @CreatedDate
    @Column(nullable = false)
    private LocalDateTime createdAt;

    public void updateContent(String content) {
        this.content = content;
    }
}
