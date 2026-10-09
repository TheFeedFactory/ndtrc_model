package nl.ithelden.model.ndtrc;

import java.util.List;
import nl.ithelden.model.util.ToStringBuilder;
import org.joda.time.DateTime;

/**
 * Represents a promotion associated with a TRC item, detailing the product, type,
 * discount, validity period, translations, and associated details.
 */
public class Promotion {
    private String product;
    private String externalReference;
    private PromotionType promotionType;
    private Discount discount;

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
    private boolean discountValueRequired = true;
    private List<PromotionTranslation> translations;
    private List<Contactinfo.Url> detailsUrls;

    /**
     * Optional image for the promotion, typically the logo of the offer (a city card, a
     * partner brand) shown next to it in the editor and on the publishing sites.
     * Uses the same {@link File} shape as the rest of the model, so it carries
     * {@code hlink}, {@code copyright} and {@code title} translations.
     */
    private File image;
    private boolean enabled = true;
    private Boolean restrictedToRegisteredUsers;
    private ValidityStrategy validityStrategy = ValidityStrategy.always;

    public static enum ValidityStrategy {
        always, dateRange, earlyBird, lastMinute;

        // Groovy gives every enum these members; kept so the 1.x API is unchanged.
        public static final ValidityStrategy MIN_VALUE = always;
        public static final ValidityStrategy MAX_VALUE = lastMinute;

        public ValidityStrategy next() {
            ValidityStrategy[] values = values();
            int ordinal = ordinal() + 1;
            return values[ordinal >= values.length ? 0 : ordinal];
        }

        public ValidityStrategy previous() {
            ValidityStrategy[] values = values();
            int ordinal = ordinal() - 1;
            return values[ordinal < 0 ? values.length - 1 : ordinal];
        }
    }

    private String eventRelativeDuration; // e.g. "PT1H" for 1 hour, P3Y6M4DT12H30M5S for 3 years, 6 months, 4 days, 12 hours, 30 minutes and 5 seconds
    private DateTime startDate;
    private DateTime endDate;

    private List<Calendar.PatternDate.Open> opens;

    public String getProduct() {
        return product;
    }

    public void setProduct(String product) {
        this.product = product;
    }

    public String getExternalReference() {
        return externalReference;
    }

    public void setExternalReference(String externalReference) {
        this.externalReference = externalReference;
    }

    public PromotionType getPromotionType() {
        return promotionType;
    }

    public void setPromotionType(PromotionType promotionType) {
        this.promotionType = promotionType;
    }

    public Discount getDiscount() {
        return discount;
    }

    public void setDiscount(Discount discount) {
        this.discount = discount;
    }

    public boolean getDiscountValueRequired() {
        return discountValueRequired;
    }

    public boolean isDiscountValueRequired() {
        return discountValueRequired;
    }

    public void setDiscountValueRequired(boolean discountValueRequired) {
        this.discountValueRequired = discountValueRequired;
    }

    public List<PromotionTranslation> getTranslations() {
        return translations;
    }

    public void setTranslations(List<PromotionTranslation> translations) {
        this.translations = translations;
    }

    public List<Contactinfo.Url> getDetailsUrls() {
        return detailsUrls;
    }

    public void setDetailsUrls(List<Contactinfo.Url> detailsUrls) {
        this.detailsUrls = detailsUrls;
    }

    public File getImage() {
        return image;
    }

    public void setImage(File image) {
        this.image = image;
    }

    public boolean getEnabled() {
        return enabled;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public Boolean getRestrictedToRegisteredUsers() {
        return restrictedToRegisteredUsers;
    }

    public Boolean isRestrictedToRegisteredUsers() {
        return restrictedToRegisteredUsers;
    }

    public void setRestrictedToRegisteredUsers(Boolean restrictedToRegisteredUsers) {
        this.restrictedToRegisteredUsers = restrictedToRegisteredUsers;
    }

    public ValidityStrategy getValidityStrategy() {
        return validityStrategy;
    }

    public void setValidityStrategy(ValidityStrategy validityStrategy) {
        this.validityStrategy = validityStrategy;
    }

    public String getEventRelativeDuration() {
        return eventRelativeDuration;
    }

    public void setEventRelativeDuration(String eventRelativeDuration) {
        this.eventRelativeDuration = eventRelativeDuration;
    }

    public DateTime getStartDate() {
        return startDate;
    }

    public void setStartDate(DateTime startDate) {
        this.startDate = startDate;
    }

    public DateTime getEndDate() {
        return endDate;
    }

    public void setEndDate(DateTime endDate) {
        this.endDate = endDate;
    }

    public List<Calendar.PatternDate.Open> getOpens() {
        return opens;
    }

    public void setOpens(List<Calendar.PatternDate.Open> opens) {
        this.opens = opens;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(Promotion.class, this)
                .add("product", product)
                .add("externalReference", externalReference)
                .add("promotionType", promotionType)
                .add("discount", discount)
                .add("discountValueRequired", discountValueRequired)
                .add("translations", translations)
                .add("detailsUrls", detailsUrls)
                .add("image", image)
                .add("enabled", enabled)
                .add("restrictedToRegisteredUsers", restrictedToRegisteredUsers)
                .add("validityStrategy", validityStrategy)
                .add("eventRelativeDuration", eventRelativeDuration)
                .add("startDate", startDate)
                .add("endDate", endDate)
                .add("opens", opens)
                .build();
    }

    /**
     * Represents the discount details for a promotion, which can be free,
     * a percentage, or a fixed amount.
     */
    public static class Discount {
        private Boolean free;
        private Integer percentage;
        private Double amount;

        public Boolean getFree() {
            return free;
        }

        public Boolean isFree() {
            return free;
        }

        public void setFree(Boolean free) {
            this.free = free;
        }

        public Integer getPercentage() {
            return percentage;
        }

        public void setPercentage(Integer percentage) {
            this.percentage = percentage;
        }

        public Double getAmount() {
            return amount;
        }

        public void setAmount(Double amount) {
            this.amount = amount;
        }

        @Override
        public String toString() {
            return new ToStringBuilder(Discount.class, this)
                    .add("free", free)
                    .add("percentage", percentage)
                    .add("amount", amount)
                    .build();
        }
    }

    /**
     * Represents a translation of the promotion's description in a specific language.
     */
    public static class PromotionTranslation {
        private String lang;
        private String description;

        public String getLang() {
            return lang;
        }

        public void setLang(String lang) {
            this.lang = lang;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        @Override
        public String toString() {
            return new ToStringBuilder(PromotionTranslation.class, this)
                    .add("lang", lang)
                    .add("description", description)
                    .build();
        }
    }

    public static enum PromotionType {
        none, free, discount, gift, allowance;

        // Groovy gives every enum these members; kept so the 1.x API is unchanged.
        public static final PromotionType MIN_VALUE = none;
        public static final PromotionType MAX_VALUE = allowance;

        public PromotionType next() {
            PromotionType[] values = values();
            int ordinal = ordinal() + 1;
            return values[ordinal >= values.length ? 0 : ordinal];
        }

        public PromotionType previous() {
            PromotionType[] values = values();
            int ordinal = ordinal() - 1;
            return values[ordinal < 0 ? values.length - 1 : ordinal];
        }
    }
}
