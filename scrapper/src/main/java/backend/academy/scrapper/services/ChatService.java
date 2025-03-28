package backend.academy.scrapper.services;

import backend.academy.scrapper.domain.repository.ChatRepository;
import backend.academy.scrapper.domain.repository.LinksWithChatsRepository;
import java.util.Set;
import org.springframework.stereotype.Service;

@Service
public class ChatService {
    private final ChatRepository chatRepository;
    private final LinksWithChatsRepository linksWithChatsRepository;

    public ChatService(ChatRepository chatRepository, LinksWithChatsRepository linksWithChatsRepository) {
        this.chatRepository = chatRepository;
        this.linksWithChatsRepository = linksWithChatsRepository;
    }

    public boolean registerChat(Long chatId) {
        return chatRepository.registerChat(chatId);
    }

    public boolean removeChat(Long id) {
        boolean isRegistered = chatRepository.removeChat(id);
        if (!isRegistered) return false;

        linksWithChatsRepository.untrackAllLinksByChat(id);
        return true;
    }

    public Set<Long> allChats() {
        return chatRepository.registeredChats();
    }
}
