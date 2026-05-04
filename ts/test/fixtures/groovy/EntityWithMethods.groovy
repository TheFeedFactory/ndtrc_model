package nl.ithelden.model.ndtrc

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonProperty
import groovy.transform.ToString

@ToString(includeNames = true)
class EntityWithMethods {
    @JsonProperty String name
    @JsonProperty String country = 'NL'
    @Deprecated @JsonProperty List<String> tags = []
    Translations translations = new Translations()

    @JsonIgnore
    boolean isEmpty() {
        return name == null
    }

    void normalize() {
        if (name) {
            name = name.trim()
        }
    }

    static String helper(String input) {
        return input?.toLowerCase()
    }
}
