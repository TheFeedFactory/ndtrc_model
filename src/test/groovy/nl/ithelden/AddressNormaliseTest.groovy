package nl.ithelden

import nl.ithelden.model.ndtrc.Address
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class AddressNormaliseTest {
    @Test
    void testNormalise1() {
        Address address = new Address(
                zipcode: "1234ab",
                city: "amsterdam",
        )
        address.normaliseAdresItems()

        Assertions.assertEquals("1234 AB", address.zipcode)
        Assertions.assertEquals("Amsterdam", address.city)
    }

    @Test
    void testNormalise2() {
        Address address = new Address(
                zipcode: "1234    ab",
                city: "DEN HAAG",
        )
        address.normaliseAdresItems()

        Assertions.assertEquals("1234 AB", address.zipcode)
        Assertions.assertEquals("Den Haag", address.city)
    }

    @Test
    void testNormalise3() {
        Address address = new Address(
                zipcode: "1234    abd",
                city: "DEN HAAG",
        )
        address.normaliseAdresItems()

        Assertions.assertEquals("1234    abd", address.zipcode)
        Assertions.assertEquals("Den Haag", address.city)
    }

    @Test
    void testNormaliseWrong() {
        Address address = new Address(
                zipcode: "12ab",
                city: "den Haag",
        )
        address.normaliseAdresItems()

        Assertions.assertEquals("12ab", address.zipcode)
        Assertions.assertEquals("den Haag", address.city)
    }

    @Test
    void testNormaliseNull() {
        Address address = new Address(
                zipcode: null,
                city: null,
        )
        address.normaliseAdresItems()

        Assertions.assertNull(address.zipcode)
        Assertions.assertNull(address.city)
    }

    @Test
    void testNormaliseEmpty() {
        Address address = new Address(
                zipcode: "",
                city: "",
        )
        address.normaliseAdresItems()

        Assertions.assertEquals("", address.zipcode)
        Assertions.assertEquals("", address.city)
    }
}
