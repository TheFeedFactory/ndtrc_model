package nl.ithelden.model.ndtrc


import groovy.transform.ToString
import org.joda.time.DateTime

@ToString(includeNames = true)
class Promotion {
    PromotionType promotionType
    Discount discount
    List<PromotionTranslation> translations
    List<PromotionDetailsUrl> detailsUrls
    DateTime startDate
    DateTime endDate
    boolean enabled = true
    List<PromotionTargetAudience> targetAudiences
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

    @ToString(includeNames = true)
    static class PromotionDetailsUrl {
        String lang, url
    }

    static enum PromotionType { free, discount, gift, allowance }
    static enum PromotionTargetAudience { residents, visitors }
}