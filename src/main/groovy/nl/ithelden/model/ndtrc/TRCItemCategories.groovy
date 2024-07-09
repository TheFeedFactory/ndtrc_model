package nl.ithelden.model.ndtrc

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import groovy.transform.ToString
import nl.ithelden.model.util.StringUtils

/**
 * Categorisation of the object. The categorisation cotnains 4 subelements: types (indicating the type of the item),
 * categories (indicating the properties of the item) soldout (indicating whether the items are soldout) and
 * cancelled (indication whether the items are cancelled).
 */
@ToString(includeNames = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
class TRCItemCategories {

    @JsonProperty List<Type> types = []  // Type indication of the item (Hotel, Camping Site, ...)
    @JsonProperty List<Category> categories = []  // Categories/properties of the item
    @JsonProperty Boolean soldout  // Boolean flag indicating the item is soldout
    @JsonProperty Boolean canceled // Boolean flag indicating the item is cancelled

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

        enum DataType { yesno, nullableyesno, choice, multichoice, freetext, integer, decimal, date }
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
            if (!category || StringUtils.isEmpty(category.catid)) return false

            if ("freetext".equalsIgnoreCase(category.datatype) && !category.value) {
                return false
            }
            if ("multichoice".equalsIgnoreCase(category.datatype) && !category.categoryvalues) {
                return false
            }
            if ("choice".equalsIgnoreCase(category.datatype) && !category.valueid) {
                return false
            }

            return true
        } ?: []

        this.types = this.types?.findAll { Type type ->
            return type && !StringUtils.isEmpty(type.catid)
        } ?: []
    }
}