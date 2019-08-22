package ru.maza.telegramweb.parser;

import com.google.gson.Gson;
import org.springframework.stereotype.Component;

@Component
public class Parser<K> {

    public K fromJson(String json, Class<K> tClass) {
        Gson gson = new Gson();
        return gson.fromJson(json, tClass);
    }

    public String toJson(K k) {
        Gson gson = new Gson();
        return gson.toJson(k);
    }
}
