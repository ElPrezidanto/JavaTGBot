package backend.academy.bot;

import static org.assertj.core.api.Assertions.assertThat;

import backend.academy.bot.repository.api.scrapper.ScrapperApi;
import backend.academy.dto.request.AddLinkRequest;
import backend.academy.dto.request.RemoveLinkRequest;
import backend.academy.dto.response.ApiErrorResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class ScrapperApiTest {
    private static final Logger log = LogManager.getLogger(ScrapperApiTest.class);

    @Autowired
    private ScrapperApi scrapperApi;

    //    @Test
    void addLink_ApiError() {
        var a = scrapperApi.addLink(1L, new AddLinkRequest("", null, null));

        log.info(a);
        assertThat(a).isInstanceOf(ApiErrorResponse.class);
    }

    //    @Test
    void getLinks_ApiError() {
        assertThat(scrapperApi.getLinks(-2L)).isInstanceOf(ApiErrorResponse.class);
    }

    //    @Test
    void deleteLink_ApiError() {
        assertThat(scrapperApi.deleteLink(1L, new RemoveLinkRequest(null))).isInstanceOf(ApiErrorResponse.class);
    }

    //    @Test
    void registerChat_apiError() {
        assertThat(scrapperApi.registerChat(null)).isInstanceOf(ApiErrorResponse.class);
    }

    //    @Test
    void deleteChat_apiError() {
        assertThat(scrapperApi.deleteChat(null)).isInstanceOf(ApiErrorResponse.class);
    }
}
