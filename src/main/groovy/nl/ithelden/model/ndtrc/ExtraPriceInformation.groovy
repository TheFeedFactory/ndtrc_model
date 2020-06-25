package nl.ithelden.model.ndtrc

import com.fasterxml.jackson.annotation.JsonProperty
import groovy.transform.ToString

@ToString(includeNames = true)
class ExtraPriceInformation {
    @JsonProperty String lang
    @JsonProperty String text
}