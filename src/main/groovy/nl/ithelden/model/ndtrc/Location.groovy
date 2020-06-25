package nl.ithelden.model.ndtrc

import com.fasterxml.jackson.annotation.JsonProperty
import groovy.transform.ToString

@ToString(includeNames = true)
class Location {
    @JsonProperty Address address
    @JsonProperty String label
    @JsonProperty LocationItem locationItem

    @ToString(includeNames = true)
    static class LocationItem {
        @JsonProperty String trcid
        @JsonProperty String text
    }
}
