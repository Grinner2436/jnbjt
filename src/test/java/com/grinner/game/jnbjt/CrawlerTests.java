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

    @Test
    void testSlf4j() {
        String sourceTimeString = "10秒";
        Pattern secondPattern1 = Pattern.compile("(\\d+)秒");
        Pattern secondPattern2 = Pattern.compile("20分钟后存满");
        Pattern secondPattern3 = Pattern.compile("/(\\d+)(?=秒)/");
        Pattern secondPattern4 = Pattern.compile("(\\d+)(?=秒)");
        Matcher matcher1 = secondPattern1.matcher(sourceTimeString);
        Matcher matcher2 = secondPattern2.matcher(sourceTimeString);
        Matcher matcher3 = secondPattern3.matcher(sourceTimeString);
        Matcher matcher4 = secondPattern4.matcher(sourceTimeString);
        if(matcher1.matches()){
            String secondString = matcher1.group(1);
            System.out.println(secondString);
        }
        if(matcher2.matches()){
            String secondString = matcher2.group(0);
            System.out.println(secondString);
        }
        if(matcher3.matches()){
            String secondString = matcher3.group(0);
            System.out.println(secondString);
        }
        if(matcher4.matches()){
            String secondString = matcher4.group(0);
            System.out.println(secondString);
        }
    }


}
