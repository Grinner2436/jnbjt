package com.grinner.game.jnbjt.crawler.handler;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.net.URLDecoder;

@Slf4j
@Component
public class StateCapitalHandler implements LinkHandler {

    @Autowired
    private ResidenceBuildingHandler residenceBuildingHandler;

    @Autowired
    private CraftBuildingHandler craftBuildingHandler;

    @Autowired
    private EntertainmentBuildingHandler entertainmentBuildingHandler;

    @Override
    public void handle(String link, JSONObject context) {
        RestTemplate restTemplate = new RestTemplate();
        String data = restTemplate.getForObject(link,String.class);
        Document document = Jsoup.parse(data);
        //建筑
        Elements buildings = document.select(".mw-parser-output>div[style]");
        if(!buildings.isEmpty() && buildings.size() == 3){
            //居住建筑
            Element residenceBuildingContainer = buildings.get(0);
            Elements residenceBuildings = residenceBuildingContainer.select("div>a:nth-child(1)");
            residenceBuildings.stream().forEach(building -> {
                String href = building.attr("href");
                String url = EntranceHandler.WIKI_SITE + URLDecoder.decode(href);
                residenceBuildingHandler.handle(url,null);
            });

            //生产建筑
            Element craftBuildingContainer = buildings.get(1);
            Elements craftBuildings = craftBuildingContainer.select("div>a:nth-child(1)");
            craftBuildings.stream().forEach(building -> {
                String href = building.attr("href");
                String url = EntranceHandler.WIKI_SITE + URLDecoder.decode(href);
                craftBuildingHandler.handle(url,null);
            });

            //娱乐建筑
            Element entertainmentBuildingContainer = buildings.get(2);
            Elements entertainmentBuildings = entertainmentBuildingContainer.select("div>a:nth-child(1)");
            entertainmentBuildings.stream().forEach(building -> {
                String href = building.attr("href");
                String url = EntranceHandler.WIKI_SITE + URLDecoder.decode(href);
                entertainmentBuildingHandler.handle(url,null);
            });
        }
    }
}
