package com.grinner.game.jnbjt.crawler.handler;

import com.alibaba.fastjson.JSONObject;
import com.grinner.game.jnbjt.dao.jpa.BookRepository;
import com.grinner.game.jnbjt.dao.jpa.ResidentRepository;
import com.grinner.game.jnbjt.domain.entity.Book;
import com.grinner.game.jnbjt.domain.entity.Resident;
import com.grinner.game.jnbjt.domain.enums.AttrbuiteLevel;
import com.grinner.game.jnbjt.domain.enums.Profession;
import com.grinner.game.jnbjt.domain.enums.ResidentGrade;
import com.grinner.game.jnbjt.domain.relation.AttributeProperty;
import lombok.extern.slf4j.Slf4j;
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

    @Override
    public void handle(String link, JSONObject context) {
        RestTemplate restTemplate = new RestTemplate();
        String data = restTemplate.getForObject(link,String.class);
        Document document = Jsoup.parse(data);

        String residenceName = document.select("#firstHeading").text().replaceAll("\\p{Zs}","");
        boolean residentExists = residentRepository.existsByName(residenceName);
        if(residentExists){
            return;
        }
        Resident resident = new Resident();
        resident.setName(residenceName);

        String gradeName = document.select("#mw-normal-catlinks li:last-child a").text();
        resident.setGrade(ResidentGrade.getResidentGrade(gradeName));

        //数据区
        Elements tables = document.select(".main .row .col-lg-8");
        if(!tables.isEmpty()) {
            Elements residentProperties = tables.first().child(0).select(".wikitable tbody tr");
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
            Map<Profession, AttributeProperty> attributes = new HashMap<>();
            for (int rowNum = 1;rowNum <= 5; rowNum++) {
                Element buildRow = residentProperties.get(rowNum);
                Elements columns = buildRow.children();
                //职业
                String professionName = columns.get(0).text().replaceAll("\\p{Zs}","");
                Profession profession = Profession.getProfession(professionName);
                //等级
                String attrbuiteLevelName = columns.get(1).text().replaceAll("\\p{Zs}","");
                AttrbuiteLevel attrbuiteLevel = AttrbuiteLevel.getAttrbuiteLevel(attrbuiteLevelName);
                //初始值
                Integer attrbuiteValue = Integer.valueOf(columns.get(2).text().replaceAll("\\p{Zs}",""));
                AttributeProperty attributeProperty = new AttributeProperty(profession, attrbuiteLevel,attrbuiteValue);
                attributes.put(profession, attributeProperty);
            }
            resident.setAttributes(attributes);
            residentRepository.save(resident);
        }
    }
}
