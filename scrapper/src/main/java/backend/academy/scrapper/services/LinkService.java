package backend.academy.scrapper.services;

import backend.academy.dto.response.LinkResponse;
import backend.academy.dto.response.ListLinksResponse;
import backend.academy.scrapper.domain.model.LinkWithSubscribers;
import backend.academy.scrapper.domain.repository.LinkRepository;
import backend.academy.scrapper.domain.repository.LinksWithChatsRepository;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import org.springframework.stereotype.Service;

@Service
public class LinkService {
    private final LinkRepository linkRepository;
    private final LinksWithChatsRepository linksWithChatsRepository;

    public LinkService(LinkRepository linkRepository, LinksWithChatsRepository linksWithChatsRepository) {
        this.linkRepository = linkRepository;
        this.linksWithChatsRepository = linksWithChatsRepository;
    }

    public LinkResponse trackLinkByChat(Long chatId, String url, List<String> tags, List<String> filters) {
        return linksWithChatsRepository
                .trackLinkByChat(chatId, url, filters, tags)
                .map(linkWithTagsAndFilters -> linkWithTagsAndFilters.toLinkResponse(chatId))
                .orElse(null);
    }

    public LinkResponse untrackLinkByChat(Long chatId, String url) {
        return linksWithChatsRepository.untrackLinkByChat(chatId, url).toLinkResponse(chatId);
    }

    public ListLinksResponse trackingLinksByChat(Long chatId) {
        return linksWithChatsRepository
                .trackingLinksByChat(chatId)
                .map(links -> {
                    List<LinkResponse> list = links.stream()
                            .map(link -> link.toLinkResponse(chatId))
                            .toList();
                    return new ListLinksResponse(list, list.size());
                })
                .orElseGet(() -> new ListLinksResponse(Collections.emptyList(), 0));
    }

    public Set<LinkWithSubscribers> getAllTrackingLinksWithSubscribers() {
        return linkRepository.allTrackingLinksWithSubscribers();
    }
}
