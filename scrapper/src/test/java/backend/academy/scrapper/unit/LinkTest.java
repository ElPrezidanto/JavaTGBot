package backend.academy.scrapper.unit;

import static org.assertj.core.api.Assertions.assertThat;

import backend.academy.scrapper.data.impl.LinkRepositoryImpl;
import backend.academy.scrapper.domain.model.LinkWithSubscribers;
import backend.academy.scrapper.domain.repository.LinkRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(classes = {LinkRepositoryImpl.class})
public class LinkTest {
    private static final Logger log = LogManager.getLogger(LinkTest.class);

    @Autowired
    private LinkRepository linkRepository;

    private static String URL =
            "https://github.com/central-university-dev/backend-academy-2025-spring/blob/main/Java/seminar3/src/test/java/ru/tbank/sem3/unit/CheckEvenControllerTest.java";

    @BeforeEach
    void setUp() {
        linkRepository.clear();
    }

    @Test
    void addLink_Github() {
        var link = linkRepository.addSubscriberToLink(URL);

        assertThat(link).isEqualTo(new LinkWithSubscribers(URL, 1));
    }

    @Test
    void addLink_2Github() {
        linkRepository.addSubscriberToLink(URL);
        var link = linkRepository.addSubscriberToLink(URL);

        assertThat(link).isEqualTo(new LinkWithSubscribers(URL, 2));
    }

    @Test
    void deleteLink() {
        linkRepository.addSubscriberToLink(URL);
        linkRepository.addSubscriberToLink(URL);

        var link = linkRepository.removeSubscriberFromLink(URL);
        assertThat(link.get()).isEqualTo(new LinkWithSubscribers(URL, 1));
    }

    @Test
    void deleteLink_IsNull() {
        var link = linkRepository.removeSubscriberFromLink(URL);
        assertThat(link.isEmpty()).isTrue();
    }

    @Test
    void deleteLink_IsEmpty() {
        linkRepository.addSubscriberToLink(URL);

        var link = linkRepository.removeSubscriberFromLink(URL);
        log.info(link);
        assertThat(linkRepository.allTrackingLinks().isEmpty()).isTrue();
    }
}
