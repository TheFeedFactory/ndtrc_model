package nl.ithelden;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Map;
import nl.ithelden.model.ndtrc.File;
import nl.ithelden.model.ndtrc.Promotion;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class PromotionImageAndDiscountValueTest {
    private final ObjectMapper mapper = new ObjectMapper();

    @Test
    void testDiscountValueRequiredDefaultsToTrue() {
        // Default true so a promotion that carries no percentage or amount shows up as
        // incomplete; an admin turns it off for the offers that genuinely need no value
        // (a gift, a present, free entrance).
        Promotion promotion = new Promotion();

        Assertions.assertTrue(promotion.isDiscountValueRequired());
    }

    @Test
    void testDiscountValueRequiredDefaultsToTrueOnDeserialisation() throws Exception {
        // Promotions stored before this field existed must read back as "value required",
        // not as false — an absent field may not silently switch the check off.
        Promotion promotion = mapper.readValue("{\"product\":\"citycard\"}", Promotion.class);

        Assertions.assertTrue(promotion.isDiscountValueRequired());
    }

    @Test
    void testDiscountValueRequiredCanBeTurnedOff() throws Exception {
        Promotion promotion = mapper.readValue(
            "{\"product\":\"tuesday-gift\",\"promotionType\":\"gift\",\"discountValueRequired\":false}", Promotion.class);

        Assertions.assertFalse(promotion.isDiscountValueRequired());
    }

    @Test
    void testDiscountValueRequiredAlwaysSerialises() throws Exception {
        // Primitive boolean, like enabled: always on the wire, so the GUI and the
        // publishing sites never have to guess the default.
        Map<?, ?> json = mapper.readValue(mapper.writeValueAsString(new Promotion()), Map.class);

        Assertions.assertTrue(json.containsKey("discountValueRequired"));
        Assertions.assertEquals(true, json.get("discountValueRequired"));
    }

    @Test
    void testImageDefaultsToNull() {
        Assertions.assertNull(new Promotion().getImage());
    }

    @Test
    void testImageRoundTrips() throws Exception {
        File image = new File();
        image.setHlink("https://cdn.example.com/citycard-logo.png");
        image.setFilename("citycard-logo.png");
        image.setFiletype(File.FileType.png);
        image.setMediatype(File.MediaType.logo);
        image.setCopyright("City Card");
        Promotion promotion = new Promotion();
        promotion.setProduct("citycard");
        promotion.setImage(image);

        Promotion parsed = mapper.readValue(mapper.writeValueAsString(promotion), Promotion.class);

        Assertions.assertEquals("https://cdn.example.com/citycard-logo.png", parsed.getImage().getHlink());
        Assertions.assertEquals(File.MediaType.logo, parsed.getImage().getMediatype());
        Assertions.assertEquals("City Card", parsed.getImage().getCopyright());
    }

    @Test
    void testImageDeserialisesFromJson() throws Exception {
        Promotion promotion = mapper.readValue(
            "{\"image\":{\"hlink\":\"https://cdn.example.com/logo.png\",\"mediatype\":\"logo\"}}", Promotion.class);

        Assertions.assertEquals("https://cdn.example.com/logo.png", promotion.getImage().getHlink());
    }
}
