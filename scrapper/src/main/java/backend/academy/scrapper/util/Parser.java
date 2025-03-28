package backend.academy.scrapper.util;

import backend.academy.scrapper.domain.model.CustomLink;
import backend.academy.scrapper.domain.model.ObservableService;
import backend.academy.scrapper.domain.model.serviceLink.GithubLink;
import backend.academy.scrapper.domain.model.serviceLink.StackOverflowLink;
import jakarta.annotation.Nullable;
import java.net.URI;
import java.net.URISyntaxException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

@Slf4j
public class Parser {
    @Nullable
    @SuppressWarnings("StringSplitter")
    public static CustomLink parseUrl(String url) {
        try {
            ObservableService service;
            CustomLink link;
            URI uri = new URI(url).normalize();
            String host = uri.getHost();
            if (host == null) return null; // Если URL некорректен
            host = host.startsWith("www.") ? host.substring(4) : host;

            switch (host) {
                case "stackoverflow.com" -> service = ObservableService.STACK_OVERFLOW;
                case "github.com" -> service = ObservableService.GITHUB;
                default -> service = null;
            }

            String[] path = uri.getPath().split("/");
            if (path.length < 3) return null;

            switch (service) {
                case STACK_OVERFLOW -> {
                    if (path[0].isEmpty() && "questions".equals(path[1]) && StringUtils.isNumeric(path[2])) {
                        link = new StackOverflowLink(url, Long.parseLong(path[2]));
                    } else {
                        link = null;
                    }
                }
                case GITHUB -> {
                    if (path[0].isEmpty()) {
                        link = new GithubLink(url, path[1], path[2]);
                    } else {
                        link = null;
                    }
                }
                case null -> link = null;
                default -> throw new IllegalStateException("Unexpected value: " + service);
            }
            return link;
        } catch (URISyntaxException e) {
            return null; // Ошибка парсинга
        }
    }
}
