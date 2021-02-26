package nl.ithelden.model.ndtrc

import com.fasterxml.jackson.annotation.JsonProperty
import groovy.transform.ToString

@ToString(includeNames = true)
class GISCoordinate {
    @JsonProperty String xcoordinate
    @JsonProperty String ycoordinate
    @JsonProperty String label
}
