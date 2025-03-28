package backend.academy.scrapper.unit;

import static org.assertj.core.api.Assertions.assertThat;

import backend.academy.scrapper.domain.model.serviceLink.GithubLink;
import backend.academy.scrapper.domain.model.serviceLink.StackOverflowLink;
import backend.academy.scrapper.util.Parser;
import org.junit.jupiter.api.Test;

public class ParserTest {
    @Test
    public void testParser_GithubLink() {
        assertThat(Parser.parseUrl("https://github.com/ElPrezidanto/FinanceTrackerApp"))
                .isEqualTo(new GithubLink(
                        "https://github.com/ElPrezidanto/FinanceTrackerApp", "ElPrezidanto", "FinanceTrackerApp"));

        assertThat(
                        Parser.parseUrl(
                                "https://github.com/central-university-dev/backend-academy-2025-spring/blob/main/Java/seminar3/src/test/java/ru/tbank/sem3/unit/CheckEvenControllerTest.java"))
                .isEqualTo(new GithubLink(
                        "https://github.com/central-university-dev/backend-academy-2025-spring/blob/main/Java/seminar3/src/test/java/ru/tbank/sem3/unit/CheckEvenControllerTest.java",
                        "central-university-dev",
                        "backend-academy-2025-spring"));
    }

    @Test
    public void testParser_SOLink() {
        assertThat(
                        Parser.parseUrl(
                                "https://stackoverflow.com/questions/78785105/stateflow-value-changes-but-subscribers-are-not-notified"))
                .isEqualTo(new StackOverflowLink(
                        "https://stackoverflow.com/questions/78785105/stateflow-value-changes-but-subscribers-are-not-notified",
                        78785105L));
    }

    @Test
    public void testParser_Null() {
        assertThat(Parser.parseUrl("https://vk.com/el_prezidanto/TestIsNull")).isNull();
    }
}
