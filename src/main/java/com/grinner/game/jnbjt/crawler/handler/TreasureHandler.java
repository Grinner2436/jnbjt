package com.grinner.game.jnbjt.crawler.handler;

import com.alibaba.fastjson.JSONObject;
import com.grinner.game.jnbjt.dao.jpa.*;
import com.grinner.game.jnbjt.domain.entity.Talent;
import com.grinner.game.jnbjt.domain.entity.TalentStage;
import com.grinner.game.jnbjt.domain.entity.Treasure;
import com.grinner.game.jnbjt.domain.entity.TreasureLevel;
import com.grinner.game.jnbjt.domain.enums.AttrbuiteLevel;
import com.grinner.game.jnbjt.domain.enums.Profession;
import com.grinner.game.jnbjt.domain.relation.AttributeProperty;
import com.grinner.game.jnbjt.domain.relation.TreasureProperty;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
public class TreasureHandler implements LinkHandler {

    @Autowired
    private TreasureRepository treasureRepository;

    @Autowired
    private TreasurePropertyRepository treasurePropertyRepository;

    @Autowired
    private TreasureLevelRepository treasureLevelRepository;

    @Autowired
    private TalentRepository talentRepository;

    @Autowired
    private TalentStageRepository talentStageRepository;

    @Override
    public void handle(String link, JSONObject context) {
        RestTemplate restTemplate = new RestTemplate();
        String data = restTemplate.getForObject(link, String.class);
        Document document = Jsoup.parse(data);

        String treasureName = document.select("#firstHeading").text().replaceAll("\\p{Zs}", "");
        Treasure treasure = treasureRepository.findByName(treasureName);
        if (treasure == null) {
            treasure = new Treasure();
        }
        treasure.setName(treasureName);
        TalentStage currentTalentStage = null;
        {
            String talentName = document.select("div[id='01'] th").text().replaceAll("\\p{Zs}", "").replaceAll("天赋：", "");
            Talent talent = talentRepository.findByName(talentName);
            if (talent == null) {
                talent = new Talent();
            }
            talent.setName(talentName);
            talentRepository.save(talent);
            for(int i = 2;i <= 4 ;i++){
                String talentStageDescription = document.select("#div[id='"+ i +"0'] td").html();
                TalentStage talentStage = talentStageRepository.findByTalentAndLevel(talent, Integer.valueOf(i));
                if (talentStage == null) {
                    talentStage = new TalentStage();
                }
                talentStage.setDescription(talentStageDescription);
                talentStage.setTalent(talent);
                talentStage.setLevel(Integer.valueOf(i));
                talentStage.setUpdated(Boolean.FALSE);
                talentStageRepository.save(talentStage);
                if(i == 2){
                    currentTalentStage = talentStage;
                }
            }
            treasure.setTalent(talent);
        }
        treasureRepository.save(treasure);

        TreasureProperty treasureProperty = treasurePropertyRepository.findByTreasure(treasure);
        if(treasureProperty == null){
            treasureProperty = new TreasureProperty();
        }
        treasureProperty.setTreasure(treasure);

        TreasureLevel treasureLevel = treasureLevelRepository.findByLevel(Integer.valueOf(0));
        if(treasureLevel == null){
            treasureLevel = new TreasureLevel();
            treasureLevel.setLevel(Integer.valueOf(0));
            treasureLevelRepository.save(treasureLevel);
        }
        treasureProperty.setTreasureLevel(treasureLevel);
        treasureProperty.setTalentStage(currentTalentStage);
        Map<Profession, Integer> minAttributes = new HashMap<>();
        Elements treasureProperties = document.select(".main .row .col-lg-8>table tr");
        for (int rowNum = 1;rowNum <= 5; rowNum++) {
            Element buildRow = treasureProperties.get(rowNum - 1);
            Elements columns = buildRow.children();
            //职业
            String professionName = columns.get(0).text().replaceAll("\\p{Zs}","");
            Profession profession = Profession.getProfession(professionName);

            //初始值
            String minAttrbuite = columns.get(1).text().replaceAll("\\p{Zs}","");
            Integer minAttrbuiteValue = Integer.valueOf(0);
            if(StringUtils.isNotBlank(minAttrbuite)){
                minAttrbuiteValue = Integer.parseInt(minAttrbuite);
            }
            minAttributes.put(profession, minAttrbuiteValue);
        }
        treasureProperty.setAttributeValues(minAttributes);
        treasurePropertyRepository.save(treasureProperty);
    }
}
