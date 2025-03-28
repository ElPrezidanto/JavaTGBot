package backend.academy.scrapper.data.source.remote.stackoverflow.resoponseV2;

import com.fasterxml.jackson.annotation.JsonProperty;

public record StackOverflowOwner(@JsonProperty("display_name") String displayName, String link) {}
