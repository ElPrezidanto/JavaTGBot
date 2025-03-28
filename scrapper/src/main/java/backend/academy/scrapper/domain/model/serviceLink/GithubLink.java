package backend.academy.scrapper.domain.model.serviceLink;

import backend.academy.scrapper.domain.model.CustomLink;
import java.util.Objects;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public record GithubLink(String url, String owner, String repo) implements CustomLink {
    @Override
    public int hashCode() {
        return Objects.hash(owner, repo);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) return false;

        if (obj instanceof GithubLink link) {
            return owner.equals(link.owner()) && repo.equals(link.repo());
        }
        return false;
    }

    @Override
    public String getLink() {
        return url;
    }
}
