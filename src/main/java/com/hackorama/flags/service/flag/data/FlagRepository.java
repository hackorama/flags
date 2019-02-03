package com.hackorama.flags.service.flag.data;

import java.util.HashMap;
import java.util.Map;

import com.google.gson.Gson;

import com.hackorama.flags.common.Util;
import com.hackorama.flags.data.DataStore;

public class FlagRepository {

    private static final String COUNTRY_FLAG_STORE_NAME = "COUNTRY_FLAG";
    private static final String CONTINENT_COUNTRIES_STORE_NAME = "CONTINENT_COUNTRIES";
    private static final Gson GSON = Util.getGson();
    private DataStore dataStore;

    public FlagRepository(DataStore dataStore) {
        this.dataStore = dataStore;
    }

    public void addCountry(String continent, String country) {
        dataStore.putMultiKey(CONTINENT_COUNTRIES_STORE_NAME, continent, country);
    }

    public void addFlag(String country, String flag) {
        dataStore.put(COUNTRY_FLAG_STORE_NAME, country, flag);
    }

    public String getAllFlags() {
        Map<String, Map<String, String>> continentMap = new HashMap<>();
        dataStore.getKeys(CONTINENT_COUNTRIES_STORE_NAME).forEach(continent -> {
            Map<String, String> flagMap = new HashMap<>();
            dataStore.getMultiKey(CONTINENT_COUNTRIES_STORE_NAME, continent).forEach(country -> {
                flagMap.put(country, dataStore.get(COUNTRY_FLAG_STORE_NAME, country));
            });
            continentMap.put(continent, flagMap);
        });
        return GSON.toJson(continentMap);
    }

    public String getFlagByContinent(String id) {
        Map<String, String> flagMap = new HashMap<>();
        dataStore.getMultiKey(CONTINENT_COUNTRIES_STORE_NAME, id).forEach(country -> {
            flagMap.put(country, dataStore.get(COUNTRY_FLAG_STORE_NAME, country));
        });
        return GSON.toJson(flagMap);
    }

    public String getFlagByCountryOrContinent(String id) {
        if (dataStore.contains(COUNTRY_FLAG_STORE_NAME, id)) {
            return Util.toJsonString(id, dataStore.get(COUNTRY_FLAG_STORE_NAME, id));
        } else if (dataStore.contains(CONTINENT_COUNTRIES_STORE_NAME, id)) {
            return getFlagByContinent(id);
        }
        return null;
    }

}
