package nl.ithelden.model.ndtrc

import com.fasterxml.jackson.annotation.JsonInclude
import groovy.transform.ToString

@ToString(includeNames = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
class Performer {
    String roleid
    String label
    String rolelabel
}