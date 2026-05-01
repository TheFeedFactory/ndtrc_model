package nl.ithelden

import nl.ithelden.model.ndtrc.Promotion
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class PromotionTest {

    @Test
    void testExternalReferenceField() {
        Promotion promotion = new Promotion()
        Assertions.assertNull(promotion.externalReference)

        promotion.externalReference = "actie-12345"
        Assertions.assertEquals("actie-12345", promotion.externalReference)
    }

    @Test
    void testExternalReferencePositionedAfterProduct() {
        def fields = Promotion.class.getDeclaredFields()
            .findAll { !it.synthetic && !java.lang.reflect.Modifier.isStatic(it.modifiers) }
            .collect { it.name }

        int productIndex = fields.indexOf("product")
        int externalRefIndex = fields.indexOf("externalReference")

        Assertions.assertTrue(productIndex >= 0, "product field should exist")
        Assertions.assertTrue(externalRefIndex >= 0, "externalReference field should exist")
        Assertions.assertEquals(productIndex + 1, externalRefIndex,
            "externalReference should be positioned immediately after product")
    }
}
