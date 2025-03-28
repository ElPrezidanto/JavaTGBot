package backend.academy.scrapper.data.impl;

import backend.academy.exceptions.NotFound;
import backend.academy.scrapper.domain.repository.ChatRepository;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.stereotype.Repository;

@Repository
public class ChatRepositoryImpl implements ChatRepository {
    private final Set<Long> chats = ConcurrentHashMap.newKeySet();

    @Override
    public boolean registerChat(Long chatId) {
        chats.add(chatId);
        return true;
    }

    @Override
    public boolean removeChat(Long chatId) {
        if (!chats.remove(chatId)) {
            throw new NotFound("", String.format("Нет чата с id = %d", chatId));
        }
        return true;
    }

    @Override
    public boolean isChatRegistered(Long chatId) {
        return chats.contains(chatId);
    }

    @Override
    public Set<Long> registeredChats() {
        return chats;
    }
}
