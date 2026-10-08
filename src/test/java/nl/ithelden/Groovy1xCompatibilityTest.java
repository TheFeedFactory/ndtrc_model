package nl.ithelden;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.joda.JodaModule;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import nl.ithelden.model.FetchedEntry;
import nl.ithelden.model.ndtrc.Address;
import nl.ithelden.model.ndtrc.Calendar;
import nl.ithelden.model.ndtrc.Promotion;
import nl.ithelden.model.ndtrc.RouteInfo;
import nl.ithelden.model.ndtrc.TRCItem;
import nl.ithelden.model.ndtrc.TRCItemCategories;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * Pins the parts of the 1.x API that came from Groovy rather than from the source text, so a
 * later refactor (Lombok, records, an IDE "generate accessors") cannot silently drop them.
 * The expected strings were captured from ff-model 1.7.0.
 */
class Groovy1xCompatibilityTest {

    @Test
    void groovyIsNotOnTheClasspath() {
        Assertions.assertThrows(ClassNotFoundException.class, () -> Class.forName("groovy.lang.GroovyObject"));
    }

    @Test
    void booleanPropertiesHaveBothGetAndIsAccessors() throws Exception {
        // primitive boolean
        Assertions.assertEquals(boolean.class, Promotion.class.getMethod("getEnabled").getReturnType());
        Assertions.assertEquals(boolean.class, Promotion.class.getMethod("isEnabled").getReturnType());
        Assertions.assertEquals(boolean.class, Calendar.class.getMethod("getExcludeholidays").getReturnType());
        // wrapper Boolean gets an is-accessor returning Boolean too
        Assertions.assertEquals(Boolean.class, Calendar.class.getMethod("isAlwaysopen").getReturnType());
        Assertions.assertEquals(Boolean.class, TRCItem.class.getMethod("isPublished").getReturnType());
        // a field named isDefault yields getIsDefault/isIsDefault/setIsDefault
        Method getIsDefault = TRCItemCategories.Type.class.getMethod("getIsDefault");
        Method isIsDefault = TRCItemCategories.Type.class.getMethod("isIsDefault");
        Assertions.assertEquals(Boolean.class, getIsDefault.getReturnType());
        Assertions.assertEquals(Boolean.class, isIsDefault.getReturnType());
        Assertions.assertNotNull(TRCItemCategories.Type.class.getMethod("setIsDefault", Boolean.class));
    }

    @Test
    void whenHasNoBooleanIsAccessorForItsValidField() throws Exception {
        // isValid() is the computed check; the `valid` field only has getValid/setValid
        Assertions.assertEquals(boolean.class, Calendar.When.class.getMethod("isValid").getReturnType());
        Assertions.assertEquals(Boolean.class, Calendar.When.class.getMethod("getValid").getReturnType());
    }

    @Test
    void enumsKeepGroovysNextPreviousAndBounds() {
        Assertions.assertEquals(TRCItem.WFStatus.readyforvalidation, TRCItem.WFStatus.draft.next());
        Assertions.assertEquals(TRCItem.WFStatus.draft, TRCItem.WFStatus.archived.next(), "next() wraps around");
        Assertions.assertEquals(TRCItem.WFStatus.archived, TRCItem.WFStatus.draft.previous(), "previous() wraps around");
        Assertions.assertEquals(TRCItem.WFStatus.draft, TRCItem.WFStatus.MIN_VALUE);
        Assertions.assertEquals(TRCItem.WFStatus.archived, TRCItem.WFStatus.MAX_VALUE);
        Assertions.assertEquals(RouteInfo.RouteType.horse_riding, RouteInfo.RouteType.MAX_VALUE);
        Assertions.assertEquals(6, TRCItem.WFStatus.values().length, "the bounds are not extra constants");
    }

    @Test
    void normalizeYouTubeURLStillReturnsObject() throws Exception {
        // `static def` in 1.x; keeping the erased return type keeps callers compiled against 1.x linking
        Assertions.assertEquals(Object.class, nl.ithelden.model.ndtrc.File.class.getMethod("normalizeYouTubeURL", String.class).getReturnType());
    }

