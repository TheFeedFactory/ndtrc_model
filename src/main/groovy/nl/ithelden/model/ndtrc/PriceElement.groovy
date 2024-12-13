package nl.ithelden.model.ndtrc

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import groovy.transform.ToString

@ToString(includeNames = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
class PriceElement {
    Boolean freeentrance = false
    PriceValue priceValue
    Description description
    List<Comment> comments = []
    List<ExtraPriceInformation> extraPriceInformations = []

    @ToString(includeNames = true)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    static class Description {
        PriceDescriptionValue value
        List<DescriptionTranslation> descriptionTranslations = []

        public static enum PriceDescriptionValue { Adults, Children, Groups, CJP, Pasholders, Lastminute }
    }

    @ToString(includeNames = true)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    static class DescriptionTranslation {
        String lang
        String text
    }

    @ToString(includeNames = true)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    static class ExtraPriceInformation {
        String lang
        String text
    }

    @ToString(includeNames = true)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    static class PriceValue {
        Double from
        Double until
    }

    @ToString(includeNames = true)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    static class Comment {
        String text
    }
}