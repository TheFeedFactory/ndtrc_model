package nl.ithelden.model.ndtrc

import com.fasterxml.jackson.annotation.JsonInclude
import groovy.transform.ToString

/**
 * used to define a way to map rooms and other types to a item
*/
@ToString(includeNames = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
class SubItemGroup {
    String trcid // ID of this type (random)
    TRCItemCategories.Type type
    List<TRCItemCategories.Category> categories = []  // Categories/properties of the item
    List<SubItemTranslation> subItemTranslations
    List<File> media // media

    @ToString(includeNames = true)
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    static class SubItemTranslation {
        String lang, title
    }
}