package backend.academy.scrapper.services;

import backend.academy.dto.request.LinkUpdateRequest;
import backend.academy.scrapper.data.source.remote.botapi.BotApiModule;
import backend.academy.scrapper.data.source.remote.github.GithubHelper;
import backend.academy.scrapper.data.source.remote.stackoverflow.StackOverflowHelper;
import backend.academy.scrapper.domain.model.serviceLink.GithubLink;
import backend.academy.scrapper.domain.model.serviceLink.StackOverflowLink;
import backend.academy.scrapper.domain.repository.LinkRepository;
import backend.academy.scrapper.domain.repository.LinksWithChatsRepository;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LinkObserverService {
    private static final Logger log = LogManager.getLogger(LinkObserverService.class);
    private final LinkRepository linkRepository;
    private final LinksWithChatsRepository linksWithChatsRepository;

    private final BotApiModule botApiModule;
    private final StackOverflowHelper stackOverflowHelper;
    private final GithubHelper githubHelper;

    @Scheduled(fixedRate = 5000)
    @SuppressWarnings({"UnusedPrivateMethod", "UnusedMethod", "PMD.UnusedPrivateMethod"})
    @SuppressFBWarnings({"UnusedPrivateMethod", "UnusedMethod"})
    private void observeLinks() {
        log.info("ObserveLinks: {}", linkRepository.allTrackingLinks());
        linkRepository.allTrackingLinks().forEach(link -> {
            if (link instanceof StackOverflowLink stackOverflowLink) {
                stackOverflowHelper.observeLink(stackOverflowLink);
            } else if (link instanceof GithubLink githubLink) {
                githubHelper.observeLink(githubLink);
            }
        });

        checkUpdates().forEach((url, messages) -> messages.forEach(message -> sendUpdateToUsers(url, message)));
    }

    private Map<String, List<String>> checkUpdates() {
        var soUpdates = stackOverflowHelper.checkUpdates();
        var ghUpdates = githubHelper.checkUpdates();
        var res = new HashMap<String, List<String>>();

        soUpdates.forEach((link, messages) ->
                res.computeIfAbsent(link.getLink(), k -> new ArrayList<>()).addAll(messages));

        ghUpdates.forEach((link, messages) ->
                res.computeIfAbsent(link.getLink(), k -> new ArrayList<>()).addAll(messages));

        return res;
    }

    private void sendUpdateToUsers(String url, String description) {
        List<Long> chats = linksWithChatsRepository.chatsThatTracingLink(url);
        botApiModule.sendUpdates(new LinkUpdateRequest(0L, url, description, chats));
    }
}
