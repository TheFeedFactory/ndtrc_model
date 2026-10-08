package nl.ithelden;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;
import nl.ithelden.model.ndtrc.Promotion;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class PromotionTest {

    @Test
    void testExternalReferenceField() {
        Promotion promotion = new Promotion();
        Assertions.assertNull(promotion.getExternalReference());

        promotion.setExternalReference("actie-12345");
        Assertions.assertEquals("actie-12345", promotion.getExternalReference());
    }

    @Test
    void testExternalReferencePositionedAfterProduct() {
        List<String> fields = new ArrayList<>();
        for (Field field : Promotion.class.getDeclaredFields()) {
            if (!field.isSynthetic() && !Modifier.isStatic(field.getModifiers())) {
                fields.add(field.getName());
            }
        }

        int productIndex = fields.indexOf("product");
        int externalRefIndex = fields.indexOf("externalReference");

        Assertions.assertTrue(productIndex >= 0, "product field should exist");
        Assertions.assertTrue(externalRefIndex >= 0, "externalReference field should exist");
        Assertions.assertEquals(productIndex + 1, externalRefIndex,
            "externalReference should be positioned immediately after product");
    }
}
