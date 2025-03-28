package backend.academy.bot;

import static org.assertj.core.api.Assertions.assertThat;

import backend.academy.bot.repository.TelegramRepository;
import backend.academy.bot.repository.api.scrapper.ScrapperRepository;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

@SpringBootTest
public class LinksListFormatTest {
    @MockitoBean
    ScrapperRepository scrapperRepository;

    @Autowired
    private TelegramRepository repository;

    @Test
    public void test() {
        Mockito.when(scrapperRepository.listLinks(2L)).thenReturn(List.of("asd", "qwe"));
        assertThat(repository.listLinks(2L)).isEqualTo("1. asd\n2. qwe\n");
    }

    @TestConfiguration
    static class Config {}
}
