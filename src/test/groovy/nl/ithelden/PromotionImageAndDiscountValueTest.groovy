package nl.ithelden

import com.fasterxml.jackson.databind.ObjectMapper
import nl.ithelden.model.ndtrc.File
import nl.ithelden.model.ndtrc.Promotion
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class PromotionImageAndDiscountValueTest {
    private final ObjectMapper mapper = new ObjectMapper()

    @Test
    void testDiscountValueRequiredDefaultsToTrue() {
        // Default true so a promotion that carries no percentage or amount shows up as
        // incomplete; an admin turns it off for the offers that genuinely need no value
        // (a gift, a present, free entrance).
        Promotion promotion = new Promotion()

        Assertions.assertTrue(promotion.discountValueRequired)
    }

    @Test
    void testDiscountValueRequiredDefaultsToTrueOnDeserialisation() {
        // Promotions stored before this field existed must read back as "value required",
        // not as false — an absent field may not silently switch the check off.
        Promotion promotion = mapper.readValue('{"product":"citycard"}', Promotion)

        Assertions.assertTrue(promotion.discountValueRequired)
    }

    @Test
    void testDiscountValueRequiredCanBeTurnedOff() {
        Promotion promotion = mapper.readValue(
            '{"product":"tuesday-gift","promotionType":"gift","discountValueRequired":false}', Promotion)

        Assertions.assertFalse(promotion.discountValueRequired)
    }

    @Test
    void testDiscountValueRequiredAlwaysSerialises() {
        // Primitive boolean, like enabled: always on the wire, so the GUI and the
        // publishing sites never have to guess the default.
        Map json = mapper.readValue(mapper.writeValueAsString(new Promotion()), Map)

        Assertions.assertTrue(json.containsKey('discountValueRequired'))
        Assertions.assertEquals(true, json.discountValueRequired)
    }

    @Test
    void testImageDefaultsToNull() {
        Assertions.assertNull(new Promotion().image)
    }

    @Test
    void testImageRoundTrips() {
        Promotion promotion = new Promotion(
            product: 'citycard',
            image: new File(
                hlink: 'https://cdn.example.com/citycard-logo.png',
                filename: 'citycard-logo.png',
                filetype: File.FileType.png,
                mediatype: File.MediaType.logo,
                copyright: 'City Card'
            )
        )

        Promotion parsed = mapper.readValue(mapper.writeValueAsString(promotion), Promotion)

        Assertions.assertEquals('https://cdn.example.com/citycard-logo.png', parsed.image.hlink)
        Assertions.assertEquals(File.MediaType.logo, parsed.image.mediatype)
        Assertions.assertEquals('City Card', parsed.image.copyright)
    }

    @Test
    void testImageDeserialisesFromJson() {
        Promotion promotion = mapper.readValue(
            '{"image":{"hlink":"https://cdn.example.com/logo.png","mediatype":"logo"}}', Promotion)

        Assertions.assertEquals('https://cdn.example.com/logo.png', promotion.image.hlink)
    }
}
