package backend.academy.bot.controllers;

import backend.academy.bot.service.BotService;
import backend.academy.dto.request.LinkUpdateRequest;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/updates")
public class BotUpdateController {

    private final BotService botHelper;

    public BotUpdateController(BotService botHelper) {
        this.botHelper = botHelper;
    }

    @PostMapping
    public ResponseEntity<String> update(@RequestBody @Valid LinkUpdateRequest linkUpdate) {
        linkUpdate.tgChatIds().forEach(chatId -> botHelper.sendMessageTG(chatId, linkUpdate.description()));

        return ResponseEntity.ok("Обновление обработано");
    }
}
