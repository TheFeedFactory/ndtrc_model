package nl.ithelden;

import nl.ithelden.model.ndtrc.Address;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * Edge cases of {@link Address#normaliseAdresItems()}, with the outcomes the 1.7.0 Groovy
 * implementation produced. They pin behaviour, not intent: some (an underscore counting as a
 * letter, surrounding whitespace blocking the zipcode rewrite) are quirks that consumers have
 * been living with.
 */
class AddressNormaliseEdgeCasesTest {
    private static String zipcode(String given) {
        Address address = new Address();
        address.setZipcode(given);
        address.normaliseAdresItems();
        return address.getZipcode();
    }

    private static String city(String given) {
        Address address = new Address();
        address.setCity(given);
        address.normaliseAdresItems();
        return address.getCity();
    }

    @Test
    void zipcodes() {
        Assertions.assertEquals("1234 AB", zipcode("1234AB"));
        Assertions.assertEquals("1234 AB", zipcode("1234 ab"));
        Assertions.assertEquals("1234 AB", zipcode("1234\tab"));
        Assertions.assertEquals("1234 _B", zipcode("1234_b"));
        Assertions.assertEquals(" 1234ab", zipcode(" 1234ab"));
        Assertions.assertEquals("1234ab ", zipcode("1234ab "));
        Assertions.assertEquals("1234 a b", zipcode("1234 a b"));
        Assertions.assertEquals("abcd12", zipcode("abcd12"));
        Assertions.assertEquals(" ", zipcode(" "));
    }

    @Test
    void citiesInOneCaseAreCapitalisedPerWord() {
        Assertions.assertEquals("A", city("a"));
        Assertions.assertEquals("Amsterdam  Noord", city("AMSTERDAM  NOORD"));
        Assertions.assertEquals("Amsterdam Zuidoost", city("AMSTERDAM ZUIDOOST"));
        Assertions.assertEquals("\tAmsterdam", city("\tamsterdam"));
        Assertions.assertEquals(" IJmuiden", city(" ijmuiden"));
    }

    @Test
    void ijDigraphAtTheStartOfEveryWord() {
        Assertions.assertEquals("IJ", city("ij"));
        Assertions.assertEquals("IJ IJ", city("IJ IJ"));
        Assertions.assertEquals("IJsselstein IJsselmonde", city("ijsselstein ijsselmonde"));
    }

    @Test
    void citiesWithMixedCaseOrNonAsciiOrPunctuationAreLeftAlone() {
        Assertions.assertEquals("Den Haag", city("Den Haag"));
        Assertions.assertEquals("den Haag", city("den Haag"));
        Assertions.assertEquals("ZÜRICH", city("ZÜRICH"));
        Assertions.assertEquals("zürich", city("zürich"));
        Assertions.assertEquals("s-hertogenbosch", city("s-hertogenbosch"));
        Assertions.assertEquals("'S-HERTOGENBOSCH", city("'S-HERTOGENBOSCH"));
        Assertions.assertEquals(" ", city(" "));
    }

    @Test
    void zipcodeAndCityAreNormalisedIndependently() {
        Address address = new Address();
        address.setZipcode("9999zz");
        address.setCity("Groningen");
        address.normaliseAdresItems();
        Assertions.assertEquals("9999 ZZ", address.getZipcode());
        Assertions.assertEquals("Groningen", address.getCity());
    }
}
