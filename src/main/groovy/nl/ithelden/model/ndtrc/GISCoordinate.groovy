package nl.ithelden.model.ndtrc

import com.fasterxml.jackson.annotation.JsonProperty

class GISCoordinate {
    @JsonProperty String xcoordinate
    @JsonProperty String ycoordinate
    @JsonProperty String label
}
