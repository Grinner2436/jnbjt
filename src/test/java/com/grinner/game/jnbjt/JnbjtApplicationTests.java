package com.grinner.game.jnbjt;

import com.grinner.game.jnbjt.dao.jpa.ActivityRepository;
import com.grinner.game.jnbjt.domain.entity.Activity;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.transaction.Transactional;
import java.util.Optional;

@SpringBootTest
class JnbjtApplicationTests {

    @Autowired
    private ActivityRepository activityRepository;

    @Test
    @Transactional
    void contextLoads() {
        long count = activityRepository.count();
        Optional<Activity> activity = activityRepository.findById(Integer.valueOf(1));
        Activity a = activity.get();
        System.out.println(activity.get().toString());
    }
    public static void main(String[] args) {
        System.out.println("INSERT INTO profit(id) VALUES(0);");
        //生产设施
        String[] buildings = new String[]{"","裁缝铺",
                "餐馆",
                "瓷窑",
                "锯木厂",
                "林场",
                "菱角池塘",
                "棉花田",
                "黏土矿",
                "农田",
                "丝织坊",
                "养蚕场",
                "鱼塘",
                "造纸厂",
                "织布坊",
                "民宅",
                "仓库",
                "水井",
                "家具铺",
                "画室",
                "棉花铺",
                "布匹店",
                "成衣店",
                "税课司",
                "书局",
                "瓦片店",
                "瓷器店",
                "渔具店",
                "丝绸店",
                "望火楼",
                "木牌坊",
                "书院",
                "集市",
                "戏台",
                "琴楼",
                "宗祠",
                "盆景园",
                "钓鱼台",
                "勾栏"};
        int activityId = 1;
        for (int buildingId = 1; buildingId <= 14; buildingId++) {
            //建筑信息
            StringBuffer buildingBuffer = new StringBuffer("INSERT INTO building(id,building_type,level_limitation,name) VALUES(");
            buildingBuffer.append(buildingId).append(",")
                    .append("'Production',")
                    .append("4,")
                    .append("'").append(buildings[buildingId]).append("');");
            System.out.println(buildingBuffer.toString());
            for(int j = 1;j <= 4 ;j++){
                //建造活动
                //成本和收益
                System.out.println("INSERT INTO investment(id) VALUES("+ activityId +");");
                //具体成本
                System.out.println("INSERT INTO investment_asset_properties(investment_id,amount,asset_id,asset_name) VALUES("+ activityId +",1,1,'时间');");
                System.out.println("INSERT INTO investment_asset_properties(investment_id,amount,asset_id,asset_name) VALUES("+ activityId +",1,2,'金钱');");
                System.out.println("INSERT INTO investment_asset_properties(investment_id,amount,asset_id,asset_name) VALUES("+ activityId +",1,3,'居民');");
                System.out.println("INSERT INTO investment_asset_properties(investment_id,amount,asset_id,asset_name) VALUES("+ activityId +",1,4,'工人');");
                System.out.println("INSERT INTO investment_asset_properties(investment_id,amount,asset_id,asset_name) VALUES("+ activityId +",1,5,'食物');");
                System.out.println("INSERT INTO investment_asset_properties(investment_id,amount,asset_id,asset_name) VALUES("+ activityId +",1,1,'材料二');");
                StringBuffer buildActivityBuffer = new StringBuffer("INSERT INTO activity(id,building_id,profession,priority,investment_id,profit_id,description) VALUES(");
                buildActivityBuffer.append(activityId).append(",")
                        .append(buildingId).append(",'Build',1,")
                        .append(activityId).append(",")
                        .append("0,");
                if(j == 1){
                    buildActivityBuffer.append("'").append("建造1级").append(buildings[buildingId]).append("');");
                }else {
                    buildActivityBuffer.append("'").append(buildings[buildingId]).append(":")
                            .append(j - 1).append("级")
                            .append("到")
                            .append(j).append("级").append("');");
                }
                System.out.println(buildActivityBuffer.toString());
                activityId++;

                //生产活动
                //成本和收益
                System.out.println("INSERT INTO investment(id) VALUES("+ activityId +");");
                //具体成本
                System.out.println("INSERT INTO investment_asset_properties(investment_id,amount,asset_id,asset_name) VALUES("+ activityId +",1,1,'时间');");
                System.out.println("INSERT INTO investment_asset_properties(investment_id,amount,asset_id,asset_name) VALUES("+ activityId +",1,2,'金钱');");
                System.out.println("INSERT INTO investment_asset_properties(investment_id,amount,asset_id,asset_name) VALUES("+ activityId +",1,3,'居民');");
                System.out.println("INSERT INTO investment_asset_properties(investment_id,amount,asset_id,asset_name) VALUES("+ activityId +",1,4,'工人');");
                System.out.println("INSERT INTO investment_asset_properties(investment_id,amount,asset_id,asset_name) VALUES("+ activityId +",1,5,'食物');");
                System.out.println("INSERT INTO investment_asset_properties(investment_id,amount,asset_id,asset_name) VALUES("+ activityId +",1,1,'材料二');");//TODO 这里要改成真正的投资
                System.out.println("INSERT INTO profit(id) VALUES("+ activityId +");");
                System.out.println("INSERT INTO profit_asset_properties(profit_id,amount,asset_id,asset_name) VALUES("+ activityId +",1,1,'收益');");//TODO 这里要改成真正的收益
                StringBuffer produceActivityBuffer = new StringBuffer("INSERT INTO activity(id,building_id,profession,priority,investment_id,profit_id,description) VALUES(");
                produceActivityBuffer.append(activityId).append(",")
                        .append(buildingId).append(",'Farm',1,")//TODO 这里要区分职业
                        .append(activityId).append(",")
                        .append("0,");
                produceActivityBuffer.append("'").append(j).append("级生产');");//TODO 这里生产的具体东西不一样
                System.out.println(produceActivityBuffer.toString());
                activityId++;
            }
        }
        //商业设施
        for (int buildingId = 15; buildingId <= 28; buildingId++) {
            //建筑信息
            StringBuffer buildingBuffer = new StringBuffer("INSERT INTO building(id,building_type,level_limitation,name) VALUES(");
            buildingBuffer.append(buildingId).append(",")
                    .append("'Residence',")
                    .append("4,")
                    .append("'").append(buildings[buildingId]).append("');");
            System.out.println(buildingBuffer.toString());
            for(int j = 1;j <= 4 ;j++){
                //建造活动
                //成本和收益
                System.out.println("INSERT INTO investment(id) VALUES("+ activityId +");");
                //具体成本
                System.out.println("INSERT INTO investment_asset_properties(investment_id,amount,asset_id,asset_name) VALUES("+ activityId +",1,1,'时间');");
                System.out.println("INSERT INTO investment_asset_properties(investment_id,amount,asset_id,asset_name) VALUES("+ activityId +",1,2,'金钱');");
                System.out.println("INSERT INTO investment_asset_properties(investment_id,amount,asset_id,asset_name) VALUES("+ activityId +",1,3,'居民');");
                System.out.println("INSERT INTO investment_asset_properties(investment_id,amount,asset_id,asset_name) VALUES("+ activityId +",1,4,'工人');");
                System.out.println("INSERT INTO investment_asset_properties(investment_id,amount,asset_id,asset_name) VALUES("+ activityId +",1,5,'食物');");
                System.out.println("INSERT INTO investment_asset_properties(investment_id,amount,asset_id,asset_name) VALUES("+ activityId +",1,1,'材料二');");
                StringBuffer buildActivityBuffer = new StringBuffer("INSERT INTO activity(id,building_id,profession,priority,investment_id,profit_id,description) VALUES(");
                buildActivityBuffer.append(activityId).append(",")
                        .append(buildingId).append(",'Build',1,")
                        .append(activityId).append(",")
                        .append("0,");
                if(j == 1){
                    buildActivityBuffer.append("'").append("建造1级").append(buildings[buildingId]).append("');");
                }else {
                    buildActivityBuffer.append("'").append(buildings[buildingId]).append(":")
                            .append(j - 1).append("级")
                            .append("到")
                            .append(j).append("级").append("');");
                }
                System.out.println(buildActivityBuffer.toString());
                activityId++;

                //生产活动
                //成本和收益
                System.out.println("INSERT INTO investment(id) VALUES("+ activityId +");");
                //具体成本
                System.out.println("INSERT INTO investment_asset_properties(investment_id,amount,asset_id,asset_name) VALUES("+ activityId +",1,1,'时间');");
                System.out.println("INSERT INTO investment_asset_properties(investment_id,amount,asset_id,asset_name) VALUES("+ activityId +",1,2,'金钱');");
                System.out.println("INSERT INTO investment_asset_properties(investment_id,amount,asset_id,asset_name) VALUES("+ activityId +",1,3,'居民');");
                System.out.println("INSERT INTO investment_asset_properties(investment_id,amount,asset_id,asset_name) VALUES("+ activityId +",1,4,'工人');");
                System.out.println("INSERT INTO investment_asset_properties(investment_id,amount,asset_id,asset_name) VALUES("+ activityId +",1,5,'食物');");
                System.out.println("INSERT INTO investment_asset_properties(investment_id,amount,asset_id,asset_name) VALUES("+ activityId +",1,1,'材料二');");
                System.out.println("INSERT INTO profit(id) VALUES("+ activityId +");");
                System.out.println("INSERT INTO profit_asset_properties(profit_id,amount,asset_id,asset_name) VALUES("+ activityId +",1,1,'收益');");
                StringBuffer produceActivityBuffer = new StringBuffer("INSERT INTO activity(id,building_id,profession,priority,investment_id,profit_id,description) VALUES(");
                produceActivityBuffer.append(activityId).append(",")
                        .append(buildingId).append(",'Finance',1,")//TODO 这里要区分职业
                        .append(activityId).append(",")
                        .append("0,");
                produceActivityBuffer.append("'").append(j).append("级商业活动');");//TODO 这里生产的具体东西不一样
                System.out.println(produceActivityBuffer.toString());
                activityId++;
            }
        }
        //娱乐设施
        for (int buildingId = 29; buildingId <= 38; buildingId++) {
            //建筑信息
            StringBuffer buildingBuffer = new StringBuffer("INSERT INTO building(id,building_type,level_limitation,name) VALUES(");
            buildingBuffer.append(buildingId).append(",")
                    .append("'Entertainment',")
                    .append("0,")
                    .append("'").append(buildings[buildingId]).append("');");
            System.out.println(buildingBuffer.toString());
            //建造活动
            //成本和收益
            System.out.println("INSERT INTO investment(id) VALUES("+ activityId +");");
            //具体成本
            System.out.println("INSERT INTO investment_asset_properties(investment_id,amount,asset_id,asset_name) VALUES("+ activityId +",1,1,'时间');");
            System.out.println("INSERT INTO investment_asset_properties(investment_id,amount,asset_id,asset_name) VALUES("+ activityId +",1,2,'金钱');");
            System.out.println("INSERT INTO investment_asset_properties(investment_id,amount,asset_id,asset_name) VALUES("+ activityId +",1,3,'居民');");
            System.out.println("INSERT INTO investment_asset_properties(investment_id,amount,asset_id,asset_name) VALUES("+ activityId +",1,4,'工人');");
            System.out.println("INSERT INTO investment_asset_properties(investment_id,amount,asset_id,asset_name) VALUES("+ activityId +",1,5,'食物');");
            System.out.println("INSERT INTO investment_asset_properties(investment_id,amount,asset_id,asset_name) VALUES("+ activityId +",1,1,'材料二');");
            StringBuffer buildActivityBuffer = new StringBuffer("INSERT INTO activity(id,building_id,profession,priority,investment_id,profit_id,description) VALUES(");
            buildActivityBuffer.append(activityId).append(",")
                    .append(buildingId).append(",'Build',1,")
                    .append(activityId).append(",")
                    .append("0,");
            buildActivityBuffer.append("'建造").append(buildings[buildingId]).append("');");
            System.out.println(buildActivityBuffer.toString());
            activityId++;
        }
    }



}
