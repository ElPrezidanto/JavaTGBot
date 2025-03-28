package backend.academy.scrapper.controllers;

import backend.academy.scrapper.services.ChatService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/tg-chat", produces = MediaType.APPLICATION_JSON_VALUE)
public class ChatController {
    private final ChatService chatService;

    @PostMapping("/{id}")
    public ResponseEntity<String> registerChat(@PathVariable @Valid @Positive Long id) {
        chatService.registerChat(id);
        return ResponseEntity.ok("Чат " + id + " зарегистрирован");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteChat(@PathVariable @Valid @Positive Long id) {
        String text;
        if (chatService.removeChat(id)) {
            text = String.format("Чат %d удалён", id);
        } else {
            text = String.format("Ошибка при удалении чата %d", id);
        }
        return ResponseEntity.ok(text);
    }
}
