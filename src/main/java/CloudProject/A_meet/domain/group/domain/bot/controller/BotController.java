package CloudProject.A_meet.domain.group.domain.bot.controller;

import CloudProject.A_meet.domain.group.domain.bot.dto.response.SummaryBotResponse;
import CloudProject.A_meet.domain.group.domain.bot.service.BotService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/bot")
@Tag(name = "Bot", description = "Bot API")
@Validated
public class BotController {
    private final BotService botService;

    @Operation(summary = "요약봇 호출", description = "회원 정보를 조회하는 API")
    @GetMapping("/summary")
    public SummaryBotResponse summarize(@RequestParam Long meetingId) {
        return botService.summaryBot(meetingId);
    }

}
