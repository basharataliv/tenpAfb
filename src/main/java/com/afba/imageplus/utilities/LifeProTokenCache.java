package com.afba.imageplus.utilities;

import java.util.HashMap;

public class LifeProTokenCache {

    private static HashMap<String, Object> map = new HashMap<>();

    private LifeProTokenCache() {
    }

    public static HashMap<String, Object> getTokenDetails() {
        return map;
    }
}
