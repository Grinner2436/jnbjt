package com.grinner.game.jnbjt.crawler.handler;

import com.alibaba.fastjson.JSONObject;
import com.grinner.game.jnbjt.dao.jpa.*;
import com.grinner.game.jnbjt.domain.entity.*;
import com.grinner.game.jnbjt.domain.enums.AttrbuiteLevel;
import com.grinner.game.jnbjt.domain.enums.Profession;
import com.grinner.game.jnbjt.domain.enums.ResidentGrade;
import com.grinner.game.jnbjt.domain.instance.AttributeProperty;
import com.grinner.game.jnbjt.domain.instance.ResidentProperty;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
public class ResidenceHandler implements LinkHandler {

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private ResidentRepository residentRepository;

    @Autowired
    private ResidentPropertyRepository residentPropertyRepository;

    @Autowired
    private TalentRepository talentRepository;

    @Autowired
    private TalentStageRepository talentStageRepository;

    @Autowired
    private ResidentLevelRepository residentLevelRepository;

    @Override
    public void handle(String link, JSONObject context) {
        RestTemplate restTemplate = new RestTemplate();
        String data = restTemplate.getForObject(link,String.class);
        Document document = Jsoup.parse(data);

        String residenceName = document.select("#firstHeading").text().replaceAll("\\p{Zs}","");
        Resident resident = residentRepository.findByName(residenceName);
        if(resident == null){
            resident = new Resident();
        }
        resident.setName(residenceName);

//        String gradeName = document.select("#mw-normal-catlinks li:last-child a").text();
        String gradeName = context.getString("grade");
        resident.setGrade(ResidentGrade.getResidentGrade(gradeName));

        //数据区
        Elements tables = document.select(".row>div>table");
        if(!tables.isEmpty()) {
            Elements residentProperties = tables.get(2).select(".wikitable tbody tr");
            //关联书籍
            Elements bookElements = residentProperties.get(6).select("img");
            List<Book> books = new ArrayList<>();
            for (Element bookElement : bookElements) {
                String bookName = bookElement.attr("alt").replaceAll(".png", "");
                Book book = bookRepository.findByName(bookName);
                if (book == null) {
                    book = new Book();
                    book.setName(bookName);
                    bookRepository.save(book);
                }
                books.add(book);
            }
            resident.setPreferredbooks(books);
            //关联初始属性
            Map<Profession, AttributeProperty> minAttributes = new HashMap<>();
            Map<Profession, AttributeProperty> maxAttributes = new HashMap<>();
            for (int rowNum = 1;rowNum <= 5; rowNum++) {
                Element attributeRow = residentProperties.get(rowNum);
                Elements columns = attributeRow.children();
                //职业
                String professionName = columns.get(0).text().replaceAll("\\p{Zs}","");
                Profession profession = Profession.getProfession(professionName);
                //等级
                String attrbuiteLevelName = columns.get(1).text().replaceAll("\\p{Zs}","");
                AttrbuiteLevel attrbuiteLevel = AttrbuiteLevel.getAttrbuiteLevel(attrbuiteLevelName);
                //初始值
                String minAttribute = columns.get(2).text().replaceAll("\\p{Zs}","");
                Integer minAttrbuiteValue = Integer.valueOf(0);
                if(StringUtils.isBlank(minAttribute)){
                    minAttrbuiteValue = Integer.parseInt(minAttribute);
                }
                AttributeProperty minAttributeProperty = new AttributeProperty(profession, attrbuiteLevel,minAttrbuiteValue);
                minAttributes.put(profession, minAttributeProperty);
                //满级值
                String maxAttribute = columns.get(3).text().replaceAll("\\p{Zs}","");
                Integer maxAttrbuiteValue = Integer.valueOf(0);
                if(StringUtils.isNotBlank(maxAttribute)){
                    try {
                        maxAttrbuiteValue = Integer.parseInt(maxAttribute);
                    }catch (Exception e){
//                        System.out.println("数字格式有误：" + maxAttribute +link);
                    }
                }
                AttributeProperty maxAttributeProperty = new AttributeProperty(profession, attrbuiteLevel,maxAttrbuiteValue);
                maxAttributes.put(profession, maxAttributeProperty);
            }
            String talentName = document.select("#star2 th").text().replaceAll("\\p{Zs}","").replaceAll("天赋：","");
            Talent talent = talentRepository.findByName(talentName);
            if(talent == null){
                talent = new Talent();
            }
            talent.setName(talentName);
            talentRepository.save(talent);

            TalentStage currentTalentStage = null;
            for(int i = 0;i <= 4 ;i++){
                if(resident.getGrade() == ResidentGrade.Legendary && i < 2){//天角色直接2阶
                    continue;
                }
                if(resident.getGrade() == ResidentGrade.Excellent && i < 1){//候角色直接1阶
                    continue;
                }
                String talentStageDescription = document.select("#star" + i + " td").html();
                TalentStage talentStage = talentStageRepository.findByTalentAndLevel(talent, Integer.valueOf(i));
                if(talentStage == null){
                    talentStage =new TalentStage();
                }
                talentStage.setDescription(talentStageDescription);
                talentStage.setTalent(talent);
                talentStage.setLevel(Integer.valueOf(i));
                talentStage.setUpdated(Boolean.FALSE);
                talentStageRepository.save(talentStage);
                if(resident.getGrade() == ResidentGrade.Legendary && i == 2){
                    currentTalentStage = talentStage;
                }
                if(resident.getGrade() == ResidentGrade.Excellent && i == 1){
                    currentTalentStage = talentStage;
                }
                if(resident.getGrade() == ResidentGrade.Wellknown && i == 0){
                    currentTalentStage = talentStage;
                }
                if(resident.getGrade() == ResidentGrade.Remarkable && i == 0){
                    currentTalentStage = talentStage;
                }
            }

            resident.setMinAttributes(minAttributes);
            resident.setMaxAttributes(maxAttributes);
            resident.setTalent(talent);
            String avatar = context.getString("avatar");
            if(StringUtils.isNotBlank(avatar)){
                resident.setAvatar(avatar);
            }
            residentRepository.save(resident);

            ResidentProperty residentProperty = residentPropertyRepository.findByResident(resident);
            if(residentProperty == null){
                residentProperty = new ResidentProperty();
            }
            residentProperty.setResident(resident);
            residentProperty.setTalentStage(currentTalentStage);
            int level = 0;
            if(resident.getGrade() == ResidentGrade.Legendary || resident.getName().equals("文徵明")){
                level = 16;
            }
            if(resident.getGrade() == ResidentGrade.Excellent){
                level = 11;
            }
            if(resident.getGrade() == ResidentGrade.Wellknown){
                level = 6;
            }
            if(resident.getGrade() == ResidentGrade.Remarkable){
                level = 2;
            }
            ResidentLevel residentLevel = residentLevelRepository.findByLevel(Integer.valueOf(level));
            if(residentLevel == null){
                residentLevel = new ResidentLevel();
                residentLevel.setLevel(Integer.valueOf(level));
                residentLevelRepository.save(residentLevel);
            }
            residentProperty.setResidentLevel(residentLevel);
            residentProperty.setAttributeValues(minAttributes);
            residentPropertyRepository.save(residentProperty);
        }
    }
}
