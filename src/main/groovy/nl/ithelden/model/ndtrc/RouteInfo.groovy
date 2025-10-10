package nl.ithelden.model.ndtrc

import groovy.transform.ToString

/**
 * Contains information about a route, including its type, URL, distance,
 * duration, and start/end addresses.
 */
@ToString(includeNames = true)
class RouteInfo {
    // Route identification
    Type type
    RouteType routeType // determines the external source of the route
    String url // URL to access the route in external system

    // Basic route metrics
    Double distanceInKilometers
    Integer durationInMinutes
    Address start, end

    // Route points
    List<Poi> pois = []
    List<LatLng> routeCoordinates = []     // Points added by user
    List<LatLng> calculatedCoordinates = [] // Routing from mapbox

    // Route metadata
    RouteDifficulty difficulty           // Route difficulty level
    SurfaceType primarySurface           // Main surface type

    static enum RouteType {
        // Mapbox API routing profiles
        driving_traffic,  // Driving with real-time traffic data
        driving,          // Driving without traffic data
        walking,          // Pedestrian navigation
        cycling,          // Bicycle navigation
    }

    static enum Type {
        eventConnectors,
        route_maker,
        route_iq,
        odp_routes,
        other
    }

    static enum RouteDifficulty {
        easy,           // Suitable for all fitness levels
        moderate,       // Requires basic fitness
        challenging,    // Requires good fitness
        difficult,      // Requires excellent fitness
        expert          // Technical/extreme routes
    }

    static enum SurfaceType {
        paved,          // Asphalt/concrete
        gravel,         // Gravel paths
        dirt,           // Dirt roads/trails
        sand,           // Sandy surfaces
        grass,          // Grass paths
        cobblestone,    // Cobblestones
        boardwalk,      // Wooden walkways
        rock,           // Rocky terrain
        snow,           // Snow covered
        water,          // Water routes
        mixed           // Multiple surfaces
    }

    @ToString(includeNames = true)
    static class Poi {
        // Position along route
        Double distanceInKilometersFromStart
        Integer durationInMinutesFromStart
        LatLng coordinate

        // POI identification
        String label
        String icon              // Maki icon name (e.g., "museum", "restaurant", "park")
        PoiCategory category

        // Content from TRC system
        List<TRCItemDetail> trcItemDetails
        List<File> files

        // Additional metadata
        Calendar calendar  // object describing opening hours
    }

    static enum PoiCategory {
        // Attractions
        museum, monument, castle, church, nature_area,
        // Facilities
        parking, toilet, rest_area, picnic_area,
        // Accommodation
        hotel, camping, hostel, bed_breakfast,
        // Food & Drink
        restaurant, cafe, bar, bakery,
        // Activities
        swimming, hiking_start, cycling_start, boat_rental,
        // Transport
        bus_stop, train_station, ferry, bike_rental,
        // Other
        viewpoint, information_point, other
    }

    @ToString(includeNames = true)
    static class LatLng {
        Double lat, lng
        Double altitude  // Elevation in meters (optional)
        String label

        // Validation method
        boolean isValid() {
            return lat != null && lng != null &&
                   lat >= -90 && lat <= 90 &&
                   lng >= -180 && lng <= 180
        }

        // Calculate distance to another point (Haversine formula)
        Double distanceTo(LatLng other) {
            if (!this.isValid() || !other?.isValid()) return null

            double R = 6371 // Earth's radius in kilometers
            double dLat = Math.toRadians(other.lat - this.lat)
            double dLng = Math.toRadians(other.lng - this.lng)
            double a = Math.sin(dLat/2) * Math.sin(dLat/2) +
                      Math.cos(Math.toRadians(this.lat)) *
                      Math.cos(Math.toRadians(other.lat)) *
                      Math.sin(dLng/2) * Math.sin(dLng/2)
            double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a))
            return R * c
        }
    }
}