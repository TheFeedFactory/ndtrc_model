package nl.ithelden.model.ndtrc;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.ArrayList;
import java.util.List;
import nl.ithelden.model.util.ToStringBuilder;
import org.joda.time.DateTime;

/**
 * Represents a group of TRC items, containing metadata common to the group,
 * such as TRC ID, creation/update/availability dates, ownership information,
 * workflow status, associated calendar, contact info, categories, files, details,
 * pricing, translations, promotions, and event links.
 */
public class TRCItemGroup {
    @JsonProperty private String trcid;
    @JsonProperty private DateTime creationdate;
    @JsonProperty private DateTime availablefrom;
    @JsonProperty private DateTime availableto;
    @JsonProperty private DateTime lastupdated;
    @JsonProperty private DateTime lastimportedon;

    @JsonProperty private String createdby;
    @JsonProperty private String lastupdatedby;
    @JsonProperty private String owner;
    @JsonProperty private String legalowner;

    @JsonProperty private String externalid;

    @JsonProperty private String validator;
    @JsonProperty private TRCItem.WFStatus wfstatus;
    @JsonProperty private TRCItem.EntityType entitytype;

    @JsonProperty private Calendar calendar;
    @JsonProperty private Contactinfo contactinfo;
    @JsonProperty private TRCItemCategories trcItemCategories;
    @JsonProperty private List<File> files = new ArrayList<>();
    @JsonProperty private List<TRCItemDetail> trcItemDetails = new ArrayList<>();
    @JsonProperty private String keywords;
    @JsonProperty private String markers;
    @JsonProperty private String userorganisation;
    @JsonProperty private List<PriceElement> priceElements = new ArrayList<>();

    @JsonProperty private Translations translations = new Translations();

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    @JsonProperty private List<Promotion> promotions;

    private List<EventLink> eventLinks = new ArrayList<>();

    public String getTrcid() {
        return trcid;
    }

    public void setTrcid(String trcid) {
        this.trcid = trcid;
    }

    public DateTime getCreationdate() {
        return creationdate;
    }

    public void setCreationdate(DateTime creationdate) {
        this.creationdate = creationdate;
    }

    public DateTime getAvailablefrom() {
        return availablefrom;
    }

    public void setAvailablefrom(DateTime availablefrom) {
        this.availablefrom = availablefrom;
    }

    public DateTime getAvailableto() {
        return availableto;
    }

    public void setAvailableto(DateTime availableto) {
        this.availableto = availableto;
    }

    public DateTime getLastupdated() {
        return lastupdated;
    }

    public void setLastupdated(DateTime lastupdated) {
        this.lastupdated = lastupdated;
    }

    public DateTime getLastimportedon() {
        return lastimportedon;
    }

    public void setLastimportedon(DateTime lastimportedon) {
        this.lastimportedon = lastimportedon;
    }

    public String getCreatedby() {
        return createdby;
    }

    public void setCreatedby(String createdby) {
        this.createdby = createdby;
    }

    public String getLastupdatedby() {
        return lastupdatedby;
    }

    public void setLastupdatedby(String lastupdatedby) {
        this.lastupdatedby = lastupdatedby;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getLegalowner() {
        return legalowner;
    }

    public void setLegalowner(String legalowner) {
        this.legalowner = legalowner;
    }

    public String getExternalid() {
        return externalid;
    }

    public void setExternalid(String externalid) {
        this.externalid = externalid;
    }

    public String getValidator() {
        return validator;
    }

    public void setValidator(String validator) {
        this.validator = validator;
    }

    public TRCItem.WFStatus getWfstatus() {
        return wfstatus;
    }

    public void setWfstatus(TRCItem.WFStatus wfstatus) {
        this.wfstatus = wfstatus;
    }

    public TRCItem.EntityType getEntitytype() {
        return entitytype;
    }

    public void setEntitytype(TRCItem.EntityType entitytype) {
        this.entitytype = entitytype;
    }

    public Calendar getCalendar() {
        return calendar;
    }

    public void setCalendar(Calendar calendar) {
        this.calendar = calendar;
    }

    public Contactinfo getContactinfo() {
        return contactinfo;
    }

    public void setContactinfo(Contactinfo contactinfo) {
        this.contactinfo = contactinfo;
    }

    public TRCItemCategories getTrcItemCategories() {
        return trcItemCategories;
    }

    public void setTrcItemCategories(TRCItemCategories trcItemCategories) {
        this.trcItemCategories = trcItemCategories;
    }

    public List<File> getFiles() {
        return files;
    }

    public void setFiles(List<File> files) {
        this.files = files;
    }

    public List<TRCItemDetail> getTrcItemDetails() {
        return trcItemDetails;
    }

    public void setTrcItemDetails(List<TRCItemDetail> trcItemDetails) {
        this.trcItemDetails = trcItemDetails;
    }

    public String getKeywords() {
        return keywords;
    }

    public void setKeywords(String keywords) {
        this.keywords = keywords;
    }

    public String getMarkers() {
        return markers;
    }

    public void setMarkers(String markers) {
        this.markers = markers;
    }

    public String getUserorganisation() {
        return userorganisation;
    }

    public void setUserorganisation(String userorganisation) {
        this.userorganisation = userorganisation;
    }

    public List<PriceElement> getPriceElements() {
        return priceElements;
    }

    public void setPriceElements(List<PriceElement> priceElements) {
        this.priceElements = priceElements;
    }

    public Translations getTranslations() {
        return translations;
    }

    public void setTranslations(Translations translations) {
        this.translations = translations;
    }

    public List<Promotion> getPromotions() {
        return promotions;
    }

    public void setPromotions(List<Promotion> promotions) {
        this.promotions = promotions;
    }

    public List<EventLink> getEventLinks() {
        return eventLinks;
    }

    public void setEventLinks(List<EventLink> eventLinks) {
        this.eventLinks = eventLinks;
    }

    /**
     * Represents a link to an event associated with the TRC item group.
     */
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class EventLink {
        private String eventId;

        public String getEventId() {
            return eventId;
        }

        public void setEventId(String eventId) {
            this.eventId = eventId;
        }
    }

    @Override
    public String toString() {
        return new ToStringBuilder(TRCItemGroup.class, this)
                .add("trcid", trcid)
                .add("creationdate", creationdate)
                .add("availablefrom", availablefrom)
                .add("availableto", availableto)
                .add("lastupdated", lastupdated)
                .add("lastimportedon", lastimportedon)
                .add("createdby", createdby)
                .add("lastupdatedby", lastupdatedby)
                .add("owner", owner)
                .add("legalowner", legalowner)
                .add("externalid", externalid)
                .add("validator", validator)
                .add("wfstatus", wfstatus)
                .add("entitytype", entitytype)
                .add("calendar", calendar)
                .add("contactinfo", contactinfo)
                .add("trcItemCategories", trcItemCategories)
                .add("files", files)
                .add("trcItemDetails", trcItemDetails)
                .add("keywords", keywords)
                .add("markers", markers)
                .add("userorganisation", userorganisation)
                .add("priceElements", priceElements)
                .add("translations", translations)
                .add("promotions", promotions)
                .add("eventLinks", eventLinks)
                .build();
    }
}
