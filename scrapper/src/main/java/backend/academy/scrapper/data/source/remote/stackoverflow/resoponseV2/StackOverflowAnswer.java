package backend.academy.scrapper.data.source.remote.stackoverflow.resoponseV2;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

public record StackOverflowAnswer(
        List<String> tags,
        StackOverflowOwner owner,
        String link,
        @JsonProperty("answer_id") Long answerId,
        String body) {
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof StackOverflowAnswer answer) {
            return answerId.equals(answer.answerId);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return answerId.hashCode();
    }

    public String toNotification() {
        return "Пользователь " + owner.displayName() + " добавил ответ: " + body + "\nна вопрос: " + link;
    }
}
