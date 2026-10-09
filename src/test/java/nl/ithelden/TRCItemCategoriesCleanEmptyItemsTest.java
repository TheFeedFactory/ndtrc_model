package nl.ithelden;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import nl.ithelden.model.ndtrc.TRCItemCategories;
import nl.ithelden.model.ndtrc.TRCItemCategories.Category.DataType;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class TRCItemCategoriesCleanEmptyItemsTest {
    private static TRCItemCategories.Category category(String catid, DataType datatype, String value, String valueid) {
        TRCItemCategories.Category category = new TRCItemCategories.Category();
        category.setCatid(catid);
        category.setDatatype(datatype);
        category.setValue(value);
        category.setValueid(valueid);
        return category;
    }

    private static List<TRCItemCategories.Category> clean(TRCItemCategories.Category... categories) {
        TRCItemCategories itemCategories = new TRCItemCategories();
        itemCategories.setCategories(new ArrayList<>(Arrays.asList(categories)));
        itemCategories.cleanEmptyItems();
        return itemCategories.getCategories();
    }

    @Test
    void dropsNullsAndCategoriesWithoutCatid() {
        Assertions.assertTrue(clean(null, category(null, null, "v", null), category(" ", DataType.yes, "v", null)).isEmpty());
        Assertions.assertEquals(1, clean(category("1.1", null, null, null)).size());
    }

    @Test
    void valueTypesNeedANonEmptyValue() {
        for (DataType type : new DataType[]{DataType.freetext, DataType.url, DataType.email, DataType.phone}) {
            Assertions.assertTrue(clean(category("1", type, null, null)).isEmpty(), type.name());
            Assertions.assertTrue(clean(category("1", type, "", null)).isEmpty(), type.name());
            // Groovy truth: a whitespace-only value is kept
            Assertions.assertEquals(1, clean(category("1", type, " ", null)).size(), type.name());
            Assertions.assertEquals(1, clean(category("1", type, "x", null)).size(), type.name());
        }
    }

    @Test
    void choiceNeedsAValueidAndMultichoiceNeedsValues() {
        Assertions.assertTrue(clean(category("1", DataType.choice, "x", null)).isEmpty());
        Assertions.assertTrue(clean(category("1", DataType.choice, "x", "")).isEmpty());
        Assertions.assertEquals(1, clean(category("1", DataType.choice, null, "v1")).size());

        TRCItemCategories.Category noValues = category("1", DataType.multichoice, "x", "v");
        Assertions.assertTrue(clean(noValues).isEmpty());
        TRCItemCategories.Category nullValues = category("1", DataType.multichoice, "x", "v");
        nullValues.setCategoryvalues(null);
        Assertions.assertTrue(clean(nullValues).isEmpty());
        TRCItemCategories.Category withValues = category("1", DataType.multichoice, null, null);
        withValues.getCategoryvalues().add(new TRCItemCategories.CategoryValue());
        Assertions.assertEquals(1, clean(withValues).size());
    }

    @Test
    void otherTypesAreKeptWithoutAValue() {
        for (DataType type : new DataType[]{DataType.yes, DataType.yesno, DataType.nullableyesno, DataType.integer, DataType.decimal, DataType.date, DataType.data}) {
            Assertions.assertEquals(1, clean(category("1", type, null, null)).size(), type.name());
        }
    }

    @Test
    void typesWithoutCatidAreDropped() {
        TRCItemCategories itemCategories = new TRCItemCategories();
        TRCItemCategories.Type blank = new TRCItemCategories.Type();
        blank.setCatid("  ");
        TRCItemCategories.Type kept = new TRCItemCategories.Type();
        kept.setCatid("t1");
        itemCategories.setTypes(new ArrayList<>(Arrays.asList(null, new TRCItemCategories.Type(), blank, kept)));

        itemCategories.cleanEmptyItems();

        Assertions.assertEquals(List.of(kept), itemCategories.getTypes());
    }

    @Test
    void nullListsBecomeFreshMutableEmptyLists() {
        TRCItemCategories itemCategories = new TRCItemCategories();
        itemCategories.setCategories(null);
        itemCategories.setTypes(null);

        itemCategories.cleanEmptyItems();

        Assertions.assertEquals(List.of(), itemCategories.getCategories());
        Assertions.assertEquals(List.of(), itemCategories.getTypes());
        itemCategories.getCategories().add(new TRCItemCategories.Category());
        itemCategories.getTypes().add(new TRCItemCategories.Type());
    }

    @Test
    void orderIsPreserved() {
        TRCItemCategories.Category a = category("a", null, null, null);
        TRCItemCategories.Category b = category("b", DataType.freetext, "", null);
        TRCItemCategories.Category c = category("c", DataType.freetext, "x", null);
        Assertions.assertEquals(List.of(a, c), clean(a, b, c));
    }
}
