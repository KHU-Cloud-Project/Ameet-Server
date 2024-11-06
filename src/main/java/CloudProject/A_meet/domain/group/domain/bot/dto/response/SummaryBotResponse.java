package CloudProject.A_meet.domain.group.domain.bot.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class SummaryBotResponse {
    private Long meetingId;
    private Long botId;
    private String summary;
}