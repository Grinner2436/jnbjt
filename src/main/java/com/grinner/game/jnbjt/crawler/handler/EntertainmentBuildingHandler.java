package com.grinner.game.jnbjt.crawler.handler;

import com.alibaba.fastjson.JSONObject;
import com.grinner.game.jnbjt.crawler.util.HtmlUtils;
import com.grinner.game.jnbjt.dao.jpa.*;
import com.grinner.game.jnbjt.domain.entity.*;
import com.grinner.game.jnbjt.domain.enums.BuildingType;
import com.grinner.game.jnbjt.domain.enums.Job;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
@Component
public class EntertainmentBuildingHandler implements LinkHandler {

    @Autowired
    private StateCapitalRepository stateCapitalRepository;

    @Autowired
    private BuildingRepository buildingRepository;

    @Autowired
    private AssetRepository assetRepository;

    @Autowired
    private ProfitRepository profitRepository;

    @Autowired
    private InvestmentRepository investmentRepository;

    @Autowired
    private ActivityRepository activityRepository;

    @Override
    public void handle(String link, JSONObject context) {
        RestTemplate restTemplate = new RestTemplate();
        String data = restTemplate.getForObject(link,String.class);
        Document document = Jsoup.parse(data);

        String buildingName = document.select("#firstHeading").text().replaceAll("\\p{Zs}","");
        Building building = buildingRepository.findByName(buildingName);
        if(building == null){
            building = new Building();
        }
        building.setName(buildingName);

        building.setBuildingType(BuildingType.Entertainment);

        //数据区
        Elements tables = document.select(".main .row .col-lg-8");
        if(!tables.isEmpty()) {
            Elements buildingProperties = tables.first().select("tbody tr td");
            //保存建筑定义
            {
                //关联州府
                Elements citieElements = buildingProperties.get(0).select("a");
                List<StateCapital> stateCapitals = new ArrayList<>();
                for (Element cityElement : citieElements) {
                    String stateCapitalName = cityElement.attr("title");
                    //拿出州府ID做关联
                    StateCapital stateCapital = stateCapitalRepository.findByName(stateCapitalName);
                    if (stateCapital == null) {
                        stateCapital = new StateCapital();
                        stateCapital.setName(stateCapitalName);
                        stateCapitalRepository.save(stateCapital);
                    }
                    stateCapitals.add(stateCapital);
                }
                building.setStateCapitals(stateCapitals);

                //解锁等级
                Integer unlockLevel = Integer.valueOf(buildingProperties.get(1).text().replaceAll("\\p{Zs}",""));
                building.setUnlockLevel(unlockLevel);
                String amountLimitationStr = buildingProperties.get(2).text().replaceAll("\\p{Zs}","");
                building.setAmountLimitation(amountLimitationStr);
                buildingRepository.save(building);
            }

            //建造此建筑的活动
            {
                String actityDescription= "建造建筑：1级" + buildingName;
                Activity activity = activityRepository.findByDescription(actityDescription);
                if(activity == null){
                    activity = new Activity();
                }
                activity.setDescription(actityDescription);

                //建造建筑总投资
                Map<Asset, AssetProperty> investments = new HashMap<>();
                //建造建筑时间投资
                {
                    String assetName = buildingProperties.get(2).select("img").attr("alt").replaceAll(".png", "");
                    String sourceTimeString = buildingProperties.get(2).text()
                            .replaceAll("&nbsp;", "").replaceAll("\\p{Zs}","");
                    double minuteAmount = 0;
                    {
                        Pattern minutePattern = Pattern.compile("(\\d+)小时");
                        Matcher matcher = minutePattern.matcher(sourceTimeString);
                        if(matcher.find()){
                            String minuteString = matcher.group(1);
                            int minute = Integer.parseInt(minuteString) * 60;
                            minuteAmount += minute;
                        }
                    }
                    {
                        Pattern minutePattern = Pattern.compile("(\\d+)分");
                        Matcher matcher = minutePattern.matcher(sourceTimeString);
                        if(matcher.find()){
                            String minuteString = matcher.group(1);
                            int minute = Integer.parseInt(minuteString);
                            minuteAmount += minute;
                        }
                    }
                    {
                        Pattern secondPattern = Pattern.compile("(\\d+)秒");
                        Matcher matcher = secondPattern.matcher(sourceTimeString);
                        if(matcher.find()){
                            String secondString = matcher.group(1);
                            int minute = Integer.parseInt(secondString) / 60;
                            minuteAmount += minute;
                        }
                    }

                    Asset asset = assetRepository.findByName(assetName);
                    if (asset == null) {
                        asset = new Asset();
                        asset.setName(assetName);
                        assetRepository.save(asset);
                    }
                    AssetProperty assetProperty = new AssetProperty();
                    //assetProperty.setAsset(asset);
                    assetProperty.setAmount(minuteAmount);
                    assetProperty.setAssetName(asset.getName());
                    investments.put(asset, assetProperty);
                }
                //建造建筑实物投资
                List<Node> investmentElementsOfCreate = buildingProperties.get(3).children().get(0).childNodes();
                investmentElementsOfCreate = HtmlUtils.trim(investmentElementsOfCreate);
                for (int index = 0; index < investmentElementsOfCreate.size(); ) {
                    String assetName = investmentElementsOfCreate.get(index).attr("alt").replaceAll(".png", "");
                    index++;
                    Asset asset = assetRepository.findByName(assetName);
                    if (asset == null) {
                        asset = new Asset();
                        asset.setName(assetName);
                        assetRepository.save(asset);
                    }
                    if(index == investmentElementsOfCreate.size()){
                       activity.setNeedResident(Boolean.TRUE);
                       break;
                    }
                    //通过数据库缓存拿到对应类型
                    Double amount = Double.valueOf(investmentElementsOfCreate.get(index).outerHtml()
                            .replaceAll("&nbsp;", "").replaceAll("\\p{Zs}",""));
                    index++;
                    AssetProperty assetProperty = new AssetProperty();
                    //assetProperty.setAsset(asset);
                    assetProperty.setAmount(amount);
                    assetProperty.setAssetName(asset.getName());
                    investments.put(asset, assetProperty);

                }
                Investment investment = investmentRepository.findByName(activity.getDescription() + "的投资");
                if(investment == null){
                    investment = new Investment();
                }
                investment.setName(activity.getDescription() + "的投资");
                investment.setAssetProperties(investments);
                investmentRepository.save(investment);

                //建造建筑活动
                activity.setBuilding(building);
                activity.setProfit(null);
                activity.setInvestment(investment);
                activity.setProfession(Profession.Build);
                activity.setJob(Job.CreateBuilding);
                activityRepository.save(activity);
            }
        }
    }
}
