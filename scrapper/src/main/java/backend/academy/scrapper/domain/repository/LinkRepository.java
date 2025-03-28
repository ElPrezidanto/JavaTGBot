package backend.academy.scrapper.domain.repository;

import backend.academy.scrapper.domain.model.CustomLink;
import backend.academy.scrapper.domain.model.LinkWithSubscribers;
import java.util.Optional;
import java.util.Set;

public interface LinkRepository {

    LinkWithSubscribers addSubscriberToLink(String link);

    Optional<LinkWithSubscribers> removeSubscriberFromLink(String link);

    Set<LinkWithSubscribers> allTrackingLinksWithSubscribers();

    Set<CustomLink> allTrackingLinks();

    void clear();
}
