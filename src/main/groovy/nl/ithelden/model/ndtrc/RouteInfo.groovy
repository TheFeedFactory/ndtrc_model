package nl.ithelden.model.ndtrc

import groovy.transform.ToString

@ToString(includeNames = true)
 class RouteInfo {
    RouteInfo.Type type
    String url
    Double distanceInKilometers
    Integer durationInMinutes

    static enum Type {
        route_maker
    }
}
