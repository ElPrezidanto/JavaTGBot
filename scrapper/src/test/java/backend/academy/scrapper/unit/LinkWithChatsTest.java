package backend.academy.scrapper.unit;

import static org.assertj.core.api.Assertions.assertThat;

import backend.academy.scrapper.data.impl.LinkRepositoryImpl;
import backend.academy.scrapper.data.impl.LinksWithChatsRepositoryImpl;
import backend.academy.scrapper.domain.repository.LinksWithChatsRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(classes = {LinkRepositoryImpl.class, LinksWithChatsRepositoryImpl.class})
public class LinkWithChatsTest {
    private static String URL =
            "https://github.com/central-university-dev/backend-academy-2025-spring/blob/main/Java/seminar3/src/test/java/ru/tbank/sem3/unit/CheckEvenControllerTest.java";

    @Autowired
    private LinksWithChatsRepository linksWithChatsRepository;

    @BeforeEach
    void setUp() {
        linksWithChatsRepository.clear();
    }

    @Test
    void addLink_NotUnique() {
        linksWithChatsRepository.trackLinkByChat(1L, URL, null, null);
        linksWithChatsRepository.trackLinkByChat(1L, URL, null, null);

        var a = linksWithChatsRepository.trackingLinksByChat(1L);
        assertThat(a.get().size()).isEqualTo(1);
    }
}
