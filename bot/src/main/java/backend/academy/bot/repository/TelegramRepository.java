package backend.academy.bot.repository;

import backend.academy.bot.repository.api.scrapper.ScrapperRepository;
import backend.academy.dto.request.AddLinkRequest;
import backend.academy.dto.request.RemoveLinkRequest;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

@Slf4j
@Repository
@RequiredArgsConstructor
public class TelegramRepository {
    private final ScrapperRepository scrapperRepository;

    public String start(Long chatId) {
        scrapperRepository.registerChat(chatId);
        return "Юзер зарегистрирован";
    }

    public String untrack(Long chatId, String link) {
        return scrapperRepository.untrackLinkByUserId(chatId, new RemoveLinkRequest(link));
    }

    public String listLinks(long chatId) {
        var list = scrapperRepository.listLinks(chatId);

        if (list == null || list.isEmpty()) {
            return "Нет отслеживаемых ссылок";
        }

        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < list.size(); i++) {
            stringBuilder.append(i + 1).append(". ").append(list.get(i)).append("\n");
        }

        return stringBuilder.toString();
    }

    public String startTracking(long chatId, String link, List<String> tags, List<String> filters) {
        String text;
        boolean response = scrapperRepository.trackLinkByUserId(chatId, new AddLinkRequest(link, tags, filters));
        if (response) {
            text = "Подписка на ссылку успешна";
        } else {
            text = "Ошибка при подписке на ссылку";
        }
        return text;
    }
}
