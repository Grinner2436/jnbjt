package com.grinner.game.jnbjt.domain.enums;

/**
 * 职业
 */
public enum Profession {
    Build("建造"),
    Farm("农牧"),
    Craft("制作"),
    Finance("理财"),
    Adventure("探险"),
    Any("任意");

    private String name;

    Profession(String name){
        this.name = name;
    }

    public static Profession getProfession(String name) {
        for(Profession profession : Profession.values()){
            if(profession.name.equals(name)){
                return profession;
            }
        }
        return Farm;
    }
}
