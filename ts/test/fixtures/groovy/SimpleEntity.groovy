package nl.ithelden.model.ndtrc

import com.fasterxml.jackson.annotation.JsonProperty
import groovy.transform.ToString

@ToString(includeNames = true)
class SimpleEntity {
    @JsonProperty String name
    @JsonProperty Integer count
    Boolean active
}
