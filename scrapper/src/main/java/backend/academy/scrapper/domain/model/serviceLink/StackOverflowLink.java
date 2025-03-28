package backend.academy.scrapper.domain.model.serviceLink;

import backend.academy.scrapper.domain.model.CustomLink;
import java.util.Objects;

public record StackOverflowLink(String url, Long questionId) implements CustomLink {
    @Override
    public int hashCode() {
        return Objects.hashCode(questionId);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) return false;

        if (obj instanceof StackOverflowLink link) {
            return questionId.equals(link.questionId());
        }
        return false;
    }

    @Override
    public String getLink() {
        return url;
    }
}
