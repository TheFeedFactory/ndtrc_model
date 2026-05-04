package nl.ithelden.model.ndtrc

import groovy.transform.ToString

@ToString(includeNames = true)
class MultiFieldEntity {
    Double lat, lng
    String label
    Address start, end

    String eventRelativeDuration;
}
