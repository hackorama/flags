package com.hackorama.flags.service.flag.data.loader;

import java.util.ArrayList;
import java.util.List;

public class ContinentData {
    String continent;
    List<CountryData> countries = new ArrayList<>();
    public String getContinent() {
        return continent;
    }
    public void setContinent(String continent) {
        this.continent = continent;
    }
    public List<CountryData> getCountries() {
        return countries;
    }
    public void setCountries(List<CountryData> countries) {
        this.countries = countries;
    }
}
