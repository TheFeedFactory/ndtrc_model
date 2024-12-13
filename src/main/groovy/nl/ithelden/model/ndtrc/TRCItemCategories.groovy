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

    List<Type> types = []  // Type indication of the item (Hotel, Camping Site, ...)
    List<Category> categories = []  // Categories/properties of the item
    Boolean soldout  // Boolean flag indicating the item is soldout
    Boolean canceled // Boolean flag indicating the item is cancelled

    @ToString(includeNames = true)
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    static class Type {
       String catid // ID of the category
       Boolean isDefault // Boolean flag indicating this is the "default" (or main) type
       List<CategoryTranslation> categoryTranslations= []
    }

    @ToString(includeNames = true)
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    static class Category {
        String catid   // ID of the category
        String valueid  // ValueID of the category (when applicable). Used e.g. with categories of type choice or multichoice.
        String value  // Value of the category (when applicable)
        String defaultValue  // Default value if no value is set
        DataType datatype // Datatype of the category
        List<CategoryValue> categoryvalues = []  // Categories/properties of the item
        List<CategoryTranslation> categoryTranslations = []  // translations
        List<CategoryTranslation> parentCategoryTranslations = []  // translations
        List<CategoryTranslation> valueCategoryTranslations = []  // translations

        enum DataType { yes, yesno, nullableyesno, choice, multichoice, freetext, integer, decimal, date }
    }

    @ToString(includeNames = true)
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    static class CategoryValue {
        String catid // ID of the category
        String value
        List<CategoryTranslation> categorytranslations = []  // translations
    }

    @ToString(includeNames = true)
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    static class CategoryTranslation {
        String catid
        String lang
        String label
        String unit
        String value
        String explanation
    }

    void cleanEmptyItems() {
        this.categories = this.categories?.findAll { Category category ->
            if (!category || StringUtils.isEmpty(category.catid)) return false

            if ("freetext".equalsIgnoreCase(category.datatype?.toString()) && !category.value) {
                return false
            }
            if ("multichoice".equalsIgnoreCase(category.datatype?.toString()) && !category.categoryvalues) {
                return false
            }
            if ("choice".equalsIgnoreCase(category.datatype?.toString()) && !category.valueid) {
                return false
            }

            return true
        } ?: []

        this.types = this.types?.findAll { Type type ->
            return type && !StringUtils.isEmpty(type.catid)
        } ?: []
    }
}