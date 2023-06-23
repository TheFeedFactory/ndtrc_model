package nl.ithelden.model.ndtrc

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import groovy.transform.ToString
import org.joda.time.DateTime

@ToString(includeNames = true)
class TRCItemGroup {
    @JsonProperty String id
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
    @JsonProperty WFStatus wfstatus

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
    static class EventLink {
        String eventId
        String locationId // redundant, but convenient
    }

    enum WFStatus { draft, readyforvalidation, approved, rejected, deleted, archived }
}