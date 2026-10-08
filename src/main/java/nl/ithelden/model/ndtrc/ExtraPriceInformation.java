package nl.ithelden.model.ndtrc;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import nl.ithelden.model.util.ToStringBuilder;

/**
 * Provides additional pricing information in a specific language.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ExtraPriceInformation {
    @JsonProperty private String lang;
    @JsonProperty private String text;

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
