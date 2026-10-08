package nl.ithelden.model.ndtrc

import groovy.transform.ToString
import org.joda.time.DateTime
import org.joda.time.Duration

/**
 * Represents a promotion associated with a TRC item, detailing the product, type,
 * discount, validity period, translations, and associated details.
 */
@ToString(includeNames = true)
class Promotion {
    String product
    String externalReference
    PromotionType promotionType
    Discount discount

    /**
     * Whether this promotion must carry a concrete discount value — a
     * {@link Discount#percentage} or a {@link Discount#amount} — to count as complete.
     *
     * Defaults to {@code true}: a promotion on an event or a location that has neither is
     * reported as incomplete, so the missing data becomes visible. Set it to {@code false}
     * for the offers that genuinely need no value (a gift, a present, free entrance), which
     * exempts every event and location using that promotion product from the check.
     *
     * Deliberately explicit rather than derived from {@link #promotionType}: the type says
     * what kind of offer it is, this says whether a value is expected, and the two are not
     * the same question (a {@code discount} with a partner-side price still has no
     * percentage here).
     */
    boolean discountValueRequired = true
    List<PromotionTranslation> translations
    List<Contactinfo.Url> detailsUrls

    /**
     * Optional image for the promotion, typically the logo of the offer (a city card, a
     * partner brand) shown next to it in the editor and on the publishing sites.
     * Uses the same {@link File} shape as the rest of the model, so it carries
     * {@code hlink}, {@code copyright} and {@code title} translations.
     */
    File image
    boolean enabled = true
    Boolean restrictedToRegisteredUsers
    ValidityStrategy validityStrategy = ValidityStrategy.always

    static enum ValidityStrategy {
        always, dateRange, earlyBird, lastMinute
    }

    String eventRelativeDuration; // e.g. "PT1H" for 1 hour, P3Y6M4DT12H30M5S for 3 years, 6 months, 4 days, 12 hours, 30 minutes and 5 seconds
    DateTime startDate
    DateTime endDate

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

    static enum PromotionType { none, free, discount, gift, allowance }
}
