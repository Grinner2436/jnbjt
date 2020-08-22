package com.grinner.game.jnbjt.domain.enums;

/**
 * 居民能力
 */
public enum AttrbuiteLevel {

    Bad("差", -1),
    Acceptable("中",0),
    Good("良",1),
    Great("优",1),
    Excellent("特",2);

    private String description;
    private Integer level;

    AttrbuiteLevel(String description, int level) {
        this.description = description;
        this.level = level;
    }

    public static AttrbuiteLevel getAttrbuiteLevel(String name) {
        for(AttrbuiteLevel attrbuiteLevel : AttrbuiteLevel.values()){
            if(attrbuiteLevel.description.equals(name)){
                return attrbuiteLevel;
            }
        }
        return Bad;
    }
}
