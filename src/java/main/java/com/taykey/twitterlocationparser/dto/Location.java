package com.taykey.twitterlocationparser.dto;

public class Location {

    private String name;

    private String alternateNames;

    private String stateCode;

    private String countryCode;

    private LocationType type;

    private int population;

    private double latitude;
    private double longitude;

    public Location() {
    }

    public Location(String name, String alternateNames, String countryCode,
                    String stateCode, LocationType type, int population,
                    double latitude, double longitude) {
        this.name = name;
        this.alternateNames = alternateNames;
        this.countryCode = countryCode;
        this.stateCode = stateCode;
        this.type = type;
        this.population = population;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAlternateNames() {
        return this.alternateNames;
    }

    public void setAlternateNames(String alternateNames) {
        this.alternateNames = alternateNames;
    }

    public String getStateCode() {
        return this.stateCode;
    }

    public void setStateCode(String stateCode) {
        this.stateCode = stateCode;
    }

    public String getCountryCode() {
        return this.countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public LocationType getType() {
        return this.type;
    }

    public void setType(LocationType type) {
        this.type = type;
    }

    public int getPopulation() {
        return this.population;
    }

    public double getLatitude() {
        return this.latitude;
    }

    public double getLongitude() {
        return this.longitude;
    }

    public void setPopulation(int population) {
        this.population = population;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime
                * result
                + ((this.alternateNames == null) ? 0 : this.alternateNames
                        .hashCode());
        result = prime
                * result
                + ((this.countryCode == null) ? 0 : this.countryCode.hashCode());
        result = prime * result
                + ((this.name == null) ? 0 : this.name.hashCode());
        result = prime * result + this.population;
        result = prime * result
                + ((this.stateCode == null) ? 0 : this.stateCode.hashCode());
        result = prime * result
                + ((this.type == null) ? 0 : this.type.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (!(obj instanceof Location))
            return false;
        Location other = (Location) obj;
        if (this.alternateNames == null) {
            if (other.alternateNames != null)
                return false;
        } else if (!this.alternateNames.equals(other.alternateNames))
            return false;
        if (this.countryCode == null) {
            if (other.countryCode != null)
                return false;
        } else if (!this.countryCode.equals(other.countryCode))
            return false;
        if (this.name == null) {
            if (other.name != null)
                return false;
        } else if (!this.name.equals(other.name))
            return false;
        if (this.population != other.population)
            return false;
        if (this.stateCode == null) {
            if (other.stateCode != null)
                return false;
        } else if (!this.stateCode.equals(other.stateCode))
            return false;
        if (this.type != other.type)
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "Location ["
                + (this.name != null ? "name=" + this.name + ", " : "")
                + (this.alternateNames != null ? "alternateNames="
                        + this.alternateNames + ", " : "")
                + (this.stateCode != null ? "stateCode=" + this.stateCode
                        + ", " : "")
                + (this.countryCode != null ? "countryCode=" + this.countryCode
                        + ", " : "")
                + (this.type != null ? "type=" + this.type + ", " : "")
                + "population=" + this.population + "]";
    }

}
