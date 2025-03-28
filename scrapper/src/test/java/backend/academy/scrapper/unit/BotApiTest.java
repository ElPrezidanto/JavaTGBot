package backend.academy.scrapper.unit;

import static org.assertj.core.api.Assertions.assertThat;

import backend.academy.dto.request.LinkUpdateRequest;
import backend.academy.dto.response.ApiErrorResponse;
import backend.academy.scrapper.data.source.remote.botapi.BotApiModule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class BotApiTest {

    @Autowired
    private BotApiModule botApiModule;

    //    @Test
    void sendUpdates_receiveApiError() {
        var a = botApiModule.sendUpdates(new LinkUpdateRequest(null, null, null, null));

        assertThat(a).isInstanceOf(ApiErrorResponse.class);
    }
}
