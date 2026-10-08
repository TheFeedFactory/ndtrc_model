package nl.ithelden.model.ndtrc;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.ArrayList;
import java.util.List;
import nl.ithelden.model.util.StringUtils;
import nl.ithelden.model.util.ToStringBuilder;

/**
 * Represents the categorization of a TRC item. Contains lists of item types (e.g., Hotel, Event)
 * and categories (properties/attributes), along with boolean flags indicating if the item
 * is sold out or cancelled. Includes a method to clean up empty or invalid category entries.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TRCItemCategories {

    private List<Type> types = new ArrayList<>();  // Type indication of the item (Hotel, Camping Site, ...)
    private List<Category> categories = new ArrayList<>();  // Categories/properties of the item
    private Boolean soldout;  // Boolean flag indicating the item is soldout
    private Boolean canceled; // Boolean flag indicating the item is cancelled

    public List<Type> getTypes() {
        return types;
    }

    public void setTypes(List<Type> types) {
        this.types = types;
    }

    public List<Category> getCategories() {
        return categories;
    }

    public void setCategories(List<Category> categories) {
        this.categories = categories;
    }

    public Boolean getSoldout() {
        return soldout;
    }

    public Boolean isSoldout() {
        return soldout;
    }

    public void setSoldout(Boolean soldout) {
        this.soldout = soldout;
    }

    public Boolean getCanceled() {
        return canceled;
    }

    public Boolean isCanceled() {
        return canceled;
    }

    public void setCanceled(Boolean canceled) {
        this.canceled = canceled;
    }

    /**
     * Represents the type of the TRC item (e.g., Hotel, Museum).
     * Includes the category ID, a flag for the default type, and translations.
     */
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    public static class Type {
        private String catid; // ID of the category
        private Boolean isDefault; // Boolean flag indicating this is the "default" (or main) type
        private List<CategoryTranslation> categoryTranslations = new ArrayList<>();

        public String getCatid() {
               return catid;
        }

        public void setCatid(String catid) {
               this.catid = catid;
        }

        public Boolean getIsDefault() {
               return isDefault;
        }

        public Boolean isIsDefault() {
               return isDefault;
        }

        public void setIsDefault(Boolean isDefault) {
               this.isDefault = isDefault;
        }

        public List<CategoryTranslation> getCategoryTranslations() {
               return categoryTranslations;
        }

        public void setCategoryTranslations(List<CategoryTranslation> categoryTranslations) {
               this.categoryTranslations = categoryTranslations;
        }

        @Override
        public String toString() {
               return new ToStringBuilder(Type.class, this)
                       .add("catid", catid)
                       .add("isDefault", isDefault)
                       .add("categoryTranslations", categoryTranslations)
                       .build();
        }
    }

    /**
     * Represents a specific category or property of the TRC item (e.g., Wi-Fi, Accessibility).
     * Includes category ID, value ID (for choice types), value, default value, data type,
     * potential sub-values (`CategoryValue`), and various translations.
     */
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    public static class Category {
        private String catid;   // ID of the category
        private String valueid;  // ValueID of the category (when applicable). Used e.g. with categories of type choice or multichoice.
        private String value;  // Value of the category (when applicable)
        private String defaultValue;  // Default value if no value is set
        private DataType datatype; // Datatype of the category
        private List<CategoryValue> categoryvalues = new ArrayList<>();  // Categories/properties of the item
        private List<CategoryTranslation> categoryTranslations = new ArrayList<>();  // translations
        private List<CategoryTranslation> parentCategoryTranslations = new ArrayList<>();  // translations
        private List<CategoryTranslation> valueCategoryTranslations = new ArrayList<>();  // translations

        public String getCatid() {
            return catid;
        }

        public void setCatid(String catid) {
            this.catid = catid;
        }

        public String getValueid() {
            return valueid;
        }

        public void setValueid(String valueid) {
            this.valueid = valueid;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }

        public String getDefaultValue() {
            return defaultValue;
        }

        public void setDefaultValue(String defaultValue) {
            this.defaultValue = defaultValue;
        }

        public DataType getDatatype() {
            return datatype;
        }

        public void setDatatype(DataType datatype) {
            this.datatype = datatype;
        }

        public List<CategoryValue> getCategoryvalues() {
            return categoryvalues;
        }

        public void setCategoryvalues(List<CategoryValue> categoryvalues) {
            this.categoryvalues = categoryvalues;
        }

        public List<CategoryTranslation> getCategoryTranslations() {
            return categoryTranslations;
        }

        public void setCategoryTranslations(List<CategoryTranslation> categoryTranslations) {
            this.categoryTranslations = categoryTranslations;
        }

        public List<CategoryTranslation> getParentCategoryTranslations() {
            return parentCategoryTranslations;
        }

        public void setParentCategoryTranslations(List<CategoryTranslation> parentCategoryTranslations) {
            this.parentCategoryTranslations = parentCategoryTranslations;
        }

        public List<CategoryTranslation> getValueCategoryTranslations() {
            return valueCategoryTranslations;
        }

        public void setValueCategoryTranslations(List<CategoryTranslation> valueCategoryTranslations) {
            this.valueCategoryTranslations = valueCategoryTranslations;
        }

        public enum DataType {
            yes, yesno, nullableyesno, choice, multichoice, freetext, integer, decimal, date, data, url, email, phone;

            // Groovy gives every enum these members; kept so the 1.x API is unchanged.
            public static final DataType MIN_VALUE = yes;
            public static final DataType MAX_VALUE = phone;

            public DataType next() {
                DataType[] values = values();
                int ordinal = ordinal() + 1;
                return values[ordinal >= values.length ? 0 : ordinal];
            }

            public DataType previous() {
                DataType[] values = values();
                int ordinal = ordinal() - 1;
                return values[ordinal < 0 ? values.length - 1 : ordinal];
            }
        }

        @Override
        public String toString() {
            return new ToStringBuilder(Category.class, this)
                    .add("catid", catid)
                    .add("valueid", valueid)
                    .add("value", value)
                    .add("defaultValue", defaultValue)
                    .add("datatype", datatype)
                    .add("categoryvalues", categoryvalues)
                    .add("categoryTranslations", categoryTranslations)
                    .add("parentCategoryTranslations", parentCategoryTranslations)
                    .add("valueCategoryTranslations", valueCategoryTranslations)
                    .build();
        }
    }

    /**
     * Represents a specific value within a category, often used for multi-choice categories.
     * Includes the category ID, the value itself, and translations.
     */
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    public static class CategoryValue {
        private String catid; // ID of the category
        private String value;
        private List<CategoryTranslation> categorytranslations = new ArrayList<>();  // translations

        public String getCatid() {
            return catid;
        }

        public void setCatid(String catid) {
            this.catid = catid;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }

        public List<CategoryTranslation> getCategorytranslations() {
            return categorytranslations;
        }

        public void setCategorytranslations(List<CategoryTranslation> categorytranslations) {
            this.categorytranslations = categorytranslations;
        }

        @Override
        public String toString() {
            return new ToStringBuilder(CategoryValue.class, this)
                    .add("catid", catid)
                    .add("value", value)
                    .add("categorytranslations", categorytranslations)
                    .build();
        }
    }

    /**
     * Represents a translation related to a category, type, or category value.
     * Includes the category ID, language, label, unit, value, and explanation.
     */
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    public static class CategoryTranslation {
        private String catid;
        private String lang;
        private String label;
        private String unit;
        private String value;
        private String explanation;

        public String getCatid() {
            return catid;
        }

        public void setCatid(String catid) {
            this.catid = catid;
        }

        public String getLang() {
            return lang;
        }

        public void setLang(String lang) {
            this.lang = lang;
        }

        public String getLabel() {
            return label;
        }

        public void setLabel(String label) {
            this.label = label;
        }

        public String getUnit() {
            return unit;
        }

        public void setUnit(String unit) {
            this.unit = unit;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }

        public String getExplanation() {
            return explanation;
        }

        public void setExplanation(String explanation) {
            this.explanation = explanation;
        }

        @Override
        public String toString() {
            return new ToStringBuilder(CategoryTranslation.class, this)
                    .add("catid", catid)
                    .add("lang", lang)
                    .add("label", label)
                    .add("unit", unit)
                    .add("value", value)
                    .add("explanation", explanation)
                    .build();
        }
    }

    public void cleanEmptyItems() {
        List<Category> keptCategories = new ArrayList<>();
        if (this.categories != null) {
            for (Category category : this.categories) {
                if (keepCategory(category)) {
                    keptCategories.add(category);
                }
            }
        }
        this.categories = keptCategories;

        List<Type> keptTypes = new ArrayList<>();
        if (this.types != null) {
            for (Type type : this.types) {
                if (type != null && !StringUtils.isEmpty(type.getCatid())) {
                    keptTypes.add(type);
                }
            }
        }
        this.types = keptTypes;
    }

    private static boolean keepCategory(Category category) {
        if (category == null || StringUtils.isEmpty(category.getCatid())) return false;

        String datatype = category.getDatatype() == null ? null : category.getDatatype().toString();
        if ("freetext".equalsIgnoreCase(datatype) && isNullOrEmpty(category.getValue())) {
            return false;
        }
        if ("url".equalsIgnoreCase(datatype) && isNullOrEmpty(category.getValue())) {
            return false;
        }
        if ("email".equalsIgnoreCase(datatype) && isNullOrEmpty(category.getValue())) {
            return false;
        }
        if ("phone".equalsIgnoreCase(datatype) && isNullOrEmpty(category.getValue())) {
            return false;
        }
        if ("multichoice".equalsIgnoreCase(datatype) && (category.getCategoryvalues() == null || category.getCategoryvalues().isEmpty())) {
            return false;
        }
        if ("choice".equalsIgnoreCase(datatype) && isNullOrEmpty(category.getValueid())) {
            return false;
        }

        return true;
    }

    // Groovy truth of a String in the 1.x code: null or "" is false, "  " is true
    private static boolean isNullOrEmpty(String value) {
        return value == null || value.isEmpty();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(TRCItemCategories.class, this)
                .add("types", types)
                .add("categories", categories)
                .add("soldout", soldout)
                .add("canceled", canceled)
                .build();
    }
}
