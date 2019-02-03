package com.hackorama.flags.service.flag.data.loader;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.stream.JsonReader;

import com.hackorama.flags.common.Util;
import com.hackorama.flags.service.flag.data.FlagRepository;

public class DataLoader {

    private static Logger logger = LoggerFactory.getLogger(DataLoader.class);

    public static void initializeData(FlagRepository repository, String fileName) throws IOException {
        logger.info("Iniatializing the store");
        try (JsonReader reader = new JsonReader(new InputStreamReader(
                new FileInputStream(DataLoader.class.getClassLoader().getResource(fileName).getPath()), Util.getEncoding()))) {
            ContinentData[] continents = Util.getGson().fromJson(reader, ContinentData[].class);
            for (ContinentData continent : continents) {
                continent.getCountries().forEach(country -> {
                    repository.addCountry(continent.getContinent(), country.getName());
                    repository.addFlag(country.getName(), country.getFlag());
                    logger.debug("Initalizing data for {} {} {}", continent.getContinent(), country.getName(),
                            country.getFlag());
                });
            }
        }
    }

}
