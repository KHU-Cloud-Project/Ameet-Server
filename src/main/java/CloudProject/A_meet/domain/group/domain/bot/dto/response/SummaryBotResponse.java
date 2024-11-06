package CloudProject.A_meet.domain.group.domain.bot.dto.response;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class SummaryBotResponse {
    private Long meetingId;
    private Long botId;
    private String summary;
}