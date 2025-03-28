package backend.academy.bot.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UserState {
    private State state = State.NONE;
    private String url;
    private String[] tags;
    private String[] filters;

    public void setEvent(Event event) {
        switch (event) {
            case START_TRACK -> state = State.WAITING_LINK;
            case ENTER_LINK -> state = State.WAITING_FILTERS;
            case ENTER_FILTER -> state = State.WAITING_TAGS;
            case START_UNTRACKING_LINK -> state = State.WAITING_UNTRACK_LINK;
            case ENTER_TAG, CLOSE, UNTRACK_LINK -> {
                state = State.NONE;
                url = null;
                tags = null;
                filters = null;
            }
        }
    }

    public enum State {
        NONE,
        WAITING_LINK,
        WAITING_TAGS,
        WAITING_FILTERS,
        WAITING_UNTRACK_LINK
    }

    public enum Event {
        START_TRACK,
        ENTER_LINK,
        ENTER_TAG,
        ENTER_FILTER,
        START_UNTRACKING_LINK,
        UNTRACK_LINK,
        CLOSE
    }
}
