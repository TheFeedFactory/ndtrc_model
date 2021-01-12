package nl.ithelden.model.ndtrc

import com.fasterxml.jackson.annotation.JsonProperty
import groovy.transform.ToString

/**
 * Categorisation of the object. The categorisation cotnains 4 subelements: types (indicating the type of the item),
 * categories (indicating the properties of the item) soldout (indicating whether the items are soldout) and
 * cancelled (indication whether the items are cancelled).
 */
@ToString(includeNames = true)
class TRCItemCategories {

    @JsonProperty List<Type> types = []  // Type indication of the item (Hotel, Camping Site, ...)
    @JsonProperty List<Category> categories = []  // Categories/properties of the item
    @JsonProperty Boolean soldout  // Boolean flag indicating the item is soldout
    @JsonProperty Boolean canceled // Boolean flag indicating the item is cancelled

    @ToString(includeNames = true)
    static class Type {
       @JsonProperty String catid // ID of the category
       @JsonProperty Boolean isDefault // Boolean flag indicating this is the "default" (or main) type
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