package nl.ithelden.model.ndtrc

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import groovy.transform.ToString

/**
 * used to define a way to map rooms and other types to a item
*/
@ToString(includeNames = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
class SubItemGroup {
    @JsonProperty String trcid // ID of this type (random)
    @JsonProperty TRCItemCategories.Type type
    @JsonProperty List<TRCItemCategories.Category> categories = []  // Categories/properties of the item
    @JsonProperty List<SubItemTranslation> subItemTranslations
    @JsonProperty List<File> media // media

    @ToString(includeNames = true)
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    static class SubItemTranslation {
        String lang, title
    }

    void cleanEmptyItems() {
        this.categories = this.categories?.findAll { TRCItemCategories.Category category ->
            if ("freetext".equalsIgnoreCase(category.datatype) && !category.value) {
                return false
            }
            if ("multichoice".equalsIgnoreCase(category.datatype) && !category.valueid) {
                return false
            }
            if ("choice".equalsIgnoreCase(category.datatype) && !category.valueid) {
                return false
            }

            return true
        }
    }
}