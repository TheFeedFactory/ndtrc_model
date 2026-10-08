package nl.ithelden.model.ndtrc

import com.fasterxml.jackson.annotation.JsonInclude
import groovy.transform.ToString

/**
 * Represents a performer associated with a TRC item,
 * including their role identifier, label (name), and role label.
 */
@ToString(includeNames = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
class Performer {
    String roleid
    String label
    String rolelabel
}