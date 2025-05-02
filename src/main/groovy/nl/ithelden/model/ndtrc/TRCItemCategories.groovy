package nl.ithelden.model.ndtrc

import com.fasterxml.jackson.annotation.JsonInclude
import groovy.transform.ToString
import nl.ithelden.model.util.StringUtils

/**
 * Represents the categorization of a TRC item. Contains lists of item types (e.g., Hotel, Event)
 * and categories (properties/attributes), along with boolean flags indicating if the item
 * is sold out or cancelled. Includes a method to clean up empty or invalid category entries.
 */
@ToString(includeNames = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
class TRCItemCategories {

    List<Type> types = []  // Type indication of the item (Hotel, Camping Site, ...)
    List<Category> categories = []  // Categories/properties of the item
    Boolean soldout  // Boolean flag indicating the item is soldout
    Boolean canceled // Boolean flag indicating the item is cancelled

    /**
     * Represents the type of the TRC item (e.g., Hotel, Museum).
     * Includes the category ID, a flag for the default type, and translations.
     */
    @ToString(includeNames = true)
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    static class Type {
       String catid // ID of the category
       Boolean isDefault // Boolean flag indicating this is the "default" (or main) type
       List<CategoryTranslation> categoryTranslations= []
    }

    /**
     * Represents a specific category or property of the TRC item (e.g., Wi-Fi, Accessibility).
     * Includes category ID, value ID (for choice types), value, default value, data type,
     * potential sub-values (`CategoryValue`), and various translations.
     */
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

    /**
     * Represents a specific value within a category, often used for multi-choice categories.
     * Includes the category ID, the value itself, and translations.
     */
    @ToString(includeNames = true)
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    static class CategoryValue {
        String catid // ID of the category
        String value
        List<CategoryTranslation> categorytranslations = []  // translations
    }

    /**
     * Represents a translation related to a category, type, or category value.
     * Includes the category ID, language, label, unit, value, and explanation.
     */
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