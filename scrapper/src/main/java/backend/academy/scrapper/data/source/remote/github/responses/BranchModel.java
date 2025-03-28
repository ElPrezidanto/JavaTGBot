package backend.academy.scrapper.data.source.remote.github.responses;

public record BranchModel(String name, LastCommitModel commit) {
    public String toNotification(String repo) {
        return String.format("В репозитории %s была создана ветка: %s", repo, name);
    }
}
