package nl.ithelden.model.ndtrc

import com.fasterxml.jackson.annotation.JsonProperty
import groovy.transform.ToString

/**
 * Contains language-specific details for a TRC item,
 * including title, short description, and long description.
 */
@ToString(includeNames = true)
class TRCItemDetail {
    @JsonProperty String lang
    @JsonProperty String longdescription
    @JsonProperty String shortdescription
    @JsonProperty String title
}