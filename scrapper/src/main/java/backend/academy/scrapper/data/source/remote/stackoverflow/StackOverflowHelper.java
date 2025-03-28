package backend.academy.scrapper.data.source.remote.stackoverflow;

import backend.academy.scrapper.data.source.remote.stackoverflow.resoponseV2.StackOverflowAnswer;
import backend.academy.scrapper.data.source.remote.stackoverflow.resoponseV2.StackOverflowComment;
import backend.academy.scrapper.domain.model.serviceLink.StackOverflowLink;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.stereotype.Component;

@Component
public class StackOverflowHelper {
    private final StackOverflowApi stackOverflowApi;

    // question -> timeStamp
    private final Map<StackOverflowLink, Instant> questionsWithStamp = new ConcurrentHashMap<>();

    // question -> answers
    private final Map<StackOverflowLink, Set<StackOverflowAnswer>> answers = new HashMap<>();

    private final Set<StackOverflowLink> linksForObserving = ConcurrentHashMap.newKeySet();

    public StackOverflowHelper(StackOverflowApi stackOverflowApi) {
        this.stackOverflowApi = stackOverflowApi;
    }

    public void observeLink(StackOverflowLink link) {
        if (!questionsWithStamp.containsKey(link)) {
            checkAnswers(link, Instant.EPOCH);
            questionsWithStamp.put(link, Instant.now());
        }
        linksForObserving.add(link);
    }

    public Map<StackOverflowLink, List<String>> checkUpdates() {
        var res = new HashMap<StackOverflowLink, List<String>>();
        linksForObserving.forEach(link -> {
            Instant time;
            time = questionsWithStamp.getOrDefault(link, Instant.now());
            questionsWithStamp.put(link, Instant.now());
            var list = new ArrayList<String>();
            list.addAll(checkAnswers(link, time));
            list.addAll(checkCommentsOnQuestion(link, time));
            list.addAll(checkCommentsOnAnswer(link, time));
            res.put(link, list);
        });

        return res;
    }

    public List<String> checkAnswers(StackOverflowLink link, Instant time) {
        var answersResponse = stackOverflowApi.getAnswers(link.questionId(), time.getEpochSecond());

        if (answersResponse == null
                || answersResponse.items() == null
                || answersResponse.items().isEmpty()) {
            return new ArrayList<>();
        }

        answers.computeIfAbsent(link, k -> new HashSet<>()).addAll(answersResponse.items());

        return answersResponse.items().stream()
                .map(StackOverflowAnswer::toNotification)
                .toList();
    }

    public List<String> checkCommentsOnAnswer(StackOverflowLink link, Instant time) {
        var res = new ArrayList<String>();

        answers.get(link).parallelStream()
                .forEach(answer -> res.addAll(
                        stackOverflowApi
                                .getCommentsOnAnswer(answer.answerId(), time.getEpochSecond())
                                .items()
                                .parallelStream()
                                .map(StackOverflowComment::toNotification)
                                .toList()));
        return res;
    }

    public List<String> checkCommentsOnQuestion(StackOverflowLink link, Instant time) {
        return stackOverflowApi.getCommentsOnQuestion(link.questionId(), time.getEpochSecond()).items().parallelStream()
                .map(StackOverflowComment::toNotification)
                .toList();
    }
}
