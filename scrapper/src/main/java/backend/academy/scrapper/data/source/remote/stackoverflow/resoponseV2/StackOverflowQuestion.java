package backend.academy.scrapper.data.source.remote.stackoverflow.resoponseV2;

import java.util.List;

public record StackOverflowQuestion(
        List<String> tags,
        List<StackOverflowAnswer> answers,
        List<StackOverflowComment> comments,
        StackOverflowOwner owner,
        String title,
        String body) {}
