package nl.ithelden.model.ndtrc;

import java.util.ArrayList;
import java.util.List;
import nl.ithelden.model.util.ToStringBuilder;

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
 *   <li><strong>calculatedCoordinates</strong> - Detailed routing path calculated from the
 *       routeCoordinates. For every RouteType except {@code boating} this is a turn-by-turn path
 *       from a mapping service (e.g., Mapbox); for {@code boating} it is simply routeCoordinates
 *       joined by straight lines, since no routing service can route across open water.</li>
 * </ul>
 * The routeCoordinates should contain enough information to recreate the calculatedCoordinates
 * using a mapping/routing service (or, for {@code boating}, by connecting them directly).
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
public class RouteInfo {
    // Route identification
    private Type type;
    private RouteType routeType; // determines the external source of the route
    private String url; // URL to access the route in external system

    // Basic route metrics
    private Double distanceInKilometers;
    private Integer durationInMinutes;
    private Address start; // start and end address, eg for parking, leave end empty if the same as start
    private Address end;

    // Route points
    private List<Poi> pois = new ArrayList<>();
    private List<LatLng> routeCoordinates = new ArrayList<>();     // Points plotted by route editor - waypoints defining the intended path
    private List<LatLng> calculatedCoordinates = new ArrayList<>(); // Detailed routing path: from a mapping service (e.g., Mapbox) for every
                                             // RouteType except boating, where it's routeCoordinates joined by straight lines

    // Route metadata
    private RouteDifficulty difficulty;           // Route difficulty level
    private SurfaceType primarySurface;           // Main surface type

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public RouteType getRouteType() {
        return routeType;
    }

