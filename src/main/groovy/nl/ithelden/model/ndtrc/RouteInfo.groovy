package nl.ithelden.model.ndtrc

import groovy.transform.ToString

/**
 * Contains comprehensive information about a route, including its type, URL, distance,
 * duration, start/end addresses, points of interest, and coordinate data.
 *
 * <h3>Route Coordinates</h3>
 * A route consists of two types of coordinates:
 * <ul>
 *   <li><strong>routeCoordinates</strong> - Points manually plotted by the route editor on the map.
 *       These are the waypoints that define the intended path of the route. Each coordinate can
 *       have an optional label (e.g., "01", "14") meant to be displayed on the map as markers.</li>
 *   <li><strong>calculatedCoordinates</strong> - Detailed routing path calculated by a mapping
 *       service (e.g., Mapbox) based on the routeCoordinates. These form the actual turn-by-turn
 *       path that should be followed.</li>
 * </ul>
 * The routeCoordinates should contain enough information to recreate the calculatedCoordinates
 * using a mapping/routing service.
 *
 * <h3>Points of Interest (POIs)</h3>
 * POIs are locations worth visiting along or near the route. Key characteristics:
 * <ul>
 *   <li>POIs do not need to be exactly on the route - they should be in the vicinity but
 *       proximity is not strictly enforced</li>
 *   <li>Each POI follows a similar design pattern to TRCItem, containing:
 *     <ul>
 *       <li><strong>trcItemDetails</strong> - Provides title, short description, and long description</li>
 *       <li><strong>files</strong> - Allows attaching images, videos, and other media</li>
 *       <li><strong>label</strong> - Short text meant to be displayed on the POI icon on a map</li>
 *       <li><strong>icon</strong> - References a Font Awesome icon name. If not specified, the icon
 *           can be derived from the PoiCategory (e.g., "museum", "restaurant")</li>
 *       <li><strong>calendar</strong> - Contains opening hours information if applicable</li>
 *       <li><strong>category</strong> - Categorizes the POI type (e.g., museum, parking, restaurant)</li>
 *     </ul>
 *   </li>
 *   <li>POIs include position data (distanceInKilometersFromStart, durationInMinutesFromStart)
 *       to help users understand where along the route they are located</li>
 * </ul>
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
    Address start, end // start and end address, eg for parking, leave end empty if the same as start

    // Route points
    List<Poi> pois = []
    List<LatLng> routeCoordinates = []     // Points plotted by route editor - waypoints defining the intended path
    List<LatLng> calculatedCoordinates = [] // Detailed routing path calculated from routeCoordinates by mapping service (e.g., Mapbox)

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
        eventConnectors, // source is from Event Connectors
        route_maker, // source is from Route Maker
        route_iq,
        odp_routes, // source is from ODP or CityNavigator
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

    /**
     * Represents a Point of Interest (POI) along or near a route.
     *
     * <p>A POI is a location worth visiting that is in the vicinity of the route. POIs are not
     * required to be exactly on the route path - proximity is not strictly enforced.</p>
     *
     * <p><strong>Design Pattern:</strong> POIs follow a similar structure to TRCItem, allowing rich
     * content including titles, descriptions, and media attachments.</p>
     *
     * <h4>Display Elements</h4>
     * <ul>
     *   <li><strong>label</strong> - Short text (e.g., "Museum", "Café") displayed on the POI icon on a map</li>
     *   <li><strong>icon</strong> - Font Awesome icon name (e.g., "museum", "restaurant", "park").
     *       If not specified, an appropriate icon can be selected based on the PoiCategory</li>
     * </ul>
     *
     * <h4>Content and Media</h4>
     * <ul>
     *   <li><strong>trcItemDetails</strong> - Contains title, short description, and long description
     *       for the POI, following the same pattern as TRCItem</li>
     *   <li><strong>files</strong> - Allows attaching images, videos, and other media files to
     *       enhance the POI presentation</li>
     * </ul>
     *
     * <h4>Timing and Availability</h4>
     * <ul>
     *   <li><strong>calendar</strong> - Optional opening hours information. Use this when the POI
     *       has specific visiting hours (e.g., museums, restaurants)</li>
     * </ul>
     *
     * <h4>Cross-References</h4>
     * <ul>
     *   <li><strong>locationItem</strong> - Optional reference to an existing Location in FeedFactory.
     *       Only populated when this POI corresponds to a known location in the system, providing
     *       a cross-reference for additional location data</li>
     * </ul>
     */
    @ToString(includeNames = true)
    static class Poi {
        // Position along route
        Double distanceInKilometersFromStart  // How far from the route start point
        Integer durationInMinutesFromStart    // Estimated travel time from route start
        LatLng coordinate                      // GPS coordinates of the POI
        Location location                      // General location reference (empty if not registered in FF)

        // POI identification
        String label                           // Short text for display on map icon
        String icon                            // Font Awesome icon name (e.g., "museum", "restaurant", "park")
        PoiCategory category                   // Category determines POI type and default icon

        // Content from TRC system (similar to TRCItem structure)
        List<TRCItemDetail> trcItemDetails    // Title, short description, and long description
        List<File> files                       // Images, videos, and other media attachments

        // Additional metadata
        Calendar calendar                      // Opening hours information (if applicable)

        // Cross-reference to existing FeedFactory location (optional)
        Location.LocationItem locationItem     // Populated only if this POI is also registered as a Location in FeedFactory
    }

    /**
     * Categories for POIs that help classify the type of location.
     * Each category suggests a default Font Awesome icon if no explicit icon is specified.
     *
     * <h4>Suggested Font Awesome Icons by Category:</h4>
     * <ul>
     *   <li><strong>Attractions:</strong>
     *     <ul>
     *       <li>museum - "museum", "building-columns"</li>
     *       <li>monument - "monument", "landmark"</li>
     *       <li>castle - "chess-rook", "fort-awesome"</li>
     *       <li>church - "church", "place-of-worship"</li>
     *       <li>nature_area - "tree", "leaf", "mountain"</li>
     *     </ul>
     *   </li>
     *   <li><strong>Facilities:</strong>
     *     <ul>
     *       <li>parking - "square-parking", "p"</li>
     *       <li>toilet - "restroom", "toilet"</li>
     *       <li>rest_area - "couch", "chair"</li>
     *       <li>picnic_area - "utensils", "basket-shopping"</li>
     *     </ul>
     *   </li>
     *   <li><strong>Accommodation:</strong>
     *     <ul>
     *       <li>hotel - "hotel", "bed"</li>
     *       <li>camping - "campground", "caravan"</li>
     *       <li>hostel - "bed-bunk", "house-user"</li>
     *       <li>bed_breakfast - "house", "bed"</li>
     *     </ul>
     *   </li>
     *   <li><strong>Food & Drink:</strong>
     *     <ul>
     *       <li>restaurant - "utensils", "plate-wheat"</li>
     *       <li>cafe - "mug-hot", "coffee"</li>
     *       <li>bar - "wine-glass", "beer-mug-empty"</li>
     *       <li>bakery - "bread-slice", "croissant"</li>
     *     </ul>
     *   </li>
     *   <li><strong>Activities:</strong>
     *     <ul>
     *       <li>swimming - "person-swimming", "water"</li>
     *       <li>hiking_start - "person-hiking", "boot"</li>
     *       <li>cycling_start - "person-biking", "bicycle"</li>
     *       <li>boat_rental - "ferry", "sailboat"</li>
     *     </ul>
     *   </li>
     *   <li><strong>Transport:</strong>
     *     <ul>
     *       <li>bus_stop - "bus", "bus-simple"</li>
     *       <li>train_station - "train", "train-subway"</li>
     *       <li>ferry - "ferry", "ship"</li>
     *       <li>bike_rental - "bicycle", "person-biking"</li>
     *     </ul>
     *   </li>
     *   <li><strong>Other:</strong>
     *     <ul>
     *       <li>viewpoint - "binoculars", "mountain-sun"</li>
     *       <li>information_point - "circle-info", "info"</li>
     *       <li>other - "location-dot", "map-pin"</li>
     *     </ul>
     *   </li>
     * </ul>
     */
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

    /**
     * Represents a geographic coordinate with optional altitude and label.
     *
     * <p>This class is used for both route waypoints and POI locations.</p>
     *
     * <p><strong>Route Coordinates Usage:</strong> When used in routeCoordinates, the optional
     * label field can contain a short identifier (e.g., "01", "14") that is displayed on the map
     * as a marker. This helps users identify specific waypoints along the route.</p>
     *
     * <p>Coordinates must be valid (latitude: -90 to 90, longitude: -180 to 180) and the class
     * provides validation and distance calculation methods.</p>
     */
    @ToString(includeNames = true)
    static class LatLng {
        Double lat, lng
        Double altitude  // Elevation in meters (optional)
        String label     // Optional short identifier for display on map (e.g., "01", "14")

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