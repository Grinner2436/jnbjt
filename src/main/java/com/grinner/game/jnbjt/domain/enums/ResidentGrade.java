package com.grinner.game.jnbjt.domain.enums;
/**
 * 居民品质
 */
public enum  ResidentGrade {
    Legendary("天", 0),
    Excellent("候",1),
    Wellknown("卿",2),
    Remarkable("士",3);

    private String description;
    private Integer level;

    ResidentGrade(String description, int level) {
        this.description = description;
        this.level = level;
    }

    public static ResidentGrade getResidentGrade(String description) {
        for(ResidentGrade residentGrade : ResidentGrade.values()){
            if(residentGrade.description.equals(description)){
                return residentGrade;
            }
        }
        return Remarkable;
    }
}
