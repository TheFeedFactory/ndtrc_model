package nl.ithelden.model.ndtrc

import com.fasterxml.jackson.annotation.JsonInclude
import groovy.transform.ToString
import org.joda.time.DateTime

/**
 * Represents a TRC item.
 */
@ToString(includeNames = true)
class TRCItem {
    String trcid
    DateTime creationdate
    DateTime availablefrom
    DateTime availableto
    DateTime lastupdated
    DateTime lastimportedon

    String createdby
    String lastupdatedby
    String owner
    String legalowner

    String externalid
    String slug

    String validator
    String validatedby // deprecated
    WFStatus wfstatus

    String cidn
    Boolean published
    Boolean deleted
    Boolean offline // deprecated
    Boolean isprivate //  deprecated

    EntityType entitytype
    String productiontrcid

    Calendar calendar
    Contactinfo contactinfo
    TRCItemCategories trcItemCategories
    List<Performer> performers = []
    List<File> files = []
    List<TRCItemDetail> trcItemDetails = []
    TrcitemRelation trcitemRelation
    String keywords
    String markers
    Location location
    String locationRef
    String userorganisation
    List<PriceElement> priceElements = []
    List<ExtraPriceInformation> extrapriceinformations = []

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    RouteInfo routeInfo

    Translations translations = new Translations()

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    List<Promotion> promotions

    enum WFStatus { draft, readyforvalidation, approved, rejected, deleted, archived }
    enum EntityType { EVENEMENT, LOCATIE, EVENEMENTGROEP, PLAATSREGIO , ROUTE }

    @ToString(includeNames = true)
    static class Category {
        String id
        List<Translation> translations = []

        static class Translation {
            String lang, text
        }
    }

    // below are workflow related fields, they are not part of the XSD of TRItem
    Boolean forceoverwrite
}