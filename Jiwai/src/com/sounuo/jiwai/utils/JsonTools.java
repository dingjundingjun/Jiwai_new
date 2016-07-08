package com.sounuo.jiwai.utils;

import com.google.gson.Gson;

public class JsonTools {
    public static String objToGson(Object object) {
        Gson gson = new Gson();
        return gson.toJson(object);
    }

    public static <T> T GsonToObj(String json, Class<T> cls) {
        Gson gson = new Gson();
        try {
            return  gson.fromJson(json, cls);
        } catch(Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}