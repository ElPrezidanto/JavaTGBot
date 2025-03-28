package backend.academy.scrapper.data.source.remote.stackoverflow.resoponseV2;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

public record StackOverflowCommentsResponse(
        List<StackOverflowComment> items, @JsonProperty("has_more") boolean hasMore) {}
