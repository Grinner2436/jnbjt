package com.grinner.game.jnbjt.crawler.handler;

import com.alibaba.fastjson.JSONObject;
import com.grinner.game.jnbjt.dao.jpa.TreasureRepository;
import com.grinner.game.jnbjt.domain.entity.Treasure;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Component
public class TreasureHandler implements LinkHandler {

    @Autowired
    private TreasureRepository treasureRepository;

    @Override
    public void handle(String link, JSONObject context) {
        RestTemplate restTemplate = new RestTemplate();
        String data = restTemplate.getForObject(link,String.class);
        Document document = Jsoup.parse(data);

        String treasureName = document.select("#firstHeading").text().replaceAll("\\p{Zs}","");
        boolean treasureExists = treasureRepository.existsByName(treasureName);
        if(treasureExists){
            return;
        }
        Treasure treasure = new Treasure();
        treasure.setName(treasureName);
        treasureRepository.save(treasure);
    }
}
