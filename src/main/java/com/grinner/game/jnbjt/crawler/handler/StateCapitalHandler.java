package com.grinner.game.jnbjt.crawler.handler;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.net.URLDecoder;
import java.text.MessageFormat;

@Slf4j
@Component
public class StateCapitalHandler implements LinkHandler<Void> {

    @Autowired
    private ResidenceHandler residenceHandler;

    @Override
    public Void handle(String link, JSONObject context) {
        RestTemplate restTemplate = new RestTemplate();
        String data = restTemplate.getForObject(link,String.class);
        Document document = Jsoup.parse(data);
        //建筑
        Elements buildings = document.select(".mw-parser-output>div[style]");
        if(!buildings.isEmpty() && buildings.size() == 3){
            Element residenceBuildingContainer = buildings.get(0);
            Elements residenceBuildings = residenceBuildingContainer.select("div>a:nth-child(1)");
            residenceBuildings.stream().forEach(stateCapital -> {
                String buildingName = stateCapital.attr("title");
                String href = stateCapital.attr("href");
                //居住建筑
                if(StringUtils.isBlank(buildingName) || StringUtils.isBlank(href)){
                    String errorMsg = MessageFormat.format("州府信息有误，name：{1}，uri：{2}",buildingName, href);
                }
                String url = EntranceHandler.WIKI_SITE + URLDecoder.decode(href);
                residenceHandler.handle(url,null);
//                JSONObject buildingContext = new JSONObject();
//                buildingContext.putAll(context);
            });

        }
        return null;
    }
}
