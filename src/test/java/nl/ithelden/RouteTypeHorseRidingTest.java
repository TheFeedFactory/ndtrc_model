package nl.ithelden

import com.fasterxml.jackson.databind.ObjectMapper
import nl.ithelden.model.ndtrc.RouteInfo
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class RouteTypeHorseRidingTest {
    private final ObjectMapper mapper = new ObjectMapper()

    @Test
    void testHorseRidingSerialisesAsItsName() {
        RouteInfo routeInfo = new RouteInfo(routeType: RouteInfo.RouteType.horse_riding)

        Map json = mapper.readValue(mapper.writeValueAsString(routeInfo), Map)

        Assertions.assertEquals('horse_riding', json.routeType)
    }

    @Test
    void testHorseRidingDeserialises() {
        // RouteMaker's "Ruiteren" routes arrive in ff-api as this value
        RouteInfo routeInfo = mapper.readValue('{"routeType":"horse_riding"}', RouteInfo)

        Assertions.assertEquals(RouteInfo.RouteType.horse_riding, routeInfo.routeType)
    }
}
