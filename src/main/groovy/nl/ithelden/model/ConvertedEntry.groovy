package nl.ithelden.model


import groovy.transform.ToString
import nl.ithelden.model.ndtrc.TRCItem
import org.joda.time.DateTime

@ToString(includeNames = true)
class ConvertedEntry {
    String label
    DateTime created // http://purl.org/dc/terms/created
    DateTime modified // http://purl.org/dc/terms/modified

    String externalId
    TRCItem trcItem
}