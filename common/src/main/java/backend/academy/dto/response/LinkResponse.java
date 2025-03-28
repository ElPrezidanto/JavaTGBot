package backend.academy.dto.response;

import java.util.List;

public record LinkResponse(Long id, String url, List<String> tags, List<String> filters) {}
