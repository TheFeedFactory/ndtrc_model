package nl.ithelden.model.ndtrc;

import com.fasterxml.jackson.annotation.JsonProperty;
import nl.ithelden.model.util.ToStringBuilder;

/**
 * Contains language-specific details for a TRC item,
 * including title, short description, and long description.
 */
public class TRCItemDetail {
    @JsonProperty private String lang;
    @JsonProperty private String longdescription;
    @JsonProperty private String shortdescription;
    @JsonProperty private String title;

    public String getLang() {
        return lang;
    }

    public void setLang(String lang) {
        this.lang = lang;
    }

    public String getLongdescription() {
        return longdescription;
    }

    public void setLongdescription(String longdescription) {
        this.longdescription = longdescription;
    }

    public String getShortdescription() {
        return shortdescription;
    }

    public void setShortdescription(String shortdescription) {
        this.shortdescription = shortdescription;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(TRCItemDetail.class, this)
                .add("lang", lang)
                .add("longdescription", longdescription)
                .add("shortdescription", shortdescription)
                .add("title", title)
                .build();
    }
}
