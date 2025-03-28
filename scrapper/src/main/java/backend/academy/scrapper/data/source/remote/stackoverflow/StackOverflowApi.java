package backend.academy.scrapper.data.source.remote.stackoverflow;

import backend.academy.scrapper.config.ScrapperConfig;
import backend.academy.scrapper.data.source.remote.stackoverflow.resoponseV2.StackOverflowAnswersResponse;
import backend.academy.scrapper.data.source.remote.stackoverflow.resoponseV2.StackOverflowCommentsResponse;
import backend.academy.scrapper.data.source.remote.stackoverflow.resoponseV2.StackOverflowResponse;
import java.net.URI;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.Nullable;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.RestClient;
import org.springframework.web.util.UriComponentsBuilder;

@Repository
public class StackOverflowApi {
    private static final String URL = "https://api.stackexchange.com/2.3/";
    private static final String QUESTIONS = URL + "questions/";
    private static final String ANSWERS = URL + "answers/";
    private static final String FILTER = "!D4oQDCu)44JLVkahQjYs-q0*XDCrBu((CLXwz8gHtEvl7VQolOD";
    private static final Logger log = LogManager.getLogger(StackOverflowApi.class);

    private final String SO_KEY;

    private final RestClient restClient;

    public StackOverflowApi(ScrapperConfig config, @Qualifier("soRestClient") RestClient restClient) {
        SO_KEY = config.stackOverflow().key();
        this.restClient = restClient;
    }

    public StackOverflowAnswersResponse getAnswers(Long questionId, Long from) {
        URI uri = uriBuilder("{url}{id}/answers", QUESTIONS, questionId, from);

        return restClient
                .get()
                .uri(uri)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, (request, response) -> {
                    log.error(request.getURI());
                    log.error(request.getAttributes());
                    log.error(request.getMethod());
                    throw new MyCustomRuntimeException(response.getStatusCode(), response.getHeaders());
                })
                .body(StackOverflowAnswersResponse.class);
    }

    public StackOverflowCommentsResponse getCommentsOnAnswer(Long answerId, Long from) {
        return getStackOverflowCommentsResponse(answerId, from, ANSWERS);
    }

    public StackOverflowCommentsResponse getCommentsOnQuestion(Long questionId, Long from) {
        return getStackOverflowCommentsResponse(questionId, from, QUESTIONS);
    }

    @Nullable
    private StackOverflowCommentsResponse getStackOverflowCommentsResponse(Long id, Long from, String url) {
        URI uri = uriBuilder("{url}{id}/comments", url, id, from);

        return restClient
                .get()
                .uri(uri)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, (request, response) -> {
                    log.error(request.getURI());
                    log.error(request.getAttributes());
                    log.error(request.getMethod());
                    throw new MyCustomRuntimeException(response.getStatusCode(), response.getHeaders());
                })
                .body(StackOverflowCommentsResponse.class);
    }

    public StackOverflowResponse getQuestion(Long questionId, Long from) {
        URI uri = uriBuilder("{url}{id}", QUESTIONS, questionId, from);

        return restClient
                .get()
                .uri(uri)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, (request, response) -> {
                    log.error(request.getURI());
                    log.error(request.getAttributes());
                    log.error(request.getMethod());
                    throw new MyCustomRuntimeException(response.getStatusCode(), response.getHeaders());
                })
                .body(StackOverflowResponse.class);
    }

    private URI uriBuilder(String pattern, String url, Long id, Long from) {
        return UriComponentsBuilder.fromPath(pattern)
                .queryParam("order", "desc")
                .queryParam("sort", "creation")
                .queryParam("site", "stackoverflow")
                .queryParam("fromdate", from)
                .queryParam("filter", FILTER)
                .queryParam("key", SO_KEY)
                .buildAndExpand(url, id)
                .toUri();
    }
}
