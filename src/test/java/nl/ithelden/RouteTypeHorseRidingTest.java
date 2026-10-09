package nl.ithelden;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Map;
import nl.ithelden.model.ndtrc.RouteInfo;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class RouteTypeHorseRidingTest {
    private final ObjectMapper mapper = new ObjectMapper();

    @Test
    void testHorseRidingSerialisesAsItsName() throws Exception {
        RouteInfo routeInfo = new RouteInfo();
        routeInfo.setRouteType(RouteInfo.RouteType.horse_riding);

        Map<?, ?> json = mapper.readValue(mapper.writeValueAsString(routeInfo), Map.class);

        Assertions.assertEquals("horse_riding", json.get("routeType"));
    }

    @Test
    void testHorseRidingDeserialises() throws Exception {
        // RouteMaker's "Ruiteren" routes arrive in ff-api as this value
        RouteInfo routeInfo = mapper.readValue("{\"routeType\":\"horse_riding\"}", RouteInfo.class);

        Assertions.assertEquals(RouteInfo.RouteType.horse_riding, routeInfo.getRouteType());
    }
}
