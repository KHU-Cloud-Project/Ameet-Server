package CloudProject.A_meet.domain.group.domain.userTeam.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Role {
    OWNER("OWNER"),
    MEMBER("MEMBER");

    private final String value;
}
