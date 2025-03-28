package backend.academy.scrapper.domain.model;

import backend.academy.dto.response.LinkResponse;
import java.util.List;

public record LinkWithTagsAndFilters(String url, List<String> tags, List<String> filters) {
    public static LinkWithTagsAndFilters of(String url) {
        return new LinkWithTagsAndFilters(url, List.of(), List.of());
    }

    public LinkResponse toLinkResponse(Long chatId) {
        return new LinkResponse(chatId, url, tags, filters);
    }

    @Override
    public int hashCode() {
        return url.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) return false;
        if (obj instanceof LinkWithTagsAndFilters link) {
            return url.equals(link.url());
        }
        return false;
    }
}
