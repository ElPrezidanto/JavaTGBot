package backend.academy.scrapper.data.impl;

import backend.academy.scrapper.domain.model.CustomLink;
import backend.academy.scrapper.domain.model.LinkWithSubscribers;
import backend.academy.scrapper.domain.repository.LinkRepository;
import backend.academy.scrapper.util.Parser;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Repository;

@Repository
public class LinkRepositoryImpl implements LinkRepository {
    private static final Logger log = LogManager.getLogger(LinkRepositoryImpl.class);
    private final Map<CustomLink, Integer> links = new ConcurrentHashMap<>();

    @Override
    public LinkWithSubscribers addSubscriberToLink(String link) {
        var parsedLink = Parser.parseUrl(link);

        if (parsedLink == null) {
            return null;
        }

        var subscribers = links.computeIfAbsent(parsedLink, k -> 0) + 1;
        links.put(parsedLink, subscribers);
        return new LinkWithSubscribers(link, subscribers);
    }

    @Override
    public Optional<LinkWithSubscribers> removeSubscriberFromLink(String link) {
        var customLink = Parser.parseUrl(link);
        var a = links.get(customLink);
        log.info("subscribers: {}", a);
        if (a == null) {
            return Optional.empty();
        }
        if (a == 1) {
            log.info("{} has been removed", customLink);
            log.info("links: {}", links);
            links.remove(customLink);
        } else {
            links.put(customLink, a - 1);
        }
        return Optional.of(new LinkWithSubscribers(link, a - 1));
    }

    @Override
    public Set<LinkWithSubscribers> allTrackingLinksWithSubscribers() {
        var res = new HashSet<LinkWithSubscribers>();

        links.forEach(
                (customLink, subscribers) -> res.add(new LinkWithSubscribers(customLink.toString(), subscribers)));

        return res;
    }

    @Override
    public Set<CustomLink> allTrackingLinks() {
        return links.keySet();
    }

    @Override
    public void clear() {
        links.clear();
    }
}
