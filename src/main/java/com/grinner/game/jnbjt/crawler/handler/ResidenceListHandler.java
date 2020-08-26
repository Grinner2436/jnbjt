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
import java.util.concurrent.atomic.AtomicInteger;

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
        //居民
        Elements residentsPanels = document.select(".mw-parser-output>div[style]");
        if(!residentsPanels.isEmpty()){
            String [] grades = new String[]{"天","侯","卿","士"};
            AtomicInteger gradeIndex = new AtomicInteger();
            residentsPanels.forEach(residentPanel -> {
                Elements residents = residentPanel.select(".floatnone>a");
                String gradeName = grades[gradeIndex.getAndIncrement()];
                residents.stream().forEach(resident -> {
                    String href = resident.attr("href");
                    String url = EntranceHandler.WIKI_SITE + URLDecoder.decode(href);

                    String imgUrl = null;
                    try {
                        Element img = resident.child(0);
                        String srcset = img.attr("srcset");
                        imgUrl = srcset.substring(srcset.indexOf("1.5x") + 5,srcset.indexOf(".png 2x") + 5).replaceAll("\\p{Zs}","");
                    }catch (Exception e){
                        System.out.println("获取头像失败：" + href);
                    }
                    JSONObject childContext = new JSONObject();
                    childContext.put("avatar", imgUrl);
                    childContext.put("grade", gradeName);
                    residenceHandler.handle(url,childContext);
                });
            });
        }
    }
}
