package nl.ithelden;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import nl.ithelden.model.ndtrc.Address;
import nl.ithelden.model.ndtrc.GISCoordinate;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class AddressIsEmptyTest {
    private static GISCoordinate gis(String x, String y) {
        GISCoordinate coordinate = new GISCoordinate();
        coordinate.setXcoordinate(x);
        coordinate.setYcoordinate(y);
        return coordinate;
    }

    @Test
    void newAddressIsEmptyEvenThoughCountryDefaultsToNL() {
        Assertions.assertTrue(new Address().isEmpty());
    }

    @Test
    void whitespaceOnlyFieldsCountAsEmpty() {
        Address address = new Address();
        address.setTitle("  ");
        address.setCity("\t");
        address.setHousenr("");
        Assertions.assertTrue(address.isEmpty());
    }

    @Test
    void eachPostalFieldMakesItNonEmpty() {
        String[] fields = {"title", "city", "housenr", "street", "zipcode", "province"};
        for (String field : fields) {
            Address address = new Address();
            switch (field) {
                case "title": address.setTitle("x"); break;
                case "city": address.setCity("x"); break;
                case "housenr": address.setHousenr("x"); break;
                case "street": address.setStreet("x"); break;
                case "zipcode": address.setZipcode("x"); break;
                default: address.setProvince("x"); break;
            }
            Assertions.assertFalse(address.isEmpty(), field);
        }
    }

    @Test
    void countryContinentDistrictAndNeighbourhoodDoNotCount() {
        Address address = new Address();
        address.setCountry("BE");
        address.setContinent("EU");
        address.setDistrict("d");
        address.setNeighbourhood("n");
        address.setCitytrcid("c");
        Assertions.assertTrue(address.isEmpty());
    }

    @Test
    void nullCoordinateListCountsAsEmpty() {
        Address address = new Address();
        address.setGisCoordinates(null);
        Assertions.assertTrue(address.isEmpty());
    }

    @Test
    void onlyEmptyCoordinatesCountAsEmpty() {
        Address address = new Address();
        address.setGisCoordinates(new ArrayList<>(List.of(gis(null, " "), gis("", null))));
        Assertions.assertTrue(address.isEmpty());
    }

    @Test
    void oneUsableCoordinateMakesItNonEmpty() {
        Address address = new Address();
        address.setGisCoordinates(new ArrayList<>(List.of(gis(null, null), gis(null, "52.1"))));
        Assertions.assertFalse(address.isEmpty());
    }

    @Test
    void aNullCoordinateFailsAsIn1x() {
        Address address = new Address();
        address.setGisCoordinates(new ArrayList<>(Arrays.asList((GISCoordinate) null)));
        Assertions.assertThrows(NullPointerException.class, address::isEmpty);
    }
}