    public void setRouteType(RouteType routeType) {
        this.routeType = routeType;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Double getDistanceInKilometers() {
        return distanceInKilometers;
    }

    public void setDistanceInKilometers(Double distanceInKilometers) {
        this.distanceInKilometers = distanceInKilometers;
    }

    public Integer getDurationInMinutes() {
        return durationInMinutes;
    }

    public void setDurationInMinutes(Integer durationInMinutes) {
        this.durationInMinutes = durationInMinutes;
    }

    public Address getStart() {
        return start;
    }

    public void setStart(Address start) {
        this.start = start;
    }

    public Address getEnd() {
        return end;
    }

    public void setEnd(Address end) {
        this.end = end;
    }

    public List<Poi> getPois() {
        return pois;
    }

    public void setPois(List<Poi> pois) {
        this.pois = pois;
    }

    public List<LatLng> getRouteCoordinates() {
        return routeCoordinates;
    }

    public void setRouteCoordinates(List<LatLng> routeCoordinates) {
        this.routeCoordinates = routeCoordinates;
    }

    public List<LatLng> getCalculatedCoordinates() {
        return calculatedCoordinates;
    }

    public void setCalculatedCoordinates(List<LatLng> calculatedCoordinates) {
        this.calculatedCoordinates = calculatedCoordinates;
    }

    public RouteDifficulty getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(RouteDifficulty difficulty) {
        this.difficulty = difficulty;
    }

    public SurfaceType getPrimarySurface() {
        return primarySurface;
    }

    public void setPrimarySurface(SurfaceType primarySurface) {
        this.primarySurface = primarySurface;
    }

    public static enum RouteType {
        // Mapbox API routing profiles
        driving_traffic,  // Driving with real-time traffic data
        driving,          // Driving without traffic data
        walking,          // Pedestrian navigation
        cycling,          // Bicycle navigation
        boating,          // Water navigation - not a Mapbox profile; calculatedCoordinates is a
                          // straight line between routeCoordinates instead of a Directions API call
        horse_riding,     // Horse riding - not a Mapbox profile either; routed with the walking profile,
                          // the closest Mapbox has to a bridle path
        ;

        // Groovy gives every enum these members; kept so the 1.x API is unchanged.
        public static final RouteType MIN_VALUE = driving_traffic;
        public static final RouteType MAX_VALUE = horse_riding;

        public RouteType next() {
            RouteType[] values = values();
            int ordinal = ordinal() + 1;
            return values[ordinal >= values.length ? 0 : ordinal];
        }

        public RouteType previous() {
            RouteType[] values = values();
            int ordinal = ordinal() - 1;
            return values[ordinal < 0 ? values.length - 1 : ordinal];
        }
    }

    public static enum Type {
        eventConnectors, // source is from Event Connectors
        route_maker, // source is from Route Maker
        route_iq,
        odp_routes, // source is from ODP or CityNavigator
        other;

        // Groovy gives every enum these members; kept so the 1.x API is unchanged.
        public static final Type MIN_VALUE = eventConnectors;
        public static final Type MAX_VALUE = other;

        public Type next() {
            Type[] values = values();
            int ordinal = ordinal() + 1;
            return values[ordinal >= values.length ? 0 : ordinal];
        }

        public Type previous() {
            Type[] values = values();
            int ordinal = ordinal() - 1;
            return values[ordinal < 0 ? values.length - 1 : ordinal];
        }
    }

    public static enum RouteDifficulty {
        easy,           // Suitable for all fitness levels
        moderate,       // Requires basic fitness
        challenging,    // Requires good fitness
        difficult,      // Requires excellent fitness
        expert;         // Technical/extreme routes

        // Groovy gives every enum these members; kept so the 1.x API is unchanged.
        public static final RouteDifficulty MIN_VALUE = easy;
        public static final RouteDifficulty MAX_VALUE = expert;

        public RouteDifficulty next() {
            RouteDifficulty[] values = values();
            int ordinal = ordinal() + 1;
            return values[ordinal >= values.length ? 0 : ordinal];
        }

        public RouteDifficulty previous() {
            RouteDifficulty[] values = values();
            int ordinal = ordinal() - 1;
            return values[ordinal < 0 ? values.length - 1 : ordinal];
        }
    }

    public static enum SurfaceType {
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
        mixed;          // Multiple surfaces

        // Groovy gives every enum these members; kept so the 1.x API is unchanged.
        public static final SurfaceType MIN_VALUE = paved;
        public static final SurfaceType MAX_VALUE = mixed;

        public SurfaceType next() {
            SurfaceType[] values = values();
            int ordinal = ordinal() + 1;
            return values[ordinal >= values.length ? 0 : ordinal];
        }

        public SurfaceType previous() {
            SurfaceType[] values = values();
            int ordinal = ordinal() - 1;
            return values[ordinal < 0 ? values.length - 1 : ordinal];
        }
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
    public static class Poi {
        // Position along route
        private Double distanceInKilometersFromStart;  // How far from the route start point
        private Integer durationInMinutesFromStart;    // Estimated travel time from route start
        private LatLng coordinate;                      // GPS coordinates of the POI
        private Location location;                      // General location reference (empty if not registered in FF)

        // POI identification
        private String label;                           // Short text for display on map icon
        private String icon;                            // Font Awesome icon name (e.g., "museum", "restaurant", "park")
        private PoiCategory category;                   // Category determines POI type and default icon

        // Content from TRC system (similar to TRCItem structure)
        private List<TRCItemDetail> trcItemDetails;    // Title, short description, and long description
        private List<File> files;                       // Images, videos, and other media attachments

        // Additional metadata
        private Calendar calendar;                      // Opening hours information (if applicable)

        // Cross-reference to existing FeedFactory location (optional)
        private Location.LocationItem locationItem;     // Populated only if this POI is also registered as a Location in FeedFactory

        public Double getDistanceInKilometersFromStart() {
            return distanceInKilometersFromStart;
        }

        public void setDistanceInKilometersFromStart(Double distanceInKilometersFromStart) {
            this.distanceInKilometersFromStart = distanceInKilometersFromStart;
        }

        public Integer getDurationInMinutesFromStart() {
            return durationInMinutesFromStart;
        }

        public void setDurationInMinutesFromStart(Integer durationInMinutesFromStart) {
            this.durationInMinutesFromStart = durationInMinutesFromStart;
        }

        public LatLng getCoordinate() {
            return coordinate;
        }

        public void setCoordinate(LatLng coordinate) {
            this.coordinate = coordinate;
        }

        public Location getLocation() {
            return location;
        }

        public void setLocation(Location location) {
            this.location = location;
        }

        public String getLabel() {
            return label;
        }

        public void setLabel(String label) {
            this.label = label;
        }

        public String getIcon() {
            return icon;
        }

        public void setIcon(String icon) {
            this.icon = icon;
        }

        public PoiCategory getCategory() {
            return category;
        }

        public void setCategory(PoiCategory category) {
            this.category = category;
        }

        public List<TRCItemDetail> getTrcItemDetails() {
            return trcItemDetails;
        }

        public void setTrcItemDetails(List<TRCItemDetail> trcItemDetails) {
            this.trcItemDetails = trcItemDetails;
        }

        public List<File> getFiles() {
            return files;
        }

        public void setFiles(List<File> files) {
            this.files = files;
        }

        public Calendar getCalendar() {
            return calendar;
        }

        public void setCalendar(Calendar calendar) {
            this.calendar = calendar;
        }

        public Location.LocationItem getLocationItem() {
            return locationItem;
        }

        public void setLocationItem(Location.LocationItem locationItem) {
            this.locationItem = locationItem;
        }

        @Override
        public String toString() {
            return new ToStringBuilder(Poi.class, this)
                    .add("distanceInKilometersFromStart", distanceInKilometersFromStart)
                    .add("durationInMinutesFromStart", durationInMinutesFromStart)
                    .add("coordinate", coordinate)
                    .add("location", location)
                    .add("label", label)
                    .add("icon", icon)
                    .add("category", category)
                    .add("trcItemDetails", trcItemDetails)
                    .add("files", files)
                    .add("calendar", calendar)
                    .add("locationItem", locationItem)
                    .build();
        }
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
    public static enum PoiCategory {
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
        viewpoint, information_point, other;

        // Groovy gives every enum these members; kept so the 1.x API is unchanged.
        public static final PoiCategory MIN_VALUE = museum;
        public static final PoiCategory MAX_VALUE = other;

        public PoiCategory next() {
            PoiCategory[] values = values();
            int ordinal = ordinal() + 1;
            return values[ordinal >= values.length ? 0 : ordinal];
        }

        public PoiCategory previous() {
            PoiCategory[] values = values();
            int ordinal = ordinal() - 1;
            return values[ordinal < 0 ? values.length - 1 : ordinal];
        }
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
    public static class LatLng {
        private Double lat;
        private Double lng;
        private Double altitude;  // Elevation in meters (optional)
        private String label;     // Optional short identifier for display on map (e.g., "01", "14")

        public Double getLat() {
            return lat;
        }

        public void setLat(Double lat) {
            this.lat = lat;
        }

        public Double getLng() {
            return lng;
        }

        public void setLng(Double lng) {
            this.lng = lng;
        }

        public Double getAltitude() {
            return altitude;
        }

        public void setAltitude(Double altitude) {
            this.altitude = altitude;
        }

        public String getLabel() {
            return label;
        }

        public void setLabel(String label) {
            this.label = label;
        }

        // Validation method
        public boolean isValid() {
            return lat != null && lng != null &&
                   lat >= -90 && lat <= 90 &&
                   lng >= -180 && lng <= 180;
        }

        // Calculate distance to another point (Haversine formula)
        public Double distanceTo(LatLng other) {
            if (!this.isValid() || other == null || !other.isValid()) return null;

            double R = 6371; // Earth's radius in kilometers
            double dLat = Math.toRadians(other.lat - this.lat);
            double dLng = Math.toRadians(other.lng - this.lng);
            double a = Math.sin(dLat/2) * Math.sin(dLat/2) +
                      Math.cos(Math.toRadians(this.lat)) *
                      Math.cos(Math.toRadians(other.lat)) *
                      Math.sin(dLng/2) * Math.sin(dLng/2);
            double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
            return R * c;
        }

        @Override
        public String toString() {
            return new ToStringBuilder(LatLng.class, this)
                    .add("lat", lat)
                    .add("lng", lng)
                    .add("altitude", altitude)
                    .add("label", label)
                    .add("valid", isValid())
                    .build();
        }
    }

    @Override
    public String toString() {
        return new ToStringBuilder(RouteInfo.class, this)
                .add("type", type)
                .add("routeType", routeType)
                .add("url", url)
                .add("distanceInKilometers", distanceInKilometers)
                .add("durationInMinutes", durationInMinutes)
                .add("start", start)
                .add("end", end)
                .add("pois", pois)
                .add("routeCoordinates", routeCoordinates)
                .add("calculatedCoordinates", calculatedCoordinates)
                .add("difficulty", difficulty)
                .add("primarySurface", primarySurface)
                .build();
    }
}
