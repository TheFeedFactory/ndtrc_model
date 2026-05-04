package nl.ithelden.model.ndtrc

import groovy.transform.ToString

@ToString(includeNames = true)
class EntityWithEnums {
    String label
    Status status
    DetailType detailType

    enum Status { active, inactive, archived }

    static enum DetailType {
        simple,
        complex,
        composite
    }

    @ToString(includeNames = true)
    static class Detail {
        String lang, text
    }
}
