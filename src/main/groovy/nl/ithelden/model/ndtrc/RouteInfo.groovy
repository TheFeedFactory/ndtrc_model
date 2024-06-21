package nl.ithelden.model.ndtrc

import groovy.transform.ToString

@ToString(includeNames = true)
 class RouteInfo {
    Type type
    String url
    Double distanceInKilometers
    Integer durationInMinutes
    Address start, end

    static enum Type {
        route_maker
    }
}