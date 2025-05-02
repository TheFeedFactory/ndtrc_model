package nl.ithelden.model


import groovy.transform.ToString
import org.joda.time.DateTime

/**
 * Represents an entry fetched from an external feed.
 * Contains information about the source feed, identifiers, timestamps,
 * the raw data, and potentially structured location information.
 * Can also include an error message if fetching failed.
 */
@ToString(includeNames = true)
class FetchedEntry {
    String feed
    String sourceId
    String sourceUrl
    String errorMessage

    String label
    DateTime created // http://purl.org/dc/terms/created
    DateTime modified // http://purl.org/dc/terms/modified

    String externalId
    Map<String, Object> data
    ExternalLocationInfo externalLocationInfo

    /**
     * Holds structured information about the location associated with the fetched entry,
     * including various identifiers (external, TRC, Feed Factory) and address details.
     */
    @ToString(includeNames = true)
    public static class ExternalLocationInfo {
        String externalId // location id used in feeds
        String locationName //name of location in original feed
        String address
        String housenr
        String zipcode
        String city
        String latitude
        String longitude
        String trcid // trcid as known in the TRC
        String ffId // location id in FF
    }
}