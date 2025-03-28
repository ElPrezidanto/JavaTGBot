package backend.academy.scrapper.data.source.remote.github.responses;

import com.fasterxml.jackson.annotation.JsonProperty;

public record CommitModel(
        String sha,
        Commit commit,
        UserModel author,
        @JsonProperty("html_url") String url,
        @JsonProperty("comments_url") String commentsUrl) {
    @Override
    public String toString() {
        return String.format(
                "Пользователь %s оставил коммит с сообщением: %s%n%s", author.login(), commit.message(), url);
    }

    @Override
    public int hashCode() {
        return sha.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) return false;
        if (obj instanceof CommitModel commitModel) {
            return sha.equals(commitModel.sha());
        }
        return false;
    }

    private record Commit(String message) {}
}
