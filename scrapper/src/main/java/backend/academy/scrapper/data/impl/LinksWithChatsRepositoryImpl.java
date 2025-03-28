package backend.academy.scrapper.data.impl;

import backend.academy.exceptions.NotFound;
import backend.academy.scrapper.domain.model.LinkWithTagsAndFilters;
import backend.academy.scrapper.domain.repository.LinkRepository;
import backend.academy.scrapper.domain.repository.LinksWithChatsRepository;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class LinksWithChatsRepositoryImpl implements LinksWithChatsRepository {
    private final Map<Long, Set<LinkWithTagsAndFilters>> linksWithChats = new ConcurrentHashMap<>();
    private final LinkRepository linkRepository;

    @Override
    public Optional<LinkWithTagsAndFilters> trackLinkByChat(
            Long chatId, String url, List<String> filters, List<String> tags) {
        var res = new LinkWithTagsAndFilters(url, tags, filters);

        var links = linksWithChats.computeIfAbsent(chatId, k -> new HashSet<>());

        if (!links.contains(res)) {
            var link = linkRepository.addSubscriberToLink(url);
            if (link == null) return Optional.empty();
        }

        links.add(res);
        linksWithChats.put(chatId, links);
        return Optional.of(res);
    }

    @Override
    public LinkWithTagsAndFilters untrackLinkByChat(Long chatId, String url) {
        var chat = linksWithChats.get(chatId);

        var link = LinkWithTagsAndFilters.of(url);

        if (chat == null || !chat.contains(link)) {
            throw new NotFound("", String.format("Ссылка %s не найдена", link.url()));
        }

        linkRepository.removeSubscriberFromLink(url);
        chat.remove(link);

        return link;
    }

    @Override
    public Optional<Set<LinkWithTagsAndFilters>> trackingLinksByChat(Long chatId) {
        return Optional.ofNullable(linksWithChats.get(chatId));
    }

    @Override
    public void untrackAllLinksByChat(Long chatId) {
        var a = linksWithChats.get(chatId);
        if (a == null) return;

        a.forEach(link -> untrackLinkByChat(chatId, link.url()));
    }

    @Override
    public List<Long> chatsThatTracingLink(String url) {
        List<Long> res = new ArrayList<>();
        linksWithChats.forEach((chatId, links) -> {
            if (links.contains(LinkWithTagsAndFilters.of(url))) {
                res.add(chatId);
            }
        });

        return res;
    }

    @Override
    public void clear() {
        linksWithChats.clear();
        linkRepository.clear();
    }
}
