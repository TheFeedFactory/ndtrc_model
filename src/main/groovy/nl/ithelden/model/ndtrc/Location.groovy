package nl.ithelden.model.ndtrc

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import groovy.transform.ToString

@ToString(includeNames = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
class Location {
    @JsonProperty Address address
    @JsonProperty String label
    @JsonProperty LocationItem locationItem

    @ToString(includeNames = true)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    static class LocationItem {
        @JsonProperty String id
        @JsonProperty String trcid
        @JsonProperty String text
    }
}
