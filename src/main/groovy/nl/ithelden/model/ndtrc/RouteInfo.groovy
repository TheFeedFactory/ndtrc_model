package nl.ithelden.model.ndtrc

import groovy.transform.ToString

/**
 * Contains information about a route, including its type, URL, distance,
 * duration, and start/end addresses.
 */
@ToString(includeNames = true)
 class RouteInfo {
    Type type
    String url
    Double distanceInKilometers
    Integer durationInMinutes
    Address start, end

    static enum Type {
        route_maker,
        route_iq,
        odp_routes
    }
}