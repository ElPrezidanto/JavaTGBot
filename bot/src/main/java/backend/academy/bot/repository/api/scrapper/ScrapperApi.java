package backend.academy.bot.repository.api.scrapper;

import backend.academy.dto.request.AddLinkRequest;
import backend.academy.dto.request.RemoveLinkRequest;
import backend.academy.dto.response.ApiErrorResponse;
import backend.academy.dto.response.LinkResponse;
import backend.academy.dto.response.ListLinksResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
@RequiredArgsConstructor
public class ScrapperApi {
    private final RestClient restClient;

    public Object addLink(Long chatId, AddLinkRequest link) {
        return restClient
                .post()
                .uri("links")
                .header("Tg-chat-id", chatId.toString())
                .body(link)
                .exchange((request, response) -> {
                    if (response.getStatusCode().is2xxSuccessful()) {
                        return response.bodyTo(LinkResponse.class);
                    } else {
                        return response.bodyTo(ApiErrorResponse.class);
                    }
                });
    }

    public Object getLinks(Long chatId) {
        return restClient
                .get()
                .uri("links")
                .header("Tg-chat-id", chatId.toString())
                .exchange((request, response) -> {
                    if (response.getStatusCode().is2xxSuccessful()) {
                        return response.bodyTo(ListLinksResponse.class);
                    } else {
                        return response.bodyTo(ApiErrorResponse.class);
                    }
                });
    }

    public Object deleteLink(Long chatId, RemoveLinkRequest link) {
        return restClient
                .method(HttpMethod.DELETE)
                .uri("links")
                .header("Tg-chat-id", chatId.toString())
                .body(link)
                .exchange((request, response) -> {
                    if (response.getStatusCode().is2xxSuccessful()) {
                        return response.bodyTo(LinkResponse.class);
                    } else {
                        return response.bodyTo(ApiErrorResponse.class);
                    }
                });
    }

    public Object registerChat(Long chatId) {
        return restClient.post().uri(String.format("tg-chat/%d", chatId)).exchange((request, response) -> {
            if (response.getStatusCode().is2xxSuccessful()) {
                return response.bodyTo(String.class);
            } else {
                return response.bodyTo(ApiErrorResponse.class);
            }
        });
    }

    public Object deleteChat(Long chatId) {
        return restClient.delete().uri(String.format("tg-chat/%d", chatId)).exchange((request, response) -> {
            if (response.getStatusCode().is2xxSuccessful()) {
                return response.bodyTo(String.class);
            } else {
                return response.bodyTo(ApiErrorResponse.class);
            }
        });
    }
}
