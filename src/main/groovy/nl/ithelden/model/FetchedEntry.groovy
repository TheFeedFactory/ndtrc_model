package nl.ithelden.model


import groovy.transform.ToString
import org.joda.time.DateTime

@ToString
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

    @ToString
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
    }
}