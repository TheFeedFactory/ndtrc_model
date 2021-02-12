package nl.ithelden.model

import com.fasterxml.jackson.annotation.JsonProperty
import groovy.transform.ToString
import org.joda.time.DateTime

@ToString
class FetchedEntry {
    @JsonProperty String feed
    @JsonProperty String sourceId
    @JsonProperty String label
    @JsonProperty DateTime created // http://purl.org/dc/terms/created
    @JsonProperty DateTime modified // http://purl.org/dc/terms/modified

    @JsonProperty String externalId
    @JsonProperty Map<String, Object> data
    @JsonProperty ExternalLocationInfo externalLocationInfo

    @ToString
    public static class ExternalLocationInfo {
        @JsonProperty String externalId // location id used in feeds
        @JsonProperty String locationName //name of location in original feed
        @JsonProperty String address
        @JsonProperty String housenr
        @JsonProperty String zipcode
        @JsonProperty String city
        @JsonProperty String latitude
        @JsonProperty String longitude
        @JsonProperty String trcid // trcid as known in the TRC
    }
}