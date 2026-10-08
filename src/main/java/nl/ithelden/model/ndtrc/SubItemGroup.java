package nl.ithelden.model.ndtrc

import com.fasterxml.jackson.annotation.JsonInclude
import groovy.transform.ToString

/**
 * Represents a sub-group within a TRC item, often used to map specific types
 * like rooms or variants. Includes its own TRC ID, type, categories, translations,
 * and media.
 */
@ToString(includeNames = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
class SubItemGroup {
    String trcid // ID of this type (random)
    TRCItemCategories.Type type
    List<TRCItemCategories.Category> categories = []  // Categories/properties of the item
    List<SubItemTranslation> subItemTranslations
    List<File> media // media

    /**
     * Represents a translation for the sub-item group's title in a specific language.
     */
    @ToString(includeNames = true)
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    static class SubItemTranslation {
        String lang, title
    }
}