package backend.academy.scrapper.domain.repository;

import backend.academy.scrapper.domain.model.LinkWithTagsAndFilters;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface LinksWithChatsRepository {
    Optional<LinkWithTagsAndFilters> trackLinkByChat(Long chatId, String url, List<String> filters, List<String> tags);

    LinkWithTagsAndFilters untrackLinkByChat(Long chatId, String url);

    Optional<Set<LinkWithTagsAndFilters>> trackingLinksByChat(Long chatId);

    void untrackAllLinksByChat(Long chatId);

    List<Long> chatsThatTracingLink(String url);

    void clear();
}
