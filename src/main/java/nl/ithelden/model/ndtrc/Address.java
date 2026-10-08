package nl.ithelden.model.ndtrc;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import nl.ithelden.model.util.StringUtils;
import nl.ithelden.model.util.ToStringBuilder;

/**
 * Represents a postal address, including street, house number, city, zip code, country, continent,
 * province, neighborhood, district, GIS coordinates, and associated TRC IDs. Includes flags for
 * main/reservation addresses and methods for checking emptiness and normalizing formats
 * (zip code, city name).
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Address {
    private static final Pattern WORD = Pattern.compile("(\\w)(\\w*)");

    @JsonProperty private Boolean main;
    @JsonProperty private Boolean reservation;

    @JsonProperty private String title;
    @JsonProperty private String city;
    @JsonProperty private String citytrcid;

    @JsonProperty private String country = "NL";

    /**
     * Continent the address is on, as a two-letter code: AF, AS, EU, NA, OC or SA.
     * Antarctica has no code here because no country in the picker sits on it — ff-gui's
     * continentHelper (ContinentCode) derives this value from country and emits these six.
     *
     * Deliberately a free-form String rather than an enum so unexpected values in an incoming
     * feed cannot break deserialisation, and so consumers can curate their own option list.
     * Null means unknown — it is not defaulted from {@link #country}, because a wrong continent
     * is worse than a missing one for the country/continent filters that consume this.
     */
    @JsonProperty private String continent;

    @JsonProperty private String housenr;
    @JsonProperty private String street;
    @JsonProperty private String streettrcid;

    @JsonProperty private String zipcode;
    @JsonProperty private String province;
    @JsonProperty private String neighbourhood;
    @JsonProperty private String district;

    @JsonProperty private List<GISCoordinate> gisCoordinates = new ArrayList<>();

    public Boolean getMain() {
        return main;
    }

    public Boolean isMain() {
        return main;
    }

    public void setMain(Boolean main) {
        this.main = main;
    }

    public Boolean getReservation() {
        return reservation;
    }

    public Boolean isReservation() {
        return reservation;
    }

    public void setReservation(Boolean reservation) {
        this.reservation = reservation;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCitytrcid() {
        return citytrcid;
    }

    public void setCitytrcid(String citytrcid) {
        this.citytrcid = citytrcid;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getContinent() {
        return continent;
    }

    public void setContinent(String continent) {
        this.continent = continent;
    }

    public String getHousenr() {
        return housenr;
    }

    public void setHousenr(String housenr) {
        this.housenr = housenr;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getStreettrcid() {
        return streettrcid;
    }

    public void setStreettrcid(String streettrcid) {
        this.streettrcid = streettrcid;
    }

    public String getZipcode() {
        return zipcode;
    }

    public void setZipcode(String zipcode) {
        this.zipcode = zipcode;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getNeighbourhood() {
        return neighbourhood;
    }

    public void setNeighbourhood(String neighbourhood) {
        this.neighbourhood = neighbourhood;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public List<GISCoordinate> getGisCoordinates() {
        return gisCoordinates;
    }

    public void setGisCoordinates(List<GISCoordinate> gisCoordinates) {
        this.gisCoordinates = gisCoordinates;
    }

    @JsonIgnore
    public boolean isEmpty() {
        if (!(StringUtils.isEmpty(title) && StringUtils.isEmpty(city) &&
               StringUtils.isEmpty(housenr) && StringUtils.isEmpty(street) &&
               StringUtils.isEmpty(zipcode) && StringUtils.isEmpty(province))) {
            return false;
        }
        // Groovy truth of the 1.x code: a null list counts as empty, like an empty one
        if (gisCoordinates == null || gisCoordinates.isEmpty()) {
            return true;
        }
        for (GISCoordinate coordinate : gisCoordinates) {
            if (!coordinate.isEmpty()) {
                return false;
            }
        }
        return true;
    }

    public void normaliseAdresItems() {
        // zipcodes should be uppercase and in the format of
        // 1234 AB
        // so convert 1234ab to 1234 AB and 1234AB to 1234 AB

        if (zipcode != null && !zipcode.isEmpty()) {
            // test if zipcode matches /(\d{4})(\w{2})/
            if (zipcode.matches("(\\d{4})\\s*(\\w{2})")) {
                // if so, convert to uppercase and add a space between the numbers and letters
                zipcode = zipcode.toUpperCase().replaceAll("(\\d{4})\\s*(\\w{2})", "$1 $2");
            }
        }

        // cityname should start with a capital letter and the rest should be lowercase
        // but prevent changing format for city names like "Den Haag" and "Nieuw-Lekkerland"
        if (city != null && !city.isEmpty()) {
            // only change if it is either all lowercase or all uppercase
            if (city.matches("^[a-z\\s]+$") || city.matches("^[A-Z\\s]+$")) {
                Matcher words = WORD.matcher(city.toLowerCase());
                city = words.replaceAll(word -> Matcher.quoteReplacement(word.group(1).toUpperCase() + word.group(2)));

                // IJ is one Dutch letter written as two characters, and it capitalises
                // as a pair: IJmuiden, IJsselstein, Oude IJsselstreek. Capitalising only
                // the I spells the name wrong, and a wrong spelling written by a
                // normaliser is worse than the unnormalised value it replaced, because
                // nothing downstream can tell it was not typed that way. Only at the
                // start of a word, so that Nijmegen and Wijhe are left alone.
                city = city.replaceAll("(^|\\s)Ij", "$1IJ");
            }
        }
    }

    @Override
    public String toString() {
        return new ToStringBuilder(Address.class, this)
                .add("main", main)
                .add("reservation", reservation)
                .add("title", title)
                .add("city", city)
                .add("citytrcid", citytrcid)
                .add("country", country)
                .add("continent", continent)
                .add("housenr", housenr)
                .add("street", street)
                .add("streettrcid", streettrcid)
                .add("zipcode", zipcode)
                .add("province", province)
                .add("neighbourhood", neighbourhood)
                .add("district", district)
                .add("gisCoordinates", gisCoordinates)
                .add("empty", isEmpty())
                .build();
    }
}
