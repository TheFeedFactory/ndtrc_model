package nl.ithelden.model.ndtrc

import com.fasterxml.jackson.annotation.JsonProperty

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

    @JsonProperty List<GISCoordinate> gisCoordinates = []
}
