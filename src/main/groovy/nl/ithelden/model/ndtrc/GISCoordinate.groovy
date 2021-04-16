package nl.ithelden.model.ndtrc

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonProperty
import groovy.transform.ToString
import nl.ithelden.model.util.StringUtils

@ToString(includeNames = true)
class GISCoordinate {
    @JsonProperty String xcoordinate
    @JsonProperty String ycoordinate
    @JsonProperty String label

    @JsonIgnore
    boolean isEmpty() {
        return StringUtils.isEmpty(xcoordinate) &&
               StringUtils.isEmpty(ycoordinate)
    }
}