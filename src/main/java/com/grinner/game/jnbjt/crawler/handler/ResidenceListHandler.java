package com.grinner.game.jnbjt.crawler.handler;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.net.URLDecoder;

@Slf4j
@Component
public class ResidenceListHandler implements LinkHandler {

    @Autowired
    private ResidenceHandler residenceHandler;

    @Override
    public void handle(String link, JSONObject context) {
        RestTemplate restTemplate = new RestTemplate();
        String data = restTemplate.getForObject(link,String.class);
        Document document = Jsoup.parse(data);
        //建筑
        Elements residentsPanels = document.select(".mw-parser-output>div[style]");
        if(!residentsPanels.isEmpty()){
            residentsPanels.forEach(residentPanel -> {
                Elements residents = residentPanel.select("div>a:nth-child(1)");
                residents.stream().forEach(resident -> {
                    String href = resident.attr("href");
                    String url = EntranceHandler.WIKI_SITE + URLDecoder.decode(href);
                    residenceHandler.handle(url,null);
                });
            });
        }
    }
}
