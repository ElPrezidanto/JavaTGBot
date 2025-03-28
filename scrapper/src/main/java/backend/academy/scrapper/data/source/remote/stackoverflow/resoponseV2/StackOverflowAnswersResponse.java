package backend.academy.scrapper.data.source.remote.stackoverflow.resoponseV2;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

public record StackOverflowAnswersResponse(
        List<StackOverflowAnswer> items, @JsonProperty("has_more") boolean hasMore) {}
