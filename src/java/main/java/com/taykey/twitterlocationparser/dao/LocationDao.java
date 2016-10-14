package com.taykey.twitterlocationparser.dao;

import java.util.Comparator;
import java.util.List;

import com.taykey.twitterlocationparser.dto.Location;

public interface LocationDao {

    void addLocation(Location location);

    List<Location> getLocation(String location);

    Location getCountryByCode(String countryCode);

    Location getStateByCode(String stateCode);

    void sortLocationsBy(Comparator<Location> comparator);
}
