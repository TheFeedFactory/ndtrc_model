package nl.ithelden;

import java.util.LinkedHashMap;
import java.util.Map;
import nl.ithelden.model.ndtrc.Address;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class AddressNormaliseTest {
    private static Address address(String zipcode, String city) {
        Address address = new Address();
        address.setZipcode(zipcode);
        address.setCity(city);
        return address;
    }

    @Test
    void testNormalise1() {
        Address address = address("1234ab", "amsterdam");
        address.normaliseAdresItems();

        Assertions.assertEquals("1234 AB", address.getZipcode());
        Assertions.assertEquals("Amsterdam", address.getCity());
    }

    @Test
    void testNormalise2() {
        Address address = address("1234    ab", "DEN HAAG");
        address.normaliseAdresItems();

        Assertions.assertEquals("1234 AB", address.getZipcode());
        Assertions.assertEquals("Den Haag", address.getCity());
    }

    @Test
    void testNormalise3() {
        Address address = address("1234    abd", "DEN HAAG");
        address.normaliseAdresItems();

        Assertions.assertEquals("1234    abd", address.getZipcode());
        Assertions.assertEquals("Den Haag", address.getCity());
    }

    @Test
    void testNormaliseWrong() {
        Address address = address("12ab", "den Haag");
        address.normaliseAdresItems();

        Assertions.assertEquals("12ab", address.getZipcode());
        Assertions.assertEquals("den Haag", address.getCity());
    }

    @Test
    void testNormaliseKeepsTheIJDigraph() {
        // IJ is one Dutch letter written as two characters, and it capitalises as
        // a pair. Lowercasing the J spells the name wrong.
        Map<String, String> cases = new LinkedHashMap<>();
        cases.put("IJMUIDEN", "IJmuiden");
        cases.put("ijmuiden", "IJmuiden");
        cases.put("IJSSELSTEIN", "IJsselstein");
        cases.put("IJZENDOORN", "IJzendoorn");
        cases.put("OUDE IJSSELSTREEK", "Oude IJsselstreek");
        cases.forEach((given, expected) -> {
            Address address = address(null, given);
            address.normaliseAdresItems();

            Assertions.assertEquals(expected, address.getCity());
        });
    }

    @Test
    void testNormaliseLeavesIJInsideAWordAlone() {
        Map<String, String> cases = new LinkedHashMap<>();
        cases.put("NIJMEGEN", "Nijmegen");
        cases.put("WIJHE", "Wijhe");
        cases.put("RIJSSEN", "Rijssen");
        cases.forEach((given, expected) -> {
            Address address = address(null, given);
            address.normaliseAdresItems();

            Assertions.assertEquals(expected, address.getCity());
        });
    }

    @Test
    void testNormaliseNull() {
        Address address = address(null, null);
        address.normaliseAdresItems();

        Assertions.assertNull(address.getZipcode());
        Assertions.assertNull(address.getCity());
    }

    @Test
    void testNormaliseEmpty() {
        Address address = address("", "");
        address.normaliseAdresItems();

        Assertions.assertEquals("", address.getZipcode());
        Assertions.assertEquals("", address.getCity());
    }
}
