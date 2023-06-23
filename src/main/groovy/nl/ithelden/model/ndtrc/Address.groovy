package nl.ithelden.model.ndtrc

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonProperty
import groovy.transform.ToString
import nl.ithelden.model.util.StringUtils

@ToString(includeNames = true)
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
}