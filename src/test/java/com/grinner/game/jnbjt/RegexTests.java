package com.grinner.game.jnbjt;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
class RegexTests {

    @Test
    void testSlf4j() {
        String sourceTimeString = "20分钟后存满";
        Pattern secondPattern1 = Pattern.compile("(\\d+)分");
        Matcher matcher1 = secondPattern1.matcher(sourceTimeString);
        if(matcher1.find()){
            String secondString = matcher1.group(1);
            System.out.println(secondString);
        }
    }


    @Test
    void testSlf4j1() {
        String sourceTimeString = "20分钟后存满";
        Pattern secondPattern1 = Pattern.compile("(\\d+)分");
        Matcher matcher1 = secondPattern1.matcher(sourceTimeString);
        if(matcher1.find()){
            String secondString = matcher1.group(1);
            System.out.println(secondString);
        }
    }
}
