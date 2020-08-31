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
import org.apache.commons.lang3.StringUtils;
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
public class CraftBuildingHandler implements LinkHandler {

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

    private static Map<String, Profession> professionMap = new HashMap<>();
    private static Map<String, BuildingType> buildingTypeMap = new HashMap<>();
    static {
        professionMap.put("商业建筑",Profession.Finance);
        professionMap.put("种植蔬菜",Profession.Farm);
        professionMap.put("种植菱角",Profession.Farm);
        professionMap.put("生产肉类",Profession.Farm);
        professionMap.put("饲养鸭子",Profession.Farm);
        professionMap.put("养蚕造丝",Profession.Farm);
        professionMap.put("生产原木",Profession.Farm);
        professionMap.put("种植棉花",Profession.Farm);
        professionMap.put("种植草药",Profession.Farm);
        professionMap.put("生产桐油",Profession.Farm);

        professionMap.put("烹制食物的地方",Profession.Craft);
        professionMap.put("加工木板",Profession.Craft);
        professionMap.put("制造纸张",Profession.Craft);
        professionMap.put("生产布匹",Profession.Craft);
        professionMap.put("纺织丝绸",Profession.Craft);
        professionMap.put("缝制成衣",Profession.Craft);
        professionMap.put("制作刺绣",Profession.Craft);
        professionMap.put("挖掘粘土",Profession.Craft);
        professionMap.put("烧制瓷器",Profession.Craft);
        professionMap.put("烧制木炭",Profession.Craft);
//        professionMap.put("炼丹",Profession.Craft);

        professionMap.put("",Profession.Adventure);

        buildingTypeMap.put("商业建筑",BuildingType.Decoration);
        buildingTypeMap.put("",BuildingType.Entertainment);
        buildingTypeMap.put("",BuildingType.Production);
        buildingTypeMap.put("",BuildingType.Residence);
        buildingTypeMap.put("",BuildingType.Adventure);
    }
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

        building.setBuildingType(BuildingType.Production);

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
                List<Node> investmentElementsOfCreate = buildingProperties.get(4).children().get(0).childNodes();
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

            //获取职业
            String professionKey = buildingProperties.get(3).select("td").text().replaceAll("\\p{Zs}","");
            //经营活动职业
            Profession profession = professionMap.get(professionKey);

