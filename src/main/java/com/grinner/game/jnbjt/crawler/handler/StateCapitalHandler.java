package com.grinner.game.jnbjt.crawler.handler;

import com.alibaba.fastjson.JSONObject;
import com.grinner.game.jnbjt.crawler.util.HtmlUtils;
import com.grinner.game.jnbjt.dao.jpa.ActivityRepository;
import com.grinner.game.jnbjt.dao.jpa.AssetRepository;
import com.grinner.game.jnbjt.dao.jpa.InvestmentRepository;
import com.grinner.game.jnbjt.domain.entity.Activity;
import com.grinner.game.jnbjt.domain.entity.Asset;
import com.grinner.game.jnbjt.domain.entity.Investment;
import com.grinner.game.jnbjt.domain.enums.Profession;
import com.grinner.game.jnbjt.domain.instance.AssetProperty;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.net.URLDecoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
@Component
public class StateCapitalHandler implements LinkHandler {

    @Autowired
    private ResidenceBuildingHandler residenceBuildingHandler;

    @Autowired
    private CraftBuildingHandler craftBuildingHandler;

    @Autowired
    private EntertainmentBuildingHandler entertainmentBuildingHandler;

    @Autowired
    private StatueBuildingHandler statueBuildingHandler;

    @Autowired
    private AssetRepository assetRepository;


    @Autowired
    private ActivityRepository activityRepository;


    @Autowired
    private InvestmentRepository investmentRepository;

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
        //雕像建筑
        Elements areas = document.select("#CardSelectTr tbody tr.divsort");
        if(!areas.isEmpty()){
            areas.stream().forEach(area -> {
                Elements areaInfos = area.select("td");
                String areaName = areaInfos.get(0).selectFirst("a").text();

                //探索此区域的活动
                String actityDescription= "探索区域：" + areaName;
                Activity activity = activityRepository.findByDescription(actityDescription);
                if(activity == null){
                    activity = new Activity();
                }
                activity.setDescription(actityDescription);

                Map<Asset, AssetProperty> investments = new HashMap<>();
                List<Node> investmentElementsOfAdventure = areaInfos.get(1).selectFirst("div").childNodes();
                investmentElementsOfAdventure = HtmlUtils.trim(investmentElementsOfAdventure);
                for (int index = 0; index < investmentElementsOfAdventure.size(); ) {
                    String assetName = investmentElementsOfAdventure.get(index).attr("alt").replaceAll(".png", "");
                    index++;
                    Asset asset = assetRepository.findByName(assetName);
                    if (asset == null) {
                        asset = new Asset();
                        asset.setName(assetName);
                        assetRepository.save(asset);
                    }
                    if(index == investmentElementsOfAdventure.size()){
                        activity.setNeedResident(Boolean.TRUE);
                        break;
                    }

                    AssetProperty assetProperty = new AssetProperty();
                    //通过数据库缓存拿到对应类型
                    String amountString = investmentElementsOfAdventure.get(index).outerHtml()
                            .replaceAll("&nbsp;", "").replaceAll("\\p{Zs}","");
                    if(Asset.TIME.getName().equals(asset.getName())){
                        double minuteAmount = 0;
                        {
                            Pattern minutePattern = Pattern.compile("(\\d+)小时");
                            Matcher matcher = minutePattern.matcher(amountString);
                            if(matcher.find()){
                                String minuteString = matcher.group(1);
                                int minute = Integer.parseInt(minuteString) * 60;
                                minuteAmount += minute;
                            }
                        }
                        {
                            Pattern minutePattern = Pattern.compile("(\\d+)分");
                            Matcher matcher = minutePattern.matcher(amountString);
                            if(matcher.find()){
                                String minuteString = matcher.group(1);
                                int minute = Integer.parseInt(minuteString);
                                minuteAmount += minute;
                            }
                        }
                        {
                            Pattern secondPattern = Pattern.compile("(\\d+)秒");
                            Matcher matcher = secondPattern.matcher(amountString);
                            if(matcher.find()){
                                String secondString = matcher.group(1);
                                int minute = Integer.parseInt(secondString) / 60;
                                minuteAmount += minute;
                            }
                        }
                        Double amount = Double.valueOf(minuteAmount);
                        assetProperty.setAmount(amount);
                    }else {
                        amountString = amountString.replaceAll(",","");
                        Double amount = Double.valueOf(amountString);
                        assetProperty.setAmount(amount);
                    }

                    assetProperty.setAssetName(asset.getName());
                    investments.put(asset, assetProperty);
                    index++;
                }
                Investment investment = investmentRepository.findByName(activity.getDescription() + "的投资");
                if(investment == null){
                    investment = new Investment();
                }
                investment.setName(activity.getDescription() + "的投资");
                investment.setAssetProperties(investments);
                investmentRepository.save(investment);

                activity.setNeedResident(Boolean.TRUE);
                activity.setInvestment(investment);
                activity.setProfession(Profession.Adventure);
                activityRepository.save(activity);

                //此区域的雕像
                Element statueElement = areaInfos.get(2).selectFirst("a");
                if(statueElement != null){
                    String href = statueElement.attr("href");
                    String url = EntranceHandler.WIKI_SITE + URLDecoder.decode(href);
                    statueBuildingHandler.handle(url,null);
                }
            });
        }
    }
}
