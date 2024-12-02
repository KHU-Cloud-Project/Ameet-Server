package CloudProject.A_meet.domain.group.domain.note.domain;

import CloudProject.A_meet.domain.group.domain.meeting.domain.Meeting;
import CloudProject.A_meet.global.common.error.exception.CustomException;
import CloudProject.A_meet.global.common.error.exception.ErrorCode;
import CloudProject.A_meet.global.common.model.BaseTimeEntity;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.attoparser.dom.Text;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Table(name="note")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
@ToString
@Builder
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

    @Column(nullable = false, columnDefinition = "TEXT")
    private String summary;

    @Column(columnDefinition = "TEXT")
    private String script;

    @Column(length = 1000)
    private String presignedUrl;

    private String members;

    public void updatemembers(String members) {
        this.members = members;
    }

    public void updatePresignedUrl(String presignedUrl) {
        this.presignedUrl = presignedUrl;
    }
    public void updateCreatedAt(String createdAt) {
        if (createdAt == null || createdAt.isBlank()) {
            throw new IllegalArgumentException("Invalid dateTime: dateTime cannot be null or empty");
        }
        try {

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            LocalDateTime parsedDate = LocalDateTime.parse(createdAt, formatter);
            this.createdAt = parsedDate;
        } catch (DateTimeParseException e) {
            throw new CustomException(ErrorCode.INVALID_DATE_FORMAT);
        }
    }

    public void updateSummary(String summary) {
        this.summary = summary;
    }

    public void updateScript(String script) {
        this.script = script;
    }
}
