package nl.ithelden.model.ndtrc

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import groovy.transform.ToString
import nl.ithelden.model.util.StringUtils

@ToString(includeNames = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
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