package nl.ithelden.model.ndtrc

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import groovy.transform.ToString
import org.joda.time.DateTime

/**
 * Represents a group of TRC items, containing metadata common to the group,
 * such as TRC ID, creation/update/availability dates, ownership information,
 * workflow status, associated calendar, contact info, categories, files, details,
 * pricing, translations, promotions, and event links.
 */
@ToString(includeNames = true)
class TRCItemGroup {
    @JsonProperty String trcid
    @JsonProperty DateTime creationdate
    @JsonProperty DateTime availablefrom
    @JsonProperty DateTime availableto
    @JsonProperty DateTime lastupdated
    @JsonProperty DateTime lastimportedon

    @JsonProperty String createdby
    @JsonProperty String lastupdatedby
    @JsonProperty String owner
    @JsonProperty String legalowner

    @JsonProperty String externalid

    @JsonProperty String validator
    @JsonProperty TRCItem.WFStatus wfstatus

    @JsonProperty Calendar calendar
    @JsonProperty Contactinfo contactinfo
    @JsonProperty TRCItemCategories trcItemCategories
    @JsonProperty List<File> files = []
    @JsonProperty List<TRCItemDetail> trcItemDetails = []
    @JsonProperty String keywords
    @JsonProperty String markers
    @JsonProperty String userorganisation
    @JsonProperty List<PriceElement> priceElements = []

    @JsonProperty Translations translations = new Translations()

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    @JsonProperty List<Promotion> promotions

    List<EventLink> eventLinks = []
    /**
     * Represents a link to an event associated with the TRC item group.
     */
    @JsonInclude(JsonInclude.Include.NON_NULL)
    static class EventLink {
        String eventId
    }
}