package CloudProject.A_meet.domain.group.domain.group.domain;

import CloudProject.A_meet.domain.group.domain.user.domain.User;
import CloudProject.A_meet.global.common.model.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Table(name="user_team")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@ToString
@Entity
@EntityListeners(AuditingEntityListener.class)
public class UserTeam extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Long userTeamId;

    @ManyToOne
    @JoinColumn(name="userId")
    private User userId;

    @ManyToOne
    @JoinColumn(name="team_id")
    private Team teamId;
}
