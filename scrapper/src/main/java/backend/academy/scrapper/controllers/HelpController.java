package backend.academy.scrapper.controllers;

import backend.academy.scrapper.domain.model.LinkWithSubscribers;
import backend.academy.scrapper.domain.repository.LinkRepository;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class HelpController {
    private final LinkRepository linkRepository;

    @GetMapping("/all")
    public Set<LinkWithSubscribers> getAllLinks() {
        return linkRepository.allTrackingLinksWithSubscribers();
    }
}
