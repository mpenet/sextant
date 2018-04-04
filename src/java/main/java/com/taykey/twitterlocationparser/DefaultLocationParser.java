package com.taykey.twitterlocationparser;

import java.util.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.taykey.twitterlocationparser.dao.LocationDao;
import com.taykey.twitterlocationparser.dao.MemLocationDao;
import com.taykey.twitterlocationparser.dto.Location;
import com.taykey.twitterlocationparser.dto.LocationType;
import com.taykey.twitterlocationparser.populatedb.DefaultPopulateDB;
import com.taykey.twitterlocationparser.populatedb.PopulateDB;

public class DefaultLocationParser implements LocationParser {

    private static Logger log = LoggerFactory
            .getLogger(DefaultLocationParser.class);

    private LocationDao locationDao;

    public DefaultLocationParser() {
        locationDao = new MemLocationDao();
        final PopulateDB populateDB = new DefaultPopulateDB(locationDao);
        populateDB.loadLocations();

        final Comparator<Location> typeComparator = new Comparator<Location>() {
            @Override
            public int compare(Location o1, Location o2) {
                return o1.getType().compareTo(o2.getType());
            }
        };

        locationDao.sortLocationsBy(typeComparator);
    }

    public DefaultLocationParser(LocationDao locationDao) {
        this.setLocationDao(locationDao);
    }

    public Location getCountryByCode(String countryCode) {
        return locationDao.getCountryByCode(countryCode);
    }

    @Override
    public Location parseText(String text) {
        if (text == null || text.isEmpty()) {
            return null;
        }
        List<String> words = ngrams(text, 3);
        Map<LocationType, Set<Location>> candidates = new HashMap<LocationType, Set<Location>>();
        Set<String> wordsWithLocation = new HashSet<String>();
        for (String word : words) {
            if (isContained(wordsWithLocation, word)) {
                continue;
            }

            List<Location> locations = locationDao.getLocation(word);
            if (locations == null) {
                continue;
            }

            boolean hasTheWordBeenUsedAsCityName = false;
            for (Location location : locations) {
                if (location.getType() == LocationType.City) {
                    hasTheWordBeenUsedAsCityName = true;
                } else if (hasTheWordBeenUsedAsCityName) {
                    break;
                }

                Set<Location> set = candidates.get(location.getType());
                if (set == null) {
                    set = new HashSet<Location>();
                    candidates.put(location.getType(), set);
                }
                set.add(location);
            }

            wordsWithLocation.add(word);
        }

        Location location = getLocationFromCandidates(candidates);
        log.trace("text: {}\t\t\tlocation: {}", text, location);
        return location;
    }

    private Location getLocationFromCandidates(
            Map<LocationType, Set<Location>> candidates) {
        
        Set<Location> cityCandidates = candidates.get(LocationType.City);
        Set<Location> stateCandidates = candidates.get(LocationType.State);
        Set<Location> countryCandidates = candidates.get(LocationType.Country);
        List<Suspect> suspects = new ArrayList<Suspect>();
        List<Suspect> defendants = new ArrayList<Suspect>();

        if (cityCandidates != null) {
            // eliminating duplications which are present in data
            int overallPopulation = 0;
            for (Location city : cityCandidates) {
                boolean duplicate = false;
                for (Suspect suspect : suspects) {
                    Location suspectLocation = suspect.getLocation();
                    boolean sameCountry = suspectLocation.getCountryCode().equals(city.getCountryCode());
                    boolean sameState = suspectLocation.getStateCode().equals(city.getStateCode());
                    boolean samePopulation = suspectLocation.getPopulation() == city.getPopulation();
                    if (sameCountry && sameState && samePopulation) {
                        duplicate = true;
                        break;
                    }
                }

                if (!duplicate) {
                    overallPopulation += city.getPopulation();
                    suspects.add(new Suspect(city, 0));
                }
            }

            for (Suspect suspect : suspects) {
                // confidence of a city is equal to percentage of its population compared to other cities
                // thus, maximum confidence is 1.0
                final double confidence = overallPopulation > 0 // avoiding division by zero
                        ? suspect.getLocation().getPopulation() / (double) overallPopulation
                        : 1.0;
                suspect.increaseConfidence(confidence);
            }
        }

        if (stateCandidates != null) {
            final double confidence = 1.2;

            for (Location state : stateCandidates) {
                boolean found = false;
                for (Suspect suspect : suspects) {
                    boolean sameState = suspect.getLocation().getStateCode().equals(state.getStateCode());
                    if (sameState) {
                        suspect.increaseConfidence(confidence);
                        found = true;
                    }
                }

                if (!found) {
                    suspects.add(new Suspect(state, confidence));
                }
            }
        }

        if (countryCandidates != null) {
            // number of countries in the world is much lower than number of cities or states
            // so countries have highest confidence since probability of error is much lower than in other cases
            final double confidence = 2.0;

            for (Location country : countryCandidates) {
                boolean found = false;
                for (Suspect suspect : suspects) {
                    boolean sameCountry = suspect.getLocation().getCountryCode().equals(country.getCountryCode());
                    if (sameCountry) {
                        suspect.increaseConfidence(confidence);
                        found = true;
                    }
                }

                if (!found) {
                    suspects.add(new Suspect(country, confidence));
                }
            }
        }

        // put suspects with the highest confidence to a list of defendants
        double maxConfidence = 0.0;
        for (Suspect suspect: suspects) {
            if (suspect.getConfidence() > maxConfidence) {
                maxConfidence = suspect.getConfidence();
                defendants.clear();
            }

            if (Double.compare(suspect.getConfidence(), maxConfidence) == 0) {
                defendants.add(suspect);
            }
        }

        if (defendants.size() == 1 && maxConfidence > 0.7) {
            return defendants.get(0).getLocation();
        }

        return null;
    }

    private boolean isContained(Set<String> wordsWithLocation, String word) {
        boolean contained = false;
        for (String prevWord : wordsWithLocation) {
            if (prevWord.contains(word)) {
                contained = true;
                break;
            }
        }
        return contained;
    }

    private List<String> ngrams(String text, int n) {
        List<String> ngrams = new ArrayList<String>();
        String[] twords = text.split("\\P{L}", 0);
        int l = 0;
        for (int i = 0; i < twords.length; i++) {
            if (!twords[i].trim().isEmpty())
                l++;
        }
        String[] words = new String[l];
        l = 0;
        for (int i = 0; i < twords.length; i++) {
            if (!twords[i].trim().isEmpty())
                words[l++] = twords[i].trim();
        }

        for (int k = n; k > 0; k--) {
            for (int i = 0; i < words.length - k + 1; i++) {
                ngrams.add(concat(words, i, i + k));
            }
        }

        return ngrams;
    }

    private String concat(String[] words, int start, int end) {
        StringBuilder sb = new StringBuilder();
        for (int i = start; i < end; i++)
            sb.append((i > start ? " " : "") + words[i]);
        return sb.toString();
    }

    public LocationDao getLocationDao() {
        return locationDao;
    }

    public void setLocationDao(LocationDao locationDao) {
        this.locationDao = locationDao;
    }

    private static class Suspect {
        Location location;

        double confidence;

        public Suspect(Location location, double confidence) {
            this.location = location;
            this.confidence = confidence;
        }

        public Location getLocation() {
            return location;
        }

        public double getConfidence() {
            return confidence;
        }

        public void increaseConfidence(double factor) {
            confidence += factor;
        }
    }
}
