package backend.academy.scrapper.domain.repository;

import java.util.Set;

public interface ChatRepository {
    boolean registerChat(Long chatId);

    boolean removeChat(Long chatId);

    boolean isChatRegistered(Long chatId);

    Set<Long> registeredChats();
}
