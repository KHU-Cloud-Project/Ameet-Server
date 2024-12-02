package CloudProject.A_meet.domain.group.domain.bot.controller;

import CloudProject.A_meet.domain.group.domain.bot.dto.BotResponse;
import CloudProject.A_meet.domain.group.domain.bot.service.BotService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
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

    @Operation(summary = "정리봇 호출", description = "회원 정보를 조회하는 API")
    @GetMapping("/summary")
    public ResponseEntity<BotResponse> summarize(@RequestParam Long meetingId) {
        BotResponse botResponse = botService.summaryBot(meetingId);
        return ResponseEntity.status(200).body(botResponse);
    }

    @Operation(summary = "긍정 리액션봇 호출", description = "긍정 리액션봇을 조회하는 API")
    @GetMapping("/positive")
    public ResponseEntity<BotResponse> positive(@RequestParam Long meetingId) {
        BotResponse botResponse = botService.positiveBot(meetingId);
        return ResponseEntity.status(200).body(botResponse);
    }

    @Operation(summary = "부정 리액션봇 호출", description = "부정 리액션봇을 조회하는 API")
    @GetMapping("/negative")
    public ResponseEntity<BotResponse> negative(@RequestParam Long meetingId) {
        BotResponse botResponse = botService.negativeBot(meetingId);
        return ResponseEntity.status(200).body(botResponse);
    }

    @Operation(summary = "불참자 메일 보내기 봇 호출", description = "불참자 메일 보내기 봇을 조회하는 API")
    @GetMapping("/attendance")
    public ResponseEntity<Void> attendance(@RequestParam Long meetingId) {
        botService.attendanceBot(meetingId);
        return ResponseEntity.status(200).build();
    }
}
