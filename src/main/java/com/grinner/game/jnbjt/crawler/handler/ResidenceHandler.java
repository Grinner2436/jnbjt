package com.grinner.game.jnbjt.crawler.handler;

import com.alibaba.fastjson.JSONObject;
import com.grinner.game.jnbjt.dao.jpa.*;
import com.grinner.game.jnbjt.domain.entity.*;
import com.grinner.game.jnbjt.domain.enums.BuildingType;
import com.grinner.game.jnbjt.domain.enums.Profession;
import com.grinner.game.jnbjt.domain.relation.AssetProperty;
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
public class ResidenceHandler implements LinkHandler<Void> {

    @Autowired
    private CityRepository cityRepository;

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
        professionMap.put("",Profession.Build);
        professionMap.put("",Profession.Farm);
        professionMap.put("",Profession.Craft);
        professionMap.put("",Profession.Adventure);

        buildingTypeMap.put("商业建筑",BuildingType.Decoration);
        buildingTypeMap.put("",BuildingType.Entertainment);
        buildingTypeMap.put("",BuildingType.Production);
        buildingTypeMap.put("",BuildingType.Residence);
        buildingTypeMap.put("",BuildingType.Adventure);
    }
    @Override
    public Void handle(String link, JSONObject context) {
        RestTemplate restTemplate = new RestTemplate();
        String data = restTemplate.getForObject(link,String.class);
        Document document = Jsoup.parse(data);

        Building building = new Building();
        String buildingName = document.select("#firstHeading").text().trim();
        building.setName(buildingName);

        String buildingTypeKey = document.select(".mw-normal-catlinks li:last-child a").text().trim();
        BuildingType buildingType = buildingTypeMap.get(buildingTypeKey);
        building.setBuildingType(buildingType);

        //数据区
        Elements tables = document.select(".main .row .col-lg-8");
        if(!tables.isEmpty()) {
            Elements buildingProperties = tables.first().select("tbody tr td");
            //保存建筑定义
            {
                //关联州府
                Elements citieElements = buildingProperties.get(0).select("a");
                List<StateCapital> cities = new ArrayList<>();
                for (Element cityElement : citieElements) {
                    String cityName = cityElement.attr("title");
                    //拿出州府ID做关联
                    StateCapital city = cityRepository.findByName(cityName);
                    if (city != null) {
                        cities.add(city);
                    }
                }
                building.setCities(cities);

                //解锁等级
                Integer unlockLevel = Integer.valueOf(buildingProperties.get(1).text().trim());
                building.setUnlockLevel(unlockLevel);
                String amountLimitationStr = buildingProperties.get(2).text().trim();
                building.setAmountLimitation(amountLimitationStr);
                buildingRepository.save(building);
            }

            //建造此建筑的活动
            {
                Activity activity = new Activity();
                //建造建筑总投资
                List<AssetProperty> investments = new ArrayList<>();
                //建造建筑时间投资
                {
                    String assetName = buildingProperties.get(3).select("img").attr("alt").replaceAll(".png", "");
                    String sourceTimeString = buildingProperties.get(3).text().replaceAll("&nbsp;", "").trim();
                    int minuteAmount = 0;
                    {
                        Pattern minutePattern = Pattern.compile("(\\d+)小时");
                        Matcher matcher = minutePattern.matcher(sourceTimeString);
                        if(matcher.matches()){
                            String minuteString = matcher.group(1);
                            int minute = Integer.parseInt(minuteString) * 60;
                            minuteAmount += minute;
                        }
                    }
                    {
                        Pattern minutePattern = Pattern.compile("(\\d+)分");
                        Matcher matcher = minutePattern.matcher(sourceTimeString);
                        if(matcher.matches()){
                            String minuteString = matcher.group(1);
                            int minute = Integer.parseInt(minuteString);
                            minuteAmount += minute;
                        }
                    }
                    {
                        Pattern secondPattern = Pattern.compile("(\\d+)秒");
                        Matcher matcher = secondPattern.matcher(sourceTimeString);
                        if(matcher.matches()){
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
                    assetProperty.setAsset(asset);
                    assetProperty.setAmount(minuteAmount);
                    assetProperty.setAssetName(asset.getName());
                    investments.add(assetProperty);
                }
                //建造建筑实物投资
                List<Node> investmentElementsOfCreate = buildingProperties.get(5).children().get(0).childNodes();
                for (int index = 0; index < investmentElementsOfCreate.size(); ) {
                    String assetName = investmentElementsOfCreate.get(index++).attr("alt").replaceAll(".png", "");
                    Asset asset = assetRepository.findByName(assetName);
                    if (asset == null) {
                        asset = new Asset();
                        asset.setName(assetName);
                        assetRepository.save(asset);
                    }
                    if(index == investmentElementsOfCreate.size() - 1){
                       activity.setNeedResident(Boolean.TRUE);
                       break;
                    }
                    //通过数据库缓存拿到对应类型
                    Integer amount = Integer.valueOf(investmentElementsOfCreate.get(index++).outerHtml().replaceAll("&nbsp;", "").trim());
                    AssetProperty assetProperty = new AssetProperty();
                    assetProperty.setAsset(asset);
                    assetProperty.setAmount(amount);
                    assetProperty.setAssetName(asset.getName());
                    investments.add(assetProperty);
                }
                Investment investment = new Investment();
                investment.setAssetProperties(investments);
                investmentRepository.save(investment);

                //建造建筑活动
                activity.setDescription("建造建筑：1级" + buildingName);
                activity.setBuilding(building);
                activity.setProfit(null);
                activity.setInvestment(investment);
                activity.setProfession(Profession.Build);
                activityRepository.save(activity);
            }

            //获取职业
            String professionKey = buildingProperties.get(4).select("td").text().trim();
            //经营活动职业
            Profession profession = professionMap.get(professionKey);

            Elements buildingContents = tables.select("#buildingContent table tbody");
            int level = 1;
            for(int i = 0;i < buildingContents.size() && i < 8; i++) {
                Element buildingContentTbody = buildingContents.get(i);
                Elements tds = buildingContentTbody.select("td");

                //经营此建筑的活动
                if("民宅".equals(buildingName)){
                    homeManagementAvtivity(tds,building,profession);
                }else if("仓库".equals(buildingName)||"税课司".equals(buildingName)){

                }else{
                    generalManagementAvtivity(tds,building,profession);
                }
                //升级此建筑的活动
                {
                    Activity activity = new Activity();
                    //升级建筑活动名称
                    StringBuffer activityNameBuffer = new StringBuffer("升级建筑：")
                            .append(level).append("级").append(buildingName)
                            .append(" -> ")
                            .append(++level).append("级").append(buildingName);
                    String activityName = activityNameBuffer.toString();

                    //升级建筑活动总投资
                    List<AssetProperty> investments = new ArrayList<>();
                    //升级建筑活动时间投资
                    {
                        String sourceTimeString = tds.get(6).children().get(0)
                                .children().get(1).text()
                                .replaceAll("&nbsp;", "").trim();
                        if(StringUtils.isBlank(sourceTimeString)){
                            continue;
                        }
                        int minuteAmount = 0;
                        {
                            Pattern minutePattern = Pattern.compile("(\\d+)小时");
                            Matcher matcher = minutePattern.matcher(sourceTimeString);
                            if(matcher.matches()){
                                String minuteString = matcher.group(1);
                                int minute = Integer.parseInt(minuteString) * 60;
                                minuteAmount += minute;
                            }
                        }
                        {
                            Pattern minutePattern = Pattern.compile("(\\d+)分");
                            Matcher matcher = minutePattern.matcher(sourceTimeString);
                            if(matcher.matches()){
                                String minuteString = matcher.group(1);
                                int minute = Integer.parseInt(minuteString);
                                minuteAmount += minute;
                            }
                        }
                        {
                            Pattern secondPattern = Pattern.compile("(\\d+)秒");
                            Matcher matcher = secondPattern.matcher(sourceTimeString);
                            if(matcher.matches()){
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
                        assetProperty.setAsset(asset);
                        assetProperty.setAmount(minuteAmount);
                        assetProperty.setAssetName(asset.getName());
                        investments.add(assetProperty);
                    }

                    //升级建筑活动实物投资
                    List<Node> investmentElements = tds.get(5).children().get(0).childNodes();
                    for (int index = 0; index < investmentElements.size(); ) {
                        String assetName = investmentElements.get(index++).attr("alt").replaceAll(".png", "");
                        Asset asset = assetRepository.findByName(assetName);
                        if (asset == null) {
                            asset = new Asset();
                            asset.setName(assetName);
                            assetRepository.save(asset);
                        }
                        if(index == investmentElements.size() - 1){
                            activity.setNeedResident(Boolean.TRUE);
                            break;
                        }
                        //通过数据库缓存拿到对应类型
                        Integer amount = Integer.valueOf(investmentElements.get(index++).outerHtml().replaceAll("&nbsp;", "").trim());
                        AssetProperty assetProperty = new AssetProperty();
                        assetProperty.setAsset(asset);
                        assetProperty.setAmount(amount);
                        assetProperty.setAssetName(asset.getName());
                        investments.add(assetProperty);
                    }
                    Investment investment = new Investment();
                    investment.setAssetProperties(investments);
                    investmentRepository.save(investment);

                    activity.setDescription(activityName);
                    activity.setBuilding(building);
                    activity.setProfit(null);
                    activity.setInvestment(investment);
                    activity.setProfession(profession);
                    activityRepository.save(activity);
                }
            }
        }
        return null;
    }
    private void generalManagementAvtivity(Elements tds, Building building, Profession profession){
        {
            Activity activity = new Activity();
            //经营活动名称
            String activityName = tds.get(0).text().trim();

            //经营活动总投资
            List<AssetProperty> investments = new ArrayList<>();
            //经营活动时间投资
            {
                String sourceTimeString = tds.get(2).children().get(0)
                        .children().get(1).text().replaceAll("&nbsp;", "").trim();
                if(StringUtils.isBlank(sourceTimeString)){
                    return;
                }
                int minuteAmount = 0;
                {
                    Pattern minutePattern = Pattern.compile("(\\d+)小时");
                    Matcher matcher = minutePattern.matcher(sourceTimeString);
                    if(matcher.matches()){
                        String minuteString = matcher.group(1);
                        int minute = Integer.parseInt(minuteString) * 60;
                        minuteAmount += minute;
                    }
                }
                {
                    Pattern minutePattern = Pattern.compile("(\\d+)分");
                    Matcher matcher = minutePattern.matcher(sourceTimeString);
                    if(matcher.matches()){
                        String minuteString = matcher.group(1);
                        int minute = Integer.parseInt(minuteString);
                        minuteAmount += minute;
                    }
                }
                {
                    Pattern secondPattern = Pattern.compile("(\\d+)秒");
                    Matcher matcher = secondPattern.matcher(sourceTimeString);
                    if(matcher.matches()){
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
                assetProperty.setAsset(asset);
                assetProperty.setAmount(minuteAmount);
                assetProperty.setAssetName(asset.getName());
                investments.add(assetProperty);
            }

            //经营活动实物投资
            List<Node> investmentElements = tds.get(3).children().get(0).childNodes();
            for (int index = 0; index < investmentElements.size(); ) {
                String assetName = investmentElements.get(index++).attr("alt").replaceAll(".png", "");
                Asset asset = assetRepository.findByName(assetName);
                if (asset == null) {
                    asset = new Asset();
                    asset.setName(assetName);
                    assetRepository.save(asset);
                }
                if(index == investmentElements.size() - 1){
                    activity.setNeedResident(Boolean.TRUE);
                    break;
                }
                //通过数据库缓存拿到对应类型
                Integer amount = Integer.valueOf(investmentElements.get(index++).outerHtml().replaceAll("&nbsp;", "").trim());
                AssetProperty assetProperty = new AssetProperty();
                assetProperty.setAsset(asset);
                assetProperty.setAmount(amount);
                assetProperty.setAssetName(asset.getName());
                investments.add(assetProperty);
            }
            Investment investment = new Investment();
            investment.setAssetProperties(investments);
            investmentRepository.save(investment);

            //经营活动回报
            List<AssetProperty> rewards = new ArrayList<>();
            Elements finaceItemReward = tds.get(1).children().get(0).children();
            for (int index = 0; index < finaceItemReward.size(); ) {
                String assetName = finaceItemReward.get(index++).attr("alt").replaceAll(".png", "");
                Asset asset = assetRepository.findByName(assetName);
                if (asset == null) {
                    asset = new Asset();
                    asset.setName(assetName);
                    assetRepository.save(asset);
                }
                //通过数据库缓存拿到对应类型
                Integer amount = Integer.valueOf(finaceItemReward.get(index++).text().replaceAll("&nbsp;", "").trim());
                AssetProperty assetProperty = new AssetProperty();
                assetProperty.setAsset(asset);
                assetProperty.setAmount(amount);
                assetProperty.setAssetName(asset.getName());
                rewards.add(assetProperty);
            }
            Profit profit = new Profit();
            profit.setAssetProperties(rewards);
            profitRepository.save(profit);

            activity.setDescription("商业经营：" + activityName);
            activity.setBuilding(building);
            activity.setProfit(profit);
            activity.setInvestment(investment);
            activity.setProfession(profession);
            activityRepository.save(activity);
        }
    }
    private void homeManagementAvtivity(Elements tds, Building building, Profession profession){
        {
            //经营活动名称
            String activityName = "税收";

            //经营活动总投资
            List<AssetProperty> investments = new ArrayList<>();
            //经营活动时间投资
            int minuteAmount = 0;
            {
                String sourceTimeString = tds.get(2).select("div").get(0).childNode(1).outerHtml()
                        .replaceAll("&nbsp;", "").trim();
                if(StringUtils.isBlank(sourceTimeString)){
                    return;
                }
                {
                    Pattern minutePattern = Pattern.compile("(\\d+)小时");
                    Matcher matcher = minutePattern.matcher(sourceTimeString);
                    if(matcher.matches()){
                        String minuteString = matcher.group(1);
                        int minute = Integer.parseInt(minuteString) * 60;
                        minuteAmount += minute;
                    }
                }
                {
                    Pattern minutePattern = Pattern.compile("(\\d+)分");
                    Matcher matcher = minutePattern.matcher(sourceTimeString);
                    if(matcher.matches()){
                        String minuteString = matcher.group(1);
                        int minute = Integer.parseInt(minuteString);
                        minuteAmount += minute;
                    }
                }
                {
                    Pattern secondPattern = Pattern.compile("(\\d+)秒");
                    Matcher matcher = secondPattern.matcher(sourceTimeString);
                    if(matcher.matches()){
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
                assetProperty.setAsset(asset);
                assetProperty.setAmount(minuteAmount);
                assetProperty.setAssetName(asset.getName());
                investments.add(assetProperty);
            }
            Investment investment = new Investment();
            investment.setAssetProperties(investments);
            investmentRepository.save(investment);

            //经营活动回报
            List<AssetProperty> rewards = new ArrayList<>();
            {
                AssetProperty assetProperty = new AssetProperty();
                Asset asset = assetRepository.findByName("铜钱");
                if (asset == null) {
                    asset = new Asset();
                    asset.setName("铜钱");
                    assetRepository.save(asset);
                }
                assetProperty.setAsset(asset);

                String sourceProfitString = tds.get(0).text().trim();
                {
                    Pattern minutePattern = Pattern.compile("(\\d+\\.\\d+)钱");
                    Matcher matcher = minutePattern.matcher(sourceProfitString);
                    if(matcher.matches()){
                        String minuteString = matcher.group(1);
                        float moneyPerMinus = Float.parseFloat(minuteString);
                        float totalMoney  = moneyPerMinus * minuteAmount;
                        assetProperty.setAmount((int) totalMoney);
                    }
                }
                assetProperty.setAssetName(asset.getName());
                rewards.add(assetProperty);
            }

            Profit profit = new Profit();
            profit.setAssetProperties(rewards);
            profitRepository.save(profit);

            Activity activity = new Activity();
            activity.setDescription(activityName);
            activity.setBuilding(building);
            activity.setProfit(profit);
            activity.setInvestment(investment);
            activity.setProfession(profession);
            activityRepository.save(activity);
        }
    }
}