    @Test
    void toStringUsesTheGroovyFormat() {
        Address address = new Address();
        address.setCity("x");
        Assertions.assertEquals("nl.ithelden.model.ndtrc.Address(main:null, reservation:null, title:null, city:x, citytrcid:null, "
                + "country:NL, continent:null, housenr:null, street:null, streettrcid:null, zipcode:null, province:null, "
                + "neighbourhood:null, district:null, gisCoordinates:[], empty:false)", address.toString());

        TRCItemCategories.Type type = new TRCItemCategories.Type();
        type.setCatid("c");
        type.setIsDefault(true);
        Assertions.assertEquals("nl.ithelden.model.ndtrc.TRCItemCategories$Type(catid:c, isDefault:true, categoryTranslations:[])", type.toString());

        FetchedEntry entry = new FetchedEntry();
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("a", 1);
        data.put("b", "x");
        data.put("nested", new ArrayList<>(List.of(new LinkedHashMap<>())));
        entry.setData(data);
        Assertions.assertEquals("nl.ithelden.model.FetchedEntry(feed:null, sourceId:null, sourceUrl:null, errorMessage:null, label:null, "
                + "created:null, modified:null, externalId:null, data:[a:1, b:x, nested:[[:]]], externalLocationInfo:null)", entry.toString());
    }

    @Test
    void jsonShapeAndPropertyOrderAreUnchanged() throws Exception {
        ObjectMapper mapper = new ObjectMapper().registerModule(new JodaModule());
        Assertions.assertEquals("{\"trcid\":null,\"creationdate\":null,\"availablefrom\":null,\"availableto\":null,\"lastupdated\":null,"
                + "\"lastimportedon\":null,\"createdby\":null,\"lastupdatedby\":null,\"owner\":null,\"legalowner\":null,\"externalid\":null,"
                + "\"slug\":null,\"validator\":null,\"validatedby\":null,\"wfstatus\":null,\"cidn\":null,\"published\":null,\"deleted\":null,"
                + "\"offline\":null,\"isprivate\":null,\"entitytype\":null,\"productiontrcid\":null,\"calendar\":null,\"contactinfo\":null,"
                + "\"trcItemCategories\":null,\"performers\":[],\"files\":[],\"trcItemDetails\":[],\"trcitemRelation\":null,\"keywords\":null,"
                + "\"markers\":null,\"location\":null,\"locationRef\":null,\"userorganisation\":null,\"priceElements\":[],"
                + "\"extrapriceinformations\":[],\"translations\":{\"primaryLanguage\":\"nl\",\"availableLanguages\":[\"nl\"]},"
                + "\"forceoverwrite\":null}", mapper.writeValueAsString(new TRCItem()));
        Assertions.assertEquals("{\"singleDates\":[],\"patternDates\":[],\"opens\":[],\"closeds\":[],\"soldouts\":[],\"cancelleds\":[],"
                + "\"excludeholidays\":false,\"cancelled\":false,\"soldout\":false}", mapper.writeValueAsString(new Calendar()));
        Assertions.assertEquals("{\"product\":null,\"externalReference\":null,\"promotionType\":null,\"discount\":null,"
                + "\"discountValueRequired\":true,\"translations\":null,\"detailsUrls\":null,\"image\":null,\"enabled\":true,"
                + "\"restrictedToRegisteredUsers\":null,\"validityStrategy\":\"always\",\"eventRelativeDuration\":null,\"startDate\":null,"
                + "\"endDate\":null,\"opens\":null}", mapper.writeValueAsString(new Promotion()));
        Assertions.assertEquals("{\"catid\":\"c\",\"isDefault\":true}",
                mapper.writeValueAsString(mapper.readValue("{\"catid\":\"c\",\"isDefault\":true}", TRCItemCategories.Type.class)));
    }

    @Test
    void defaultCollectionsAreMutable() {
        TRCItem item = new TRCItem();
        item.getFiles().add(new nl.ithelden.model.ndtrc.File());
        item.getTranslations().getAvailableLanguages().add("en");
        Assertions.assertEquals(List.of("nl", "en"), item.getTranslations().getAvailableLanguages());
    }
}
