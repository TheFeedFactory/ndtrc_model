package nl.ithelden

import com.fasterxml.jackson.databind.ObjectMapper
import nl.ithelden.model.ndtrc.Address
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class AddressContinentTest {
    private final ObjectMapper mapper = new ObjectMapper()

    @Test
    void testContinentDefaultsToNull() {
        // country defaults to 'NL', but continent deliberately does not default —
        // a wrong continent is worse than a missing one for the country filter.
        Address address = new Address()

        Assertions.assertEquals('NL', address.country)
        Assertions.assertNull(address.continent)
    }

    @Test
    void testContinentSerialisesWhenSet() {
        Address address = new Address(city: 'Dubai', country: 'AE', continent: 'AS')

        Map json = mapper.readValue(mapper.writeValueAsString(address), Map)

        Assertions.assertEquals('AS', json.continent)
    }

    @Test
    void testContinentOmittedFromJsonWhenNull() {
        // @JsonInclude(NON_NULL) on Address keeps existing payloads byte-identical
        Address address = new Address(city: 'Utrecht')

        Map json = mapper.readValue(mapper.writeValueAsString(address), Map)

        Assertions.assertFalse(json.containsKey('continent'))
    }

    @Test
    void testContinentDeserialises() {
        Address address = mapper.readValue('{"city":"Tokyo","country":"JP","continent":"AS"}', Address)

        Assertions.assertEquals('AS', address.continent)
    }

    @Test
    void testUnexpectedContinentValueDoesNotBreakDeserialisation() {
        // continent is a free-form String rather than an enum precisely so an
        // unexpected value in an incoming feed cannot fail the whole parse.
        Address address = mapper.readValue('{"continent":"Europa"}', Address)

        Assertions.assertEquals('Europa', address.continent)
    }

    @Test
    void testContinentDoesNotAffectIsEmpty() {
        // isEmpty() judges whether there is a usable postal address; a bare
        // continent is no more an address than a bare country is.
        Address address = new Address(continent: 'AS')

        Assertions.assertTrue(address.isEmpty())
    }
}
