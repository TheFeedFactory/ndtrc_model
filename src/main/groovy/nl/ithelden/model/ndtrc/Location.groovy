package nl.ithelden.model.ndtrc

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import groovy.transform.ToString

/**
 * Represents a location associated with a TRC item, including its address,
 * label, and a nested LocationItem with identifiers.
 */
@ToString(includeNames = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
class Location {
    @JsonProperty Address address
    @JsonProperty String label
    @JsonProperty LocationItem locationItem

    /**
     * Represents a specific item within a location, holding its ID, TRC ID, and text label.
     */
    @ToString(includeNames = true)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    static class LocationItem {
        @JsonProperty String id
        @JsonProperty String trcid
        @JsonProperty String text
    }
}
