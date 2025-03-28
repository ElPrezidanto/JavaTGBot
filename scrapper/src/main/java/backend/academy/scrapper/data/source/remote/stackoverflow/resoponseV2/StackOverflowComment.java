package backend.academy.scrapper.data.source.remote.stackoverflow.resoponseV2;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Objects;

public record StackOverflowComment(
        StackOverflowOwner owner,
        String body,
        @JsonProperty("post_id") Long answerId,
        @JsonProperty("comment_id") Long commentId,
        String link) {
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof StackOverflowComment com) {
            return commentId.equals(com.commentId);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(commentId);
    }

    public String toNotification() {
        return String.format(
                "Пользователь %s оставил новый комментарий: %s%nна вопрос %s", owner.displayName(), body, link);
    }
}
