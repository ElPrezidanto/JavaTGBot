package backend.academy.scrapper.data.source.remote.github.responses;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.Instant;

public record IssueModel(
        String url,
        String title,
        @JsonProperty("html_url") String html_url,
        @JsonProperty("comments_url") String urlComments,
        Integer number,
        UserModel user,
        @JsonProperty("updated_at") String timeStringUpdatedAt) {
    public Instant time() {
        return Instant.parse(timeStringUpdatedAt);
    }

    public String toNotification(String repo) {
        return String.format(
                "В репозитории %s появился новый вопрос от пользователя %s : %s%n%s",
                repo, user.login(), title, html_url);
    }
}
