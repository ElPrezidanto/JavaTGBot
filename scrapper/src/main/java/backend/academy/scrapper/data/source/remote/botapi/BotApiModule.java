package backend.academy.scrapper.data.source.remote.botapi;

import backend.academy.dto.request.LinkUpdateRequest;
import backend.academy.dto.response.ApiErrorResponse;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
public class BotApiModule {
    private final RestClient restClient;

    public BotApiModule(@Qualifier("botRestClient") RestClient restClient) {
        this.restClient = restClient;
    }

    public Object sendUpdates(LinkUpdateRequest linkUpdate) {
        return restClient.post().uri("updates").body(linkUpdate).exchange((request, response) -> {
            if (response.getStatusCode().is2xxSuccessful()) {
                return response.bodyTo(String.class);
            } else {
                return response.bodyTo(ApiErrorResponse.class);
            }
        });
    }
}
