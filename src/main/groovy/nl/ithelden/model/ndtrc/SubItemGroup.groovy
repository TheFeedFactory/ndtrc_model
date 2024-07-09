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
    @JsonProperty Type type
    @JsonProperty List<Category> categories = []  // Categories/properties of the item
    @JsonProperty List<SubItemTranslation> subItemTranslations
    @JsonProperty List<File> media // media

    @ToString(includeNames = true)
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    static class SubItemTranslation {
        String lang, title
    }

    @ToString(includeNames = true)
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    static class Type {
        @JsonProperty String catid // ID of the category
        @JsonProperty Boolean isDefault // Boolean flag indicating this is the "default" (or main) type
        @JsonProperty List<CategoryTranslation> categoryTranslations= []
    }

    @ToString(includeNames = true)
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    static class Category {
        @JsonProperty String catid   // ID of the category
        @JsonProperty String valueid  // ValueID of the category (when applicable). Used e.g. with categories of type choice or multichoice.
        @JsonProperty String value  // Value of the category (when applicable)
        @JsonProperty String datatype // Datatype of the category
        @JsonProperty List<CategoryValue> categoryvalues = []  // Categories/properties of the item
        @JsonProperty List<CategoryTranslation> categoryTranslations = []  // translations
        @JsonProperty List<CategoryTranslation> parentCategoryTranslations = []  // translations
        @JsonProperty List<CategoryTranslation> valueCategoryTranslations = []  // translations

        // enum DataType { yesno, nullableyesno, choice, multichoice, freetext, integer, decimal, date }
    }

    @ToString(includeNames = true)
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    static class CategoryValue {
        @JsonProperty String catid // ID of the category
        @JsonProperty List<CategoryTranslation> categorytranslations = []  // translations
    }

    @ToString(includeNames = true)
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    static class CategoryTranslation {
        @JsonProperty String catid
        @JsonProperty String lang
        @JsonProperty String label
        @JsonProperty String unit
        @JsonProperty String value
        @JsonProperty String explanation
    }

    void cleanEmptyItems() {
        this.categories = this.categories?.findAll { Category category ->
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