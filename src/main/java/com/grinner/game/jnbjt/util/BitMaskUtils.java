package com.grinner.game.jnbjt.util;

public class BitMaskUtils {
    public static boolean have(int source, int target){
        return (source & target) == target;
    }
}
