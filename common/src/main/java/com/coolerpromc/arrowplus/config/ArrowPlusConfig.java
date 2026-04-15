
package com.coolerpromc.arrowplus.config;

import java.util.ArrayList;
import java.util.List;

public class ArrowPlusConfig {
    private static List<String> removal = new ArrayList<>();

    public static void sync(List<String> removalList) {
        removal = new ArrayList<>(removalList);
    }

    public static List<String> getRemoval() {
        return removal;
    }
}