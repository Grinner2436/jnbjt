package com.grinner.game.jnbjt;

import com.grinner.game.jnbjt.crawler.handler.EntranceHandler;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.client.RestTemplate;

import java.text.MessageFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
@SpringBootTest
class CrawlerTests {

    @Autowired
    private EntranceHandler entranceHandler;

    @Test
    void crawl() {
        String indexUrl = "https://wiki.biligame.com/jiangnan/首页";
        entranceHandler.handle(indexUrl,null);
    }

}
