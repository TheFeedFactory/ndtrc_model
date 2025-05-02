package nl.ithelden.model.ndtrc

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import groovy.transform.ToString
import nl.ithelden.model.util.StringUtils

/**
 * Represents a postal address, including street, house number, city, zip code, country, province,
 * neighborhood, district, GIS coordinates, and associated TRC IDs. Includes flags for main/reservation
 * addresses and methods for checking emptiness and normalizing formats (zip code, city name).
 */
@ToString(includeNames = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
class Address {
    @JsonProperty Boolean main
    @JsonProperty Boolean reservation

    @JsonProperty String title
    @JsonProperty String city
    @JsonProperty String citytrcid

    @JsonProperty String country = 'NL'
    @JsonProperty String housenr
    @JsonProperty String street
    @JsonProperty String streettrcid

    @JsonProperty String zipcode
    @JsonProperty String province
    @JsonProperty String neighbourhood
    @JsonProperty String district

    @JsonProperty List<GISCoordinate> gisCoordinates = []

    @JsonIgnore
    boolean isEmpty() {
        return StringUtils.isEmpty(title) && StringUtils.isEmpty(city) &&
               StringUtils.isEmpty(housenr) && StringUtils.isEmpty(street) &&
               StringUtils.isEmpty(zipcode) && StringUtils.isEmpty(province) &&
               (gisCoordinates?.isEmpty() || gisCoordinates.every { it.isEmpty() })
    }

    void normaliseAdresItems() {
        // zipcodes should be uppercase and in the format of
        // 1234 AB
        // so convert 1234ab to 1234 AB and 1234AB to 1234 AB

        if (zipcode) {
            // test if zipcode matches /(\d{4})(\w{2})/
            if (zipcode.matches(/(\d{4})\s*(\w{2})/)) {
                // if so, convert to uppercase and add a space between the numbers and letters
                zipcode = zipcode.toUpperCase().replaceAll(/(\d{4})\s*(\w{2})/, '$1 $2')
            }
        }

        // cityname should start with a capital letter and the rest should be lowercase
        // but prevent changing format for city names like "Den Haag" and "Nieuw-Lekkerland"
        if (city) {
            // only change if it is either all lowercase or all uppercase
            if (city.matches(/^[a-z\s]+$/) || city.matches(/^[A-Z\s]+$/)) {
                city = city.toLowerCase().replaceAll(/(\w)(\w*)/, { match, first, rest -> first.toUpperCase() + rest })
            }
        }
    }
}