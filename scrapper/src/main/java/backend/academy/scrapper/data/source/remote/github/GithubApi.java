package backend.academy.scrapper.data.source.remote.github;

import backend.academy.scrapper.data.source.remote.github.responses.BranchModel;
import backend.academy.scrapper.data.source.remote.github.responses.CommitModel;
import backend.academy.scrapper.data.source.remote.github.responses.IssueCommentModel;
import backend.academy.scrapper.data.source.remote.github.responses.IssueModel;
import backend.academy.scrapper.util.ParseTime;
import java.net.URI;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.RestClient;
import org.springframework.web.util.UriComponentsBuilder;

@Repository
public class GithubApi {
    private static final Logger log = LogManager.getLogger(GithubApi.class);
    private final RestClient restClient;

    public GithubApi(@Qualifier("ghRestClient") RestClient restClient) {
        this.restClient = restClient;
    }

    public BranchModel[] getBranchesOfRepo(String owner, String repo) {
        String a = String.format("%s/%s/branches", owner, repo);
        var resp = restClient.get().uri(a).exchange((request, response) -> {
            if (response.getStatusCode().is2xxSuccessful()) {
                return new ResponseEntity<>(
                        response.bodyTo(BranchModel[].class), response.getHeaders(), response.getStatusCode());
            } else {
                log.error(response.getHeaders());
                log.error(request.getURI());
                log.error(request.getAttributes());
                log.error(request.getMethod());
                return new ResponseEntity<>(new BranchModel[0], response.getHeaders(), response.getStatusCode());
            }
        });
        if (resp == null) {
            return new BranchModel[0];
        }

        return resp.getBody();
    }

    public CommitModel[] getCommitsOfBranch(String owner, String repo, String branchSHA, String sinceTime) {
        String a = String.format("%s/%s/commits?sha=%s&since=%s", owner, repo, branchSHA, sinceTime);

        var resp = restClient.get().uri(a).exchange((request, response) -> {
            if (response.getStatusCode().is2xxSuccessful()) {
                return new ResponseEntity<>(
                        response.bodyTo(CommitModel[].class), response.getHeaders(), response.getStatusCode());
            } else {
                log.error(response.getHeaders());
                log.error(request.getURI());
                log.error(request.getAttributes());
                log.error(request.getMethod());
                return new ResponseEntity<>(new CommitModel[0], response.getHeaders(), response.getStatusCode());
            }
        });
        if (resp == null) {
            return new CommitModel[0];
        }

        return resp.getBody();
    }

    public List<IssueModel> getIssuesOfRepo(String owner, String repo) {
        URI nextPageUri = UriComponentsBuilder.fromPath("{owner}/{repo}/issues")
                .queryParam("per_page", 100)
                .queryParam("state", "open")
                .queryParam("sort", "created")
                .queryParam("direction", "asc")
                .buildAndExpand(owner, repo)
                .toUri();

        var allIssues = new ArrayList<IssueModel>();

        while (nextPageUri != null) {
            ResponseEntity<IssueModel[]> responseEntity = restClient
                    .get()
                    .uri(nextPageUri)
                    .exchange((request, response) -> {
                        if (response.getStatusCode().isError()) {
                            log.error(request.getURI());
                            log.error(request.getAttributes());
                            log.error(request.getMethod());
                            return new ResponseEntity<>(
                                    new IssueModel[0], response.getHeaders(), response.getStatusCode());
                        }

                        return new ResponseEntity<>(
                                response.bodyTo(IssueModel[].class), response.getHeaders(), response.getStatusCode());
                    });

            if (responseEntity == null) {
                return List.of();
            }
            var issues = responseEntity.getBody();

            if (issues == null || issues.length == 0) {
                throw new RuntimeException("no issues found");
            }

            allIssues.addAll(Arrays.asList(issues));

            HttpHeaders headers = responseEntity.getHeaders();

            List<String> linkHeader = headers.get("Link");
            nextPageUri = (linkHeader != null) ? extractNextPageUrl(linkHeader) : null;
        }
        return allIssues;
    }

    public List<IssueCommentModel> getCommentsOfIssue(String owner, String repo, Integer issueNumber, Instant since) {
        URI nextPageUri = UriComponentsBuilder.fromPath("/repos/{user}/{repo}/issues/{issue_number}/comments")
                .queryParam("per_page", 100)
                .queryParam("since", ParseTime.parseTime(since))
                .buildAndExpand(owner, repo, issueNumber)
                .toUri();

        var allComments = new ArrayList<IssueCommentModel>();

        while (nextPageUri != null) {
            ResponseEntity<IssueCommentModel[]> responseEntity = restClient
                    .get()
                    .uri(nextPageUri)
                    .exchange((request, response) -> {
                        if (response.getStatusCode().isError()) {
                            log.error(request.getURI());
                            log.error(request.getAttributes());
                            log.error(request.getMethod());

                            return new ResponseEntity<>(
                                    new IssueCommentModel[0], response.getHeaders(), response.getStatusCode());
                        }

                        return new ResponseEntity<>(
                                response.bodyTo(IssueCommentModel[].class),
                                response.getHeaders(),
                                response.getStatusCode());
                    });

            if (responseEntity == null) {
                return List.of();
            }
            var issues = responseEntity.getBody();

            if (issues == null || issues.length == 0) {
                return new ArrayList<>();
                //                throw new RuntimeException("no issues comments found");
            }

            allComments.addAll(Arrays.asList(issues));

            HttpHeaders headers = responseEntity.getHeaders();

            List<String> linkHeader = headers.get("Link");
            nextPageUri = (linkHeader != null) ? extractNextPageUrl(linkHeader) : null;
        }
        return allComments;
    }

    private URI extractNextPageUrl(List<String> linkHeader) {
        if (linkHeader == null || linkHeader.isEmpty()) {
            return null;
        }

        String linkValue = linkHeader.getFirst(); // Link - один заголовок со множеством связей.
        Pattern pattern = Pattern.compile("<(.*?)>; rel=\"next\"");
        Matcher matcher = pattern.matcher(linkValue);

        if (matcher.find()) {
            String url = matcher.group(1);
            return URI.create(url);
        }

        return null;
    }
}
