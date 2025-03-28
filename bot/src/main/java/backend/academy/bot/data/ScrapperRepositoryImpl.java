package backend.academy.bot.data;

import backend.academy.bot.repository.api.scrapper.ScrapperApi;
import backend.academy.bot.repository.api.scrapper.ScrapperRepository;
import backend.academy.dto.request.AddLinkRequest;
import backend.academy.dto.request.RemoveLinkRequest;
import backend.academy.dto.response.ApiErrorResponse;
import backend.academy.dto.response.LinkResponse;
import backend.academy.dto.response.ListLinksResponse;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class ScrapperRepositoryImpl implements ScrapperRepository {
    private final ScrapperApi scrapperApi;

    @Override
    public boolean trackLinkByUserId(Long chatId, AddLinkRequest linkRequest) {
        scrapperApi.addLink(chatId, linkRequest);
        return true;
    }

    @Override
    public String untrackLinkByUserId(Long chatId, RemoveLinkRequest link) {
        var response = scrapperApi.deleteLink(chatId, link);
        if (response instanceof LinkResponse) {
            return "Отслеживание ссылки отменено";
        }

        if (response instanceof ApiErrorResponse errorResponse) {
            return errorResponse.description();
        }

        return "";
    }

    @Override
    public List<String> listLinks(Long chatId) {
        var response = scrapperApi.getLinks(chatId);
        if (response instanceof ListLinksResponse listLinksResponse) {
            return listLinksResponse.links().parallelStream()
                    .map(LinkResponse::url)
                    .toList();
        }
        return List.of();
    }

    @Override
    public void registerChat(Long chatId) {
        scrapperApi.registerChat(chatId);
    }

    @Override
    public void deleteChat(Long chatId) {
        scrapperApi.deleteChat(chatId);
    }
}
