package nl.ithelden.model


import groovy.transform.ToString
import nl.ithelden.model.ndtrc.TRCItem
import org.joda.time.DateTime

/**
 * Represents an entry that has been converted, potentially from an external source.
 * Contains metadata like creation and modification timestamps, an external identifier,
 * and the associated TRCItem. It can also hold an error message if conversion failed.
 */
@ToString(includeNames = true)
class ConvertedEntry {
    String label
    DateTime created // http://purl.org/dc/terms/created
    DateTime modified // http://purl.org/dc/terms/modified
    String errorMessage

    String externalId
    TRCItem trcItem
}