package backend.academy.scrapper.data.source.remote.github;

import backend.academy.scrapper.data.source.remote.github.responses.CommitModel;
import backend.academy.scrapper.data.source.remote.github.responses.IssueCommentModel;
import backend.academy.scrapper.data.source.remote.github.responses.IssueModel;
import backend.academy.scrapper.domain.model.serviceLink.GithubLink;
import backend.academy.scrapper.util.ParseTime;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor()
public final class GithubHelper {
    private final GithubApi githubApi;

    private final Set<GithubLink> linksForObserving = ConcurrentHashMap.newKeySet();

    // repo -> branches
    private final Map<GithubLink, Set<String>> repoWithBranches = new ConcurrentHashMap<>();

    // repo -> issues
    private final Map<GithubLink, Set<Integer>> repoWithIssues = new ConcurrentHashMap<>();

    // repo -> last checked time
    private final Map<GithubLink, Instant> repoWithLastCheckedTime = new ConcurrentHashMap<>();

    public void observeLink(GithubLink link) {
        linksForObserving.add(link);
    }

    public Map<GithubLink, List<String>> checkUpdates() {
        var res = new HashMap<GithubLink, List<String>>();

        linksForObserving.forEach(link -> {
            Instant time;
            time = repoWithLastCheckedTime.getOrDefault(link, Instant.now());
            repoWithLastCheckedTime.put(link, Instant.now());

            var list = new ArrayList<String>();
            list.addAll(checkBranches(time, link));
            list.addAll(checkIssues(link, time));
            res.put(link, list);
        });
        return res;
    }

    public List<String> checkBranches(Instant time, GithubLink link) {
        var res = new ArrayList<String>();

        String formatTime = ParseTime.parseTime(time);

        repoWithBranches
                .computeIfAbsent(link, k -> new HashSet<>())
                .addAll(Arrays.stream(githubApi.getBranchesOfRepo(link.owner(), link.repo()))
                        .map(branch -> {
                            if (!repoWithBranches.get(link).contains(branch.name())) {
                                res.add(branch.toNotification(link.repo()));
                            }
                            res.addAll(checkCommits(branch.name(), link, formatTime));
                            return branch.name();
                        })
                        .toList());
        return res;
    }

    public List<String> checkCommits(String branch, GithubLink link, String time) {
        return Arrays.stream(githubApi.getCommitsOfBranch(link.owner(), link.repo(), branch, time))
                .map(CommitModel::toString)
                .toList();
    }

    public List<String> checkIssues(GithubLink link, Instant time) {
        var res = new ArrayList<String>();

        var issues = githubApi.getIssuesOfRepo(link.owner(), link.repo());
        issues.forEach(issue -> {
            if (!repoWithIssues.computeIfAbsent(link, k -> new HashSet<>()).contains(issue.number())
                    && issue.time().isAfter(time)) {
                res.add(issue.toNotification(link.repo()));
            }

            res.addAll(checkCommentsOfIssue(link, time, issue.number()));
        });

        repoWithIssues
                .computeIfAbsent(link, k -> new HashSet<>())
                .addAll(issues.stream().map(IssueModel::number).toList());

        return res;
    }

    public List<String> checkCommentsOfIssue(GithubLink link, Instant time, Integer number) {
        return githubApi.getCommentsOfIssue(link.owner(), link.repo(), number, time).stream()
                .map(IssueCommentModel::toNotification)
                .toList();
    }
}
