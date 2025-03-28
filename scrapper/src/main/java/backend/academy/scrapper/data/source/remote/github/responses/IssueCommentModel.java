package backend.academy.scrapper.data.source.remote.github.responses;

import com.fasterxml.jackson.annotation.JsonProperty;

public record IssueCommentModel(
        @JsonProperty("html_url") String url,
        String body,
        UserModel user,
        @JsonProperty("created_at") String timeCreatedAt) {

    public String toNotification() {
        return String.format(
                "Пользователь %s оставил комментарий: %s" + "%n" + "на вопрос %s", user.login(), body, url);
    }
}
