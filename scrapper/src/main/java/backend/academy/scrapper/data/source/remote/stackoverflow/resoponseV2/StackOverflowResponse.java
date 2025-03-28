package backend.academy.scrapper.data.source.remote.stackoverflow.resoponseV2;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

public record StackOverflowResponse(List<StackOverflowQuestion> items, @JsonProperty("has_more") boolean hasMore) {}
