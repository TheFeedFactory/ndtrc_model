package nl.ithelden.model.ndtrc

import com.fasterxml.jackson.annotation.JsonProperty
import groovy.transform.ToString

@ToString(includeNames = true)
class TRCItemDetail {
    @JsonProperty String lang
    @JsonProperty String longdescription
    @JsonProperty String shortdescription
    @JsonProperty String title
}