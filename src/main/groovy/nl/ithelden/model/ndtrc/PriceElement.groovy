package nl.ithelden.model.ndtrc

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import groovy.transform.ToString

@ToString(includeNames = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
class PriceElement {
    @JsonProperty Boolean freeentrance = false
    @JsonProperty PriceValue priceValue
    @JsonProperty Description description
    @JsonProperty List<Comment> comments = []
    @JsonProperty List<ExtraPriceInformation> extraPriceInformations = []

    @ToString(includeNames = true)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    static class Description {
        @JsonProperty PriceDescriptionValue value
        @JsonProperty List<DescriptionTranslation> descriptionTranslations = []

        public static enum PriceDescriptionValue { Adults, Children, Groups, CJP, Pasholders, Lastminute }
    }

    @ToString(includeNames = true)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    static class DescriptionTranslation {
        @JsonProperty String lang
        @JsonProperty String text
    }

    @ToString(includeNames = true)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    static class ExtraPriceInformation {
        @JsonProperty String lang
        @JsonProperty String text
    }

    @ToString(includeNames = true)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    static class PriceValue {
        @JsonProperty Double from
        @JsonProperty Double until
    }

    @ToString(includeNames = true)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    static class Comment {
        @JsonProperty String text
    }
}