package CloudProject.A_meet.domain.group.domain.meeting.domain;

import CloudProject.A_meet.domain.group.domain.group.domain.UserTeam;
import CloudProject.A_meet.domain.group.domain.user.domain.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Table(name="user_meetings")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@ToString
@Entity
@EntityListeners(AuditingEntityListener.class)
public class UserMeeting {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Long userMeetingId;

    @ManyToOne
    @JoinColumn(name="userId")
    private User userId;

    @ManyToOne
    @JoinColumn(name="meeting_id")
    private Meeting meetingId;

    @ManyToOne
    @JoinColumn(name="user_team_id")
    private UserTeam userTeamId;

    @CreatedDate
    private LocalDateTime entryTime;

    private LocalDateTime exitTime;

}
