package nl.ithelden.model.ndtrc;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.ArrayList;
import java.util.List;
import nl.ithelden.model.util.ToStringBuilder;

/**
 * Represents a sub-group within a TRC item, often used to map specific types
 * like rooms or variants. Includes its own TRC ID, type, categories, translations,
 * and media.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SubItemGroup {
    private String trcid; // ID of this type (random)
    private TRCItemCategories.Type type;
    private List<TRCItemCategories.Category> categories = new ArrayList<>();  // Categories/properties of the item
    private List<SubItemTranslation> subItemTranslations;
    private List<File> media; // media

    public String getTrcid() {
        return trcid;
    }

    public void setTrcid(String trcid) {
        this.trcid = trcid;
    }

    public TRCItemCategories.Type getType() {
        return type;
    }

    public void setType(TRCItemCategories.Type type) {
        this.type = type;
    }

    public List<TRCItemCategories.Category> getCategories() {
        return categories;
    }

    public void setCategories(List<TRCItemCategories.Category> categories) {
        this.categories = categories;
    }

    public List<SubItemTranslation> getSubItemTranslations() {
        return subItemTranslations;
    }

    public void setSubItemTranslations(List<SubItemTranslation> subItemTranslations) {
        this.subItemTranslations = subItemTranslations;
    }

    public List<File> getMedia() {
        return media;
    }

    public void setMedia(List<File> media) {
        this.media = media;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(SubItemGroup.class, this)
                .add("trcid", trcid)
                .add("type", type)
                .add("categories", categories)
                .add("subItemTranslations", subItemTranslations)
                .add("media", media)
                .build();
    }

    /**
     * Represents a translation for the sub-item group's title in a specific language.
     */
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    public static class SubItemTranslation {
        private String lang;
        private String title;

        public String getLang() {
            return lang;
        }

        public void setLang(String lang) {
            this.lang = lang;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        @Override
        public String toString() {
            return new ToStringBuilder(SubItemTranslation.class, this)
                    .add("lang", lang)
                    .add("title", title)
                    .build();
        }
    }
}
