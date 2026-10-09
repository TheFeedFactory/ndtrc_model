package nl.ithelden.model.ndtrc;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.ArrayList;
import java.util.List;
import nl.ithelden.model.util.ToStringBuilder;

/**
 * Represents a price element for a TRC item, including whether entrance is free,
 * the price value range, description, comments, and extra information.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PriceElement {
    private Boolean freeentrance = false;
    private PriceValue priceValue;
    private Description description;
    private List<Comment> comments = new ArrayList<>();
    private List<ExtraPriceInformation> extraPriceInformations = new ArrayList<>();

    public Boolean getFreeentrance() {
        return freeentrance;
    }

    public Boolean isFreeentrance() {
        return freeentrance;
    }

    public void setFreeentrance(Boolean freeentrance) {
        this.freeentrance = freeentrance;
    }

    public PriceValue getPriceValue() {
        return priceValue;
    }

    public void setPriceValue(PriceValue priceValue) {
        this.priceValue = priceValue;
    }

    public Description getDescription() {
        return description;
    }

    public void setDescription(Description description) {
        this.description = description;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }

    public List<ExtraPriceInformation> getExtraPriceInformations() {
        return extraPriceInformations;
    }

    public void setExtraPriceInformations(List<ExtraPriceInformation> extraPriceInformations) {
        this.extraPriceInformations = extraPriceInformations;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(PriceElement.class, this)
                .add("freeentrance", freeentrance)
                .add("priceValue", priceValue)
                .add("description", description)
                .add("comments", comments)
                .add("extraPriceInformations", extraPriceInformations)
                .build();
    }

    /**
     * Describes the target audience or condition for the price element (e.g., Adults, Children),
     * including translations.
     *
     * {@code value} is a free-form string so it can hold per-account price-type values.
     * The standard defaults (Adults, Children, Groups, CJP, Pasholders, Lastminute) are
     * shipped as fallback; accounts may define their own values.
     */
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class Description {
        private String value;
        private List<DescriptionTranslation> descriptionTranslations = new ArrayList<>();

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }

        public List<DescriptionTranslation> getDescriptionTranslations() {
            return descriptionTranslations;
        }

        public void setDescriptionTranslations(List<DescriptionTranslation> descriptionTranslations) {
            this.descriptionTranslations = descriptionTranslations;
        }

        @Override
        public String toString() {
            return new ToStringBuilder(Description.class, this)
                    .add("value", value)
                    .add("descriptionTranslations", descriptionTranslations)
                    .build();
        }
    }

    /**
     * Represents a translation of the price description in a specific language.
     */
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class DescriptionTranslation {
        private String lang;
        private String text;

        public String getLang() {
            return lang;
        }

        public void setLang(String lang) {
            this.lang = lang;
        }

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }

        @Override
        public String toString() {
            return new ToStringBuilder(DescriptionTranslation.class, this)
                    .add("lang", lang)
                    .add("text", text)
                    .build();
        }
    }

    /**
     * Provides additional pricing information in a specific language.
     * (Note: Similar to top-level ExtraPriceInformation)
     */
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class ExtraPriceInformation {
        private String lang;
        private String text;

        public String getLang() {
            return lang;
        }

        public void setLang(String lang) {
            this.lang = lang;
        }

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }

        @Override
        public String toString() {
            return new ToStringBuilder(ExtraPriceInformation.class, this)
                    .add("lang", lang)
                    .add("text", text)
                    .build();
        }
    }

    /**
     * Represents a price range with 'from' and 'until' values.
     */
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class PriceValue {
        private Double from;
        private Double until;

        public Double getFrom() {
            return from;
        }

        public void setFrom(Double from) {
            this.from = from;
        }

        public Double getUntil() {
            return until;
        }

        public void setUntil(Double until) {
            this.until = until;
        }

        @Override
        public String toString() {
            return new ToStringBuilder(PriceValue.class, this)
                    .add("from", from)
                    .add("until", until)
                    .build();
        }
    }

    /**
     * Represents a comment associated with the price element.
     */
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class Comment {
        private String text;

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }

        @Override
        public String toString() {
            return new ToStringBuilder(Comment.class, this)
                    .add("text", text)
                    .build();
        }
    }
}
