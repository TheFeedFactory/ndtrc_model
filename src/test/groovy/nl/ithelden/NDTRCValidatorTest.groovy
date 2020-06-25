package nl.ithelden

import nl.ithelden.services.NDTRCValidator
import org.junit.Assert
import org.junit.Test

/**
 * Created by mjumelet on 04/05/2017.
 */
class NDTRCValidatorTest {

    @Test
    void testSample1() {
        NDTRCValidator ndtrcValidator = new NDTRCValidator()
        ndtrcValidator.validate(this.getClass().getResourceAsStream("/test_sample1.xml").text)

        Assert.assertTrue(ndtrcValidator.errors.join("\n"), ndtrcValidator.isValid)
    }
}
