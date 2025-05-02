package nl.ithelden.model.ndtrc

import com.fasterxml.jackson.annotation.JsonInclude
import groovy.transform.ToString

/**
 * Represents a price element for a TRC item, including whether entrance is free,
 * the price value range, description, comments, and extra information.
 */
@ToString(includeNames = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
class PriceElement {
    Boolean freeentrance = false
    PriceValue priceValue
    Description description
    List<Comment> comments = []
    List<ExtraPriceInformation> extraPriceInformations = []

    /**
     * Describes the target audience or condition for the price element (e.g., Adults, Children),
     * including translations.
     */
    @ToString(includeNames = true)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    static class Description {
        PriceDescriptionValue value
        List<DescriptionTranslation> descriptionTranslations = []

        public static enum PriceDescriptionValue { Adults, Children, Groups, CJP, Pasholders, Lastminute }
    }

    /**
     * Represents a translation of the price description in a specific language.
     */
    @ToString(includeNames = true)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    static class DescriptionTranslation {
        String lang
        String text
    }

    /**
     * Provides additional pricing information in a specific language.
     * (Note: Similar to top-level ExtraPriceInformation)
     */
    @ToString(includeNames = true)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    static class ExtraPriceInformation {
        String lang
        String text
    }

    /**
     * Represents a price range with 'from' and 'until' values.
     */
    @ToString(includeNames = true)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    static class PriceValue {
        Double from
        Double until
    }

    /**
     * Represents a comment associated with the price element.
     */
    @ToString(includeNames = true)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    static class Comment {
        String text
    }
}