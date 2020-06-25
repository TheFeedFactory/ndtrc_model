package nl.ithelden.model

import com.fasterxml.jackson.annotation.JsonProperty
import groovy.transform.ToString
import nl.ithelden.model.ndtrc.TRCItem
import org.joda.time.DateTime

@ToString(includeNames = true)
class ConvertedEntry {
    @JsonProperty String label
    @JsonProperty DateTime created // http://purl.org/dc/terms/created
    @JsonProperty DateTime modified // http://purl.org/dc/terms/modified

    @JsonProperty String externalId
    @JsonProperty TRCItem trcItem
}