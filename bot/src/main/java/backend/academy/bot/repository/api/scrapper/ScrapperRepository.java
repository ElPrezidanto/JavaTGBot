package backend.academy.bot.repository.api.scrapper;

import backend.academy.dto.request.AddLinkRequest;
import backend.academy.dto.request.RemoveLinkRequest;
import java.util.List;

public interface ScrapperRepository {
    boolean trackLinkByUserId(Long chatId, AddLinkRequest linkRequest);

    String untrackLinkByUserId(Long chatId, RemoveLinkRequest link);

    List<String> listLinks(Long chatId);

    void registerChat(Long chatId);

    void deleteChat(Long chatId);
}
