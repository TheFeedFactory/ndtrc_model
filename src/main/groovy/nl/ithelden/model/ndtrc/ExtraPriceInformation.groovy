package nl.ithelden.model.ndtrc

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import groovy.transform.ToString

/**
 * Provides additional pricing information in a specific language.
 */
@ToString(includeNames = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
class ExtraPriceInformation {
    @JsonProperty String lang
    @JsonProperty String text
}