package nl.ithelden;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Map;
import nl.ithelden.model.ndtrc.Address;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class AddressContinentTest {
    private final ObjectMapper mapper = new ObjectMapper();

    @Test
    void testContinentDefaultsToNull() {
        // country defaults to 'NL', but continent deliberately does not default —
        // a wrong continent is worse than a missing one for the country filter.
        Address address = new Address();

        Assertions.assertEquals("NL", address.getCountry());
        Assertions.assertNull(address.getContinent());
    }

    @Test
    void testContinentSerialisesWhenSet() throws Exception {
        Address address = new Address();
        address.setCity("Dubai");
        address.setCountry("AE");
        address.setContinent("AS");

        Map<?, ?> json = mapper.readValue(mapper.writeValueAsString(address), Map.class);

        Assertions.assertEquals("AS", json.get("continent"));
    }

    @Test
    void testContinentOmittedFromJsonWhenNull() throws Exception {
        // @JsonInclude(NON_NULL) on Address keeps existing payloads byte-identical
        Address address = new Address();
        address.setCity("Utrecht");

        Map<?, ?> json = mapper.readValue(mapper.writeValueAsString(address), Map.class);

        Assertions.assertFalse(json.containsKey("continent"));
    }

    @Test
    void testContinentDeserialises() throws Exception {
        Address address = mapper.readValue("{\"city\":\"Tokyo\",\"country\":\"JP\",\"continent\":\"AS\"}", Address.class);

        Assertions.assertEquals("AS", address.getContinent());
    }

    @Test
    void testUnexpectedContinentValueDoesNotBreakDeserialisation() throws Exception {
        // continent is a free-form String rather than an enum precisely so an
        // unexpected value in an incoming feed cannot fail the whole parse.
        Address address = mapper.readValue("{\"continent\":\"Europa\"}", Address.class);

        Assertions.assertEquals("Europa", address.getContinent());
    }

    @Test
    void testContinentDoesNotAffectIsEmpty() {
        // isEmpty() judges whether there is a usable postal address; a bare
        // continent is no more an address than a bare country is.
        Address address = new Address();
        address.setContinent("AS");

        Assertions.assertTrue(address.isEmpty());
    }
}
