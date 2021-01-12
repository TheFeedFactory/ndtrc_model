package nl.ithelden.model.ndtrc

import com.fasterxml.jackson.annotation.JsonProperty
import groovy.transform.ToString

/**
 * used to define a way to map rooms and other types to a item
*/
class SubItemGroup {
    @JsonProperty String trcid // ID of this type (random)
    @JsonProperty Type type
    @JsonProperty List<Category> categories = []  // Categories/properties of the item
    @JsonProperty List<SubItemTranslation> subItemTranslations
    @JsonProperty List<File> media // media

    static class SubItemTranslation {
        String lang, title
    }

    @ToString(includeNames = true)
    static class Type {
        @JsonProperty String catid // ID of the category
        @JsonProperty Boolean isDefault // Boolean flag indicating this is the "default" (or main) type
        @JsonProperty List<CategoryTranslation> categoryTranslations= []
    }

    @ToString(includeNames = true)
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
    static class CategoryValue {
        @JsonProperty String catid // ID of the category
    }

    @ToString(includeNames = true)
    static class CategoryTranslation {
        @JsonProperty String catid
        @JsonProperty String lang
        @JsonProperty String label
        @JsonProperty String unit
        @JsonProperty String value
        @JsonProperty String explanation
    }
}
