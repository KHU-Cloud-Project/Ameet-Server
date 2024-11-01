package CloudProject.A_meet.domain.group.domain.group.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Role {
    OWNER("OWNER"),
    MEMBER("MEMBER");

    private final String value;
}
