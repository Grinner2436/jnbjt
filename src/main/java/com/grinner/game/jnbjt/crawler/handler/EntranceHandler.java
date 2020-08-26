package com.grinner.game.jnbjt.crawler.handler;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.net.URLDecoder;
import java.text.MessageFormat;

@Slf4j
@Component
public class EntranceHandler implements LinkHandler {

    public static final String WIKI_SITE = "https://wiki.biligame.com";

    @Autowired
    private StateCapitalHandler stateCapitalHandler;

    @Autowired
    private ResidenceListHandler residenceListHandler;

    @Autowired
    private TreasureListHandler treasureListHandler;

    @Override
    public void handle(String link, JSONObject context) {
        RestTemplate restTemplate = new RestTemplate();
        String data = restTemplate.getForObject(link,String.class);
        Document document = Jsoup.parse(data);
        //州府
        Elements stateCapitals = document.select(".wiki-menu-li-1:nth-child(2) .wiki-menu-li-2 a");
        if(!stateCapitals.isEmpty()){
            stateCapitals.stream().forEach(stateCapital -> {
                String zhoufuName = stateCapital.text();
                String uri = WIKI_SITE + URLDecoder.decode(stateCapital.attr("href"));
                //州府解析器
                if(StringUtils.isBlank(zhoufuName) || StringUtils.isBlank(uri)){
                    String errorMsg = MessageFormat.format("州府信息有误，name：{1}，uri：{2}",zhoufuName, uri);
                }
                stateCapitalHandler.handle(uri,null);
            });
        }

        //居民
        {
            String uri = "https://wiki.biligame.com/jiangnan/角色图鉴";
            residenceListHandler.handle(uri,null);
        }

//        //珍宝
//        {
//            String uri = "https://wiki.biligame.com/jiangnan/珍宝图鉴";
//            treasureListHandler.handle(uri, null);
//        }
    }
}
