package CloudProject.A_meet.domain.group.domain.group.domain;

import CloudProject.A_meet.domain.group.domain.user.domain.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Table(name="user_group")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@ToString
@Entity
@EntityListeners(AuditingEntityListener.class)
public class UserGroup {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="user_group_id", updatable = false)
    private Long userGroupId;

    @ManyToOne
    @JoinColumn(name="userId")
    private User userId;

    @ManyToOne
    @JoinColumn(name="group_id")
    private Group groupId;

    @CreatedDate
    @Column(name="created_at")
    private LocalDateTime createdAt;

    @Column(name="deleted_at")
    private LocalDateTime deletedAt;
}
