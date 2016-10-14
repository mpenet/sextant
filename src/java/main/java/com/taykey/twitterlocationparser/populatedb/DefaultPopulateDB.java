package com.taykey.twitterlocationparser.populatedb;

import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.taykey.twitterlocationparser.common.IterableFile;
import com.taykey.twitterlocationparser.dao.LocationDao;
import com.taykey.twitterlocationparser.dto.Location;
import com.taykey.twitterlocationparser.dto.LocationType;

public class DefaultPopulateDB implements PopulateDB {

    private static Logger log = LoggerFactory
        .getLogger(DefaultPopulateDB.class);

    private LocationDao locationDao;

    private List<String> dataFiles;

    public DefaultPopulateDB(LocationDao locationDao) {
        this(locationDao, Arrays.asList("data/countries.tsv", "data/states.tsv",
                                        "data/cities.tsv"));
    }

    public DefaultPopulateDB(LocationDao locationDao, List<String> dataFiles) {
        this.locationDao = locationDao;
        this.dataFiles = dataFiles;
    }

    public void loadLocations(String dataFile) {
        log.debug("start loading file: {}", dataFile);
        int counter = 0;
        IterableFile iterator = new IterableFile(dataFile);
        for (String text : iterator) {
            counter++;
            String[] fields = text.split("\t");
            locationDao.addLocation(new Location(fields[0], fields[1],
                                                 fields[2], fields[3], LocationType.valueOf(fields[4]),
                                                 Integer.parseInt(fields[5]),
                                                 Double.parseDouble(fields[6]),
                                                 Double.parseDouble(fields[7])
                                                 ));
        }
        log.debug("done loading file: {}. added {} new records", dataFile,
                  counter);
    }

    @Override
    public void loadLocations() {
        for (String dataFile : dataFiles) {
            loadLocations(dataFile);
        }
    }

    public LocationDao getLocationDao() {
        return this.locationDao;
    }

    public void setLocationDao(LocationDao locationDao) {
        this.locationDao = locationDao;
    }

    public List<String> getDataFiles() {
        return this.dataFiles;
    }

    public void setDataFiles(List<String> dataFiles) {
        this.dataFiles = dataFiles;
    }
}
