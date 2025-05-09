package nl.ithelden.model.ndtrc

import groovy.transform.ToString
import org.joda.time.DateTime

/**
 * Represents a promotion associated with a TRC item, detailing the product, type,
 * discount, validity period, translations, and associated details.
 */
@ToString(includeNames = true)
class Promotion {
    String product
    PromotionType promotionType
    Discount discount
    List<PromotionTranslation> translations
    List<Contactinfo.Url> detailsUrls
    DateTime startDate
    DateTime endDate
    boolean enabled = true
    List<Calendar.PatternDate.Open> opens

    /**
     * Represents the discount details for a promotion, which can be free,
     * a percentage, or a fixed amount.
     */
    @ToString(includeNames = true)
    static class Discount {
        Boolean free
        Integer percentage
        Double amount
    }

    /**
     * Represents a translation of the promotion's description in a specific language.
     */
    @ToString(includeNames = true)
    static class PromotionTranslation {
        String lang, description
    }

    static enum PromotionType { free, discount, gift, allowance }
}
