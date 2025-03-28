package backend.academy.scrapper.unit;

import backend.academy.scrapper.data.source.remote.github.GithubHelper;
import backend.academy.scrapper.domain.model.serviceLink.GithubLink;
import backend.academy.scrapper.util.Parser;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class GhHelperTest {
    @Autowired
    private GithubHelper githubHelper;

    @Test
    public void test() {
        githubHelper.observeLink((GithubLink) Parser.parseUrl("https://github.com/ElPrezidanto/FinanceTrackerApp"));
    }
}
