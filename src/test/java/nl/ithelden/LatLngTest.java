package nl.ithelden;

import nl.ithelden.model.ndtrc.RouteInfo;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class LatLngTest {
    private static RouteInfo.LatLng at(Double lat, Double lng) {
        RouteInfo.LatLng point = new RouteInfo.LatLng();
        point.setLat(lat);
        point.setLng(lng);
        return point;
    }

    @Test
    void validityIsInclusiveOfTheBounds() {
        Assertions.assertTrue(at(-90.0, -180.0).isValid());
        Assertions.assertTrue(at(90.0, 180.0).isValid());
        Assertions.assertFalse(at(90.0001, 0.0).isValid());
        Assertions.assertFalse(at(0.0, -180.5).isValid());
        Assertions.assertFalse(at(null, 0.0).isValid());
        Assertions.assertFalse(at(0.0, null).isValid());
        Assertions.assertFalse(at(Double.NaN, 0.0).isValid());
        Assertions.assertFalse(at(0.0, Double.POSITIVE_INFINITY).isValid());
    }

    @Test
    void haversineDistanceInKilometres() {
        RouteInfo.LatLng amsterdam = at(52.3676, 4.9041);
        RouteInfo.LatLng utrecht = at(52.0907, 5.1214);
        Assertions.assertEquals(34.16205836227199, amsterdam.distanceTo(utrecht), 1e-9);
        Assertions.assertEquals(0.0, amsterdam.distanceTo(amsterdam));
        Assertions.assertEquals(20015.086661761787, amsterdam.distanceTo(at(-52.3676, 4.9041 - 180)), 1e-6);
    }

    @Test
    void distanceIsNullWhenEitherPointIsUnusable() {
        RouteInfo.LatLng amsterdam = at(52.3676, 4.9041);
        Assertions.assertNull(amsterdam.distanceTo(null));
        Assertions.assertNull(amsterdam.distanceTo(at(100.0, 1.0)));
        Assertions.assertNull(at(100.0, 1.0).distanceTo(amsterdam));
    }

    @Test
    void validIsSerialisedAsAReadOnlyProperty() throws Exception {
        // isValid() is not @JsonIgnore'd, so Jackson writes "valid" (as 1.x did) but cannot read it back
        com.fasterxml.jackson.databind.ObjectMapper mapper = new com.fasterxml.jackson.databind.ObjectMapper();
        Assertions.assertEquals("{\"lat\":1.0,\"lng\":2.0,\"altitude\":null,\"label\":null,\"valid\":true}", mapper.writeValueAsString(at(1.0, 2.0)));
    }
}
