package CloudProject.A_meet.domain.group.domain.bot.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class BotResponse {
    private Long meetingId;
    private Long botId;
    private String text;
}