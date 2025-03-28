package backend.academy.scrapper.controllers;

import backend.academy.dto.request.AddLinkRequest;
import backend.academy.dto.request.RemoveLinkRequest;
import backend.academy.dto.response.LinkResponse;
import backend.academy.dto.response.ListLinksResponse;
import backend.academy.scrapper.services.LinkService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/links")
@RequiredArgsConstructor
public class LinkController {
    private static final Logger log = LogManager.getLogger(LinkController.class);
    private final LinkService linkService;

    @PostMapping
    public ResponseEntity<LinkResponse> addLink(
            @Valid @Positive @RequestHeader("Tg-Chat-Id") Long chatId, @Valid @RequestBody AddLinkRequest request) {
        log.info("Adding link with url {}", request.link());
        LinkResponse res = linkService.trackLinkByChat(chatId, request.link(), request.tags(), request.filters());
        log.info("{}", res);
        return ResponseEntity.ok(res);
    }

    @GetMapping()
    public ResponseEntity<ListLinksResponse> getLinks(@Valid @Positive @RequestHeader("Tg-Chat-Id") Long chatId) {
        return ResponseEntity.ok(linkService.trackingLinksByChat(chatId));
    }

    @DeleteMapping()
    public ResponseEntity<LinkResponse> removeLink(
            @Valid @Positive @RequestHeader("Tg-Chat-Id") Long chatId, @Valid @RequestBody RemoveLinkRequest request) {
        log.info("delete link with url {}", request.link());
        return ResponseEntity.ok(linkService.untrackLinkByChat(chatId, request.link()));
    }
}
