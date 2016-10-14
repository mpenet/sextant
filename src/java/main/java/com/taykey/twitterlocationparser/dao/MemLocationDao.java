package com.taykey.twitterlocationparser.dao;

import java.util.*;

import com.taykey.twitterlocationparser.dto.Location;
import com.taykey.twitterlocationparser.dto.LocationType;

public class MemLocationDao implements LocationDao {

    private Map<String, List<Location>> locations = new HashMap<String, List<Location>>();

    private Map<String, Location> countries = new HashMap<String, Location>();

    private Map<String, Location> states = new HashMap<String, Location>();

    public MemLocationDao() {
    }

    @Override
    public void addLocation(Location location) {
        String alternateNames = location.getAlternateNames();
        alternateNames += "," + location.getName();
        String[] names = alternateNames.split(",");
        for (String name : names) {
            name = name.trim();
            if (name.length() < 2 || (location.getType() == LocationType.City && name.length() <= 2)) {
                continue;
            }

            List<Location> list = locations.get(name.toLowerCase());
            if (list == null) {
                list = new ArrayList<Location>();
                locations.put(name.toLowerCase(), list);
            }
            list.add(location);
        }

        if (LocationType.Country.equals(location.getType())) {
            countries.put(location.getCountryCode(), location);
        }

        if (LocationType.State.equals(location.getType())) {
            states.put(location.getStateCode(), location);
        }
    }

    @Override
    public List<Location> getLocation(String location) {
        return locations.get(location.toLowerCase());
    }

    @Override
    public Location getCountryByCode(String countryCode) {
        return countries.get(countryCode);
    }

    @Override
    public Location getStateByCode(String stateCode) {
        return states.get(stateCode);
    }

    @Override
    public void sortLocationsBy(final Comparator<Location> comparator) {
        for (Map.Entry<String, List<Location>> location: locations.entrySet())
            Collections.sort(location.getValue(), comparator);
    }
}
