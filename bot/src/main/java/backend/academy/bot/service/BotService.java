package backend.academy.bot.service;

import backend.academy.bot.config.BotConfig;
import backend.academy.bot.models.UserState;
import backend.academy.bot.repository.TelegramRepository;
import com.pengrad.telegrambot.Callback;
import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.SendResponse;
import jakarta.annotation.Nullable;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BotService {
    public static final String HELP_START_COMMAND = "/start - Начать взаимодействие с ботом\n";
    public static final String HELP_COMMAND = "/help - Показать это сообщение с доступными командами\n";
    public static final String HELP_TRACK_COMMAND = "/track - Начать отслеживание ссылки\n";
    public static final String HELP_UNTRACK_COMMAND = "/untrack - Прекратить отслеживание ссылки\n";
    public static final String HELP_LIST_COMMAND = "/list - Показать список отслеживаемых ссылок\n";
    private static final Logger log = LogManager.getLogger(BotService.class);
    private final TelegramBot bot;
    private final TelegramRepository telegramRepository;

    private final ConcurrentHashMap<Long, UserState> userStateMap = new ConcurrentHashMap<>();

    @Autowired
    @SuppressWarnings("MissingCasesInEnumSwitch")
    public BotService(BotConfig config, TelegramRepository telegramRepository) {
        this.bot = new TelegramBot(config.telegramToken());
        this.telegramRepository = telegramRepository;

        bot.setUpdatesListener(
                updates -> {
                    log.info("Update received: {}", updates);
                    updates.forEach(update -> {
                        if (update.message() != null) {
                            Long id = update.message().chat().id();
                            var state = userStateMap.get(id);
                            String text = update.message().text();
                            switch (text) {
                                case "/start" -> {
                                    if (state != null) state.setEvent(UserState.Event.CLOSE);
                                    startCommand(id);
                                }
                                case "/help" -> {
                                    if (state != null) state.setEvent(UserState.Event.CLOSE);
                                    helpCommand(id);
                                }
                                case "/track" -> {
                                    if (state != null) state.setEvent(UserState.Event.CLOSE);
                                    trackCommand(id);
                                }
                                case "/untrack" -> {
                                    if (state != null) state.setEvent(UserState.Event.CLOSE);
                                    untrackCommand(id);
                                }
                                case "/list" -> {
                                    if (state != null) state.setEvent(UserState.Event.CLOSE);
                                    linkCommand(id);
                                }
                                case null -> handleCommand(state, id, text);
                                default -> handleCommand(state, id, text);
                            }
                        }
                    });
                    return UpdatesListener.CONFIRMED_UPDATES_ALL;
                },
                e -> {
                    if (e.response() != null) {
                        log.error(e.response().errorCode());
                        log.error(e.response().description());
                    } else {
                        log.error("Error receiving update from Telegram Bot", e);
                    }
                });
    }

    private void handleCommand(UserState state, Long id, String text) {
        if (state == null || state.state() == UserState.State.NONE) {
            sendMessageTG(id, "Неверная команда");
            return;
        }
        switch (state.state()) {
            case WAITING_LINK -> enterLink(id, text);
            case WAITING_FILTERS -> {
                if (emptyTagFilter(text)) {
                    enterFilters(id, new String[0]);
                } else {
                    enterFilters(id, text.strip().split(" "));
                }
            }
            case WAITING_TAGS -> {
                if (emptyTagFilter(text)) {
                    enterTags(id, new String[0]);
                } else {
                    enterTags(id, text.strip().split(" "));
                }
            }
            case WAITING_UNTRACK_LINK -> {
                if (!(text == null || text.isEmpty())) {
                    state.setEvent(UserState.Event.UNTRACK_LINK);
                    untrackLink(id, text);
                } else {
                    log.error("WAITING_UNTRACK_LINK text = null");
                }
            }
            case NONE -> {
                log.error("error");
            }
        }
    }

    private void untrackLink(Long chatId, String link) {
        sendMessageTG(chatId, telegramRepository.untrack(chatId, link));
    }

    private void linkCommand(Long chatId) {
        sendMessageTG(chatId, telegramRepository.listLinks(chatId));
    }

    private void startCommand(Long chatId) {
        sendMessageTG(chatId, telegramRepository.start(chatId));
    }

    public void sendMessageTG(Long chatId, String text) {
        SendMessage sendMessage = new SendMessage(chatId, text);

        bot.execute(sendMessage, new Callback<SendMessage, SendResponse>() {
            @Override
            public void onResponse(SendMessage request, SendResponse response) {}

            @Override
            public void onFailure(SendMessage request, IOException e) {
                log.error("Error sending message", e);
            }
        });
    }

    private void trackCommand(Long chatId) {
        UserState state;

        state = userStateMap.computeIfAbsent(chatId, k -> new UserState());
        state.setEvent(UserState.Event.START_TRACK);

        sendMessageTG(chatId, "Введите ссылку");
    }

    private void untrackCommand(Long chatId) {
        UserState state;
        state = userStateMap.computeIfAbsent(chatId, k -> new UserState());
        state.setEvent(UserState.Event.START_UNTRACKING_LINK);

        sendMessageTG(chatId, "Введите ссылку");
    }

    private void helpCommand(Long id) {
        StringBuilder message = new StringBuilder("Доступные команды:\n");
        message.append(HELP_START_COMMAND);
        message.append(HELP_COMMAND);
        message.append(HELP_TRACK_COMMAND);
        message.append(HELP_UNTRACK_COMMAND);
        message.append(HELP_LIST_COMMAND);
        sendMessageTG(id, message.toString());
    }

    private void enterLink(Long chatId, String url) {
        var state = userStateMap.get(chatId);
        state.setEvent(UserState.Event.ENTER_LINK);
        state.url(url);
        sendMessageTG(chatId, "Введите фильтры (введите - если хотите без них)");
    }

    private void enterFilters(Long chatId, @Nullable String[] filters) {
        var state = userStateMap.get(chatId);
        state.setEvent(UserState.Event.ENTER_FILTER);
        state.filters(filters);
        sendMessageTG(chatId, "Введите теги (введите - если хотите без них)");
    }

    private void enterTags(Long chatId, @Nullable String[] tags) {
        var state = userStateMap.get(chatId);
        state.tags(tags);
        List<String> tagList = (tags == null || tags.length == 0) ? List.of() : Arrays.asList(tags);
        log.info(
                "Current state for chatId {}: url = {}, filters={}, tags = {}",
                chatId,
                state.url(),
                List.of(state.filters()),
                tagList);
        // log.info("url = {}", state.url());
        sendMessageTG(chatId, telegramRepository.startTracking(chatId, state.url(), tagList, List.of(state.filters())));
        state.setEvent(UserState.Event.ENTER_TAG);
    }

    private boolean emptyTagFilter(String text) {
        return text == null || text.isEmpty() || (text.length() == 1 && text.charAt(0) == '-');
    }
}