            Elements buildingContents = tables.select("#buildingContent table tbody");
            int level = 1;
            for(int i = 0;i < buildingContents.size() && i < 8; i++) {
                Element buildingContentTbody = buildingContents.get(i);
                Elements tds = buildingContentTbody.select("td");

                //经营此建筑的活动
                generalManagementAvtivity(tds,building,profession);
                //升级此建筑的活动
                {
                    //升级建筑活动名称
                    StringBuffer activityNameBuffer = new StringBuffer("升级建筑：")
                            .append(level).append("级").append(buildingName)
                            .append(" -> ")
                            .append(++level).append("级").append(buildingName);
                    String activityName = activityNameBuffer.toString();
                    Activity activity = activityRepository.findByDescription(activityName);
                    if(activity == null){
                        activity = new Activity();
                    }
                    activity.setDescription(activityName);
                    //升级建筑活动总投资
                    Map<Asset, AssetProperty> investments = new HashMap<>();
                    //升级建筑活动时间投资
                    {
                        String sourceTimeString = tds.get(5).text().replaceAll("&nbsp;", "").replaceAll("\\p{Zs}","");
                        if(StringUtils.isBlank(sourceTimeString)){
                            continue;
                        }
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
                        Asset asset = assetRepository.findByName("时间");
                        if (asset == null) {
                            asset = new Asset();
                            asset.setName("时间");
                            assetRepository.save(asset);
                        }
                        AssetProperty assetProperty = new AssetProperty();
                        //assetProperty.setAsset(asset);
                        assetProperty.setAmount(minuteAmount);
                        assetProperty.setAssetName(asset.getName());
                        investments.put(asset, assetProperty);
                    }

                    //升级建筑活动实物投资
                    String investmentHtml =  tds.get(4).select(".vm").html().replaceAll("&nbsp;","").replaceAll("\\p{Zs}","");
                    List<Node> investmentElements = Jsoup.parse(investmentHtml).body().childNodes();
                    investmentElements = HtmlUtils.trim(investmentElements);
                    for (int index = 0; index < investmentElements.size(); ) {
                        String assetName = investmentElements.get(index++).attr("alt").replaceAll(".png", "");
                        Asset asset = assetRepository.findByName(assetName);
                        if (asset == null) {
                            asset = new Asset();
                            asset.setName(assetName);
                            assetRepository.save(asset);
                        }
                        if(index == investmentElements.size()){
                            activity.setNeedResident(Boolean.TRUE);
                            break;
                        }
                        //通过数据库缓存拿到对应类型
                        Double amount = Double.valueOf(investmentElements.get(index++).outerHtml()
                                .replaceAll("&nbsp;", "").replaceAll("\\p{Zs}",""));
                        AssetProperty assetProperty = new AssetProperty();
                        //assetProperty.setAsset(asset);
                        assetProperty.setAmount(amount);
                        assetProperty.setAssetName(asset.getName());
                        investments.put(asset, assetProperty);
                    }
                    Investment investment = new Investment();
                    investment.setAssetProperties(investments);
                    investmentRepository.save(investment);

                    activity.setBuilding(building);
                    activity.setProfit(null);
                    activity.setInvestment(investment);
                    activity.setProfession(Profession.Build);
                    activity.setJob(Job.UpgradeBuilding);
                    activityRepository.save(activity);
                }
            }
        }
    }
    private void generalManagementAvtivity(Elements tds, Building building, Profession profession){
        {
            //经营活动名称
            String activityName = tds.get(0).text().replaceAll("\\p{Zs}","");
            String prefix = "商业经营";
            if(profession == Profession.Craft){
                prefix = "生产制作";
            }else if (profession == Profession.Farm){
                prefix = "农牧收获";
            }
            String actityDescription= prefix + "：" + activityName;
            Activity activity = activityRepository.findByDescription(actityDescription);
            if(activity == null){
                activity = new Activity();
            }
            activity.setDescription(actityDescription);

            //经营活动总投资
            Map<Asset, AssetProperty> investments = new HashMap<>();
            //经营活动时间投资
            {
                String sourceTimeString = tds.get(2).text().replaceAll("&nbsp;", "").replaceAll("\\p{Zs}","");
                if(StringUtils.isBlank(sourceTimeString)){
                    return;
                }
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
                Asset asset = assetRepository.findByName("时间");
                if (asset == null) {
                    asset = new Asset();
                    asset.setName("时间");
                    assetRepository.save(asset);
                }
                AssetProperty assetProperty = new AssetProperty();
                //assetProperty.setAsset(asset);
                assetProperty.setAmount(minuteAmount);
                assetProperty.setAssetName(asset.getName());
                investments.put(asset, assetProperty);
            }

            //经营活动实物投资
            List<Node> investmentElements = tds.get(3).children().get(0).childNodes();
            investmentElements = HtmlUtils.trim(investmentElements);
            for (int index = 0; index < investmentElements.size(); ) {
                String assetName = investmentElements.get(index++).attr("alt").replaceAll(".png", "");
                Asset asset = assetRepository.findByName(assetName);
                if (asset == null) {
                    asset = new Asset();
                    asset.setName(assetName);
                    assetRepository.save(asset);
                }
                if(index == investmentElements.size()){
                    activity.setNeedResident(Boolean.TRUE);
                    break;
                }
                //通过数据库缓存拿到对应类型
                Double amount = Double.valueOf(investmentElements.get(index++).outerHtml()
                        .replaceAll("&nbsp;", "").replaceAll("\\p{Zs}",""));
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
            investment.setAssetProperties(investments);
            investmentRepository.save(investment);

            //经营活动回报
            Map<Asset, AssetProperty> rewards = new HashMap<>();
            List<Node> finaceItemReward = tds.get(1).children().get(0).childNodes();
            finaceItemReward = HtmlUtils.trim(finaceItemReward);
            for (int index = 0; index < finaceItemReward.size(); ) {
                String assetName = finaceItemReward.get(index++).attr("alt").replaceAll(".png", "");
                Asset asset = assetRepository.findByName(assetName);
                if (asset == null) {
                    asset = new Asset();
                    asset.setName(assetName);
                    assetRepository.save(asset);
                }
                if(index == finaceItemReward.size()){
                    break;
                }
                //通过数据库缓存拿到对应类型
                Double amount = Double.valueOf(finaceItemReward.get(index++).outerHtml()
                        .replaceAll("&nbsp;", "").replaceAll("\\p{Zs}",""));
                AssetProperty assetProperty = new AssetProperty();
                //assetProperty.setAsset(asset);
                assetProperty.setAmount(amount);
                assetProperty.setAssetName(asset.getName());
                rewards.put(asset, assetProperty);
            }
            Profit profit = profitRepository.findByName(activity.getDescription() + "的获利");
            if(profit == null){
                profit = new Profit();
            }
            profit.setAssetProperties(rewards);
            profitRepository.save(profit);

            activity.setBuilding(building);
            activity.setProfit(profit);
            activity.setInvestment(investment);
            activity.setProfession(profession);
            activityRepository.save(activity);
        }
    }
}
