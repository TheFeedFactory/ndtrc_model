package nl.ithelden.model.ndtrc


import groovy.transform.ToString
import org.joda.time.DateTime

@ToString(includeNames = true)
class Promotion {
    List<String> products
    PromotionType promotionType
    Discount discount
    List<PromotionTranslation> translations
    List<Contactinfo.Url> detailsUrls
    DateTime startDate
    DateTime endDate
    boolean enabled = true
    List<Calendar.PatternDate.Open> opens

    @ToString(includeNames = true)
    static class Discount {
        Boolean free
        Integer percentage
        Double amount
    }

    @ToString(includeNames = true)
    static class PromotionTranslation {
        String lang, description
    }

    static enum PromotionType { free, discount, gift, allowance }
}