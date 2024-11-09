package CloudProject.A_meet.domain.group.domain.userTeam.domain;

import CloudProject.A_meet.domain.group.domain.team.domain.Team;
import CloudProject.A_meet.domain.group.domain.user.domain.User;
import CloudProject.A_meet.global.common.model.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Table(name="user_team")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
@Builder
@ToString
@Entity
@EntityListeners(AuditingEntityListener.class)
public class UserTeam extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Long userTeamId;

    @ManyToOne
    @JoinColumn(name="user_id")
    private User userId;

    @ManyToOne
    @JoinColumn(name="team_id")
    private Team teamId;

    @Enumerated(EnumType.STRING)
    private Role role;

    @Column(length = 500)
    private String introduction;

    @Column
    private boolean isMember;

    public void updateIntro(String introduction) {
        this.introduction = introduction;
    }

    public void updateIsMember(boolean isMember) {
        this.isMember = isMember;
    }

    public void updateRole(Role role) {
        this.role = role;
    }
}
