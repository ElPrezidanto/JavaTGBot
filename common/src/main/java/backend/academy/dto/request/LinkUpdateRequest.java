package backend.academy.dto.request;

import jakarta.validation.constraints.NotEmpty;
import java.util.List;

public record LinkUpdateRequest(
        Long id, @NotEmpty String url, @NotEmpty String description, @NotEmpty List<Long> tgChatIds) {}
