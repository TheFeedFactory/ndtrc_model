package nl.ithelden.model.ndtrc

import com.fasterxml.jackson.annotation.JsonProperty
import groovy.transform.ToString

@ToString(includeNames = true)
class NestedEntity {
    @JsonProperty String id
    @JsonProperty List<Inner> items = []

    @ToString(includeNames = true)
    static class Inner {
        @JsonProperty String value
        @JsonProperty List<Deep> nested = []

        static class Deep {
            String key
        }
    }
}
