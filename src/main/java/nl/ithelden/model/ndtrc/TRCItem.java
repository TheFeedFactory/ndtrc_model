package nl.ithelden.model.ndtrc;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.ArrayList;
import java.util.List;
import nl.ithelden.model.util.ToStringBuilder;
import org.joda.time.DateTime;

/**
 * Represents a core TRC (Tourist Registration Core) item, such as an event, location, or route.
 * Contains extensive metadata including identifiers, dates, ownership, status, type,
 * calendar, contact info, categories, performers, files, details, relations, pricing,
 * location, translations, promotions, and route information.
 */
public class TRCItem {
    private String trcid;
    private DateTime creationdate;
    private DateTime availablefrom;
    private DateTime availableto;
    private DateTime lastupdated;
    private DateTime lastimportedon;

    private String createdby;
    private String lastupdatedby;
    private String owner;
    private String legalowner;

    private String externalid;
    private String slug;

    private String validator;
    private String validatedby; // deprecated
    private WFStatus wfstatus;

    private String cidn;
    private Boolean published;
    private Boolean deleted;
    private Boolean offline; // deprecated
    private Boolean isprivate; //  deprecated

    private EntityType entitytype;
    private String productiontrcid;

    private Calendar calendar;
    private Contactinfo contactinfo;
    private TRCItemCategories trcItemCategories;
    private List<Performer> performers = new ArrayList<>();
    private List<File> files = new ArrayList<>();
    private List<TRCItemDetail> trcItemDetails = new ArrayList<>();
    private TrcitemRelation trcitemRelation;
    private String keywords;
    private String markers;
    private Location location;
    private String locationRef;
    private String userorganisation;
    private List<PriceElement> priceElements = new ArrayList<>();
    private List<ExtraPriceInformation> extrapriceinformations = new ArrayList<>();

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private RouteInfo routeInfo;

    private Translations translations = new Translations();

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private List<Promotion> promotions;

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private SeoMetadata seoMetadata;

    public enum WFStatus {
        draft, readyforvalidation, approved, rejected, deleted, archived;

        // Groovy gives every enum these members; kept so the 1.x API is unchanged.
        public static final WFStatus MIN_VALUE = draft;
        public static final WFStatus MAX_VALUE = archived;

        public WFStatus next() {
            WFStatus[] values = values();
            int ordinal = ordinal() + 1;
            return values[ordinal >= values.length ? 0 : ordinal];
        }

        public WFStatus previous() {
            WFStatus[] values = values();
            int ordinal = ordinal() - 1;
            return values[ordinal < 0 ? values.length - 1 : ordinal];
        }
    }

    public enum EntityType {
        EVENEMENT, LOCATIE, EVENTGROUP, ROUTE, VENUE;

        // Groovy gives every enum these members; kept so the 1.x API is unchanged.
        public static final EntityType MIN_VALUE = EVENEMENT;
        public static final EntityType MAX_VALUE = VENUE;

        public EntityType next() {
            EntityType[] values = values();
            int ordinal = ordinal() + 1;
            return values[ordinal >= values.length ? 0 : ordinal];
        }

        public EntityType previous() {
            EntityType[] values = values();
            int ordinal = ordinal() - 1;
            return values[ordinal < 0 ? values.length - 1 : ordinal];
        }
    }

    /**
     * Represents a category associated with the TRC item, including an ID and translations.
     */
    public static class Category {
        private String id;
        private List<Translation> translations = new ArrayList<>();

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public List<Translation> getTranslations() {
            return translations;
        }

        public void setTranslations(List<Translation> translations) {
            this.translations = translations;
        }

        @Override
        public String toString() {
            return new ToStringBuilder(Category.class, this)
                    .add("id", id)
                    .add("translations", translations)
                    .build();
        }

        /**
         * Represents a translation for a category name in a specific language.
         */
        public static class Translation {
            private String lang;
            private String text;

            public String getLang() {
                return lang;
            }

            public void setLang(String lang) {
                this.lang = lang;
            }

            public String getText() {
                return text;
            }

            public void setText(String text) {
                this.text = text;
            }
        }
    }

    // below are workflow related fields, they are not part of the XSD of TRItem
    private Boolean forceoverwrite;

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

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public String getValidator() {
        return validator;
    }

    public void setValidator(String validator) {
        this.validator = validator;
    }

    public String getValidatedby() {
        return validatedby;
    }

    public void setValidatedby(String validatedby) {
        this.validatedby = validatedby;
    }

    public WFStatus getWfstatus() {
        return wfstatus;
    }

    public void setWfstatus(WFStatus wfstatus) {
        this.wfstatus = wfstatus;
    }

    public String getCidn() {
        return cidn;
    }

    public void setCidn(String cidn) {
        this.cidn = cidn;
    }

    public Boolean getPublished() {
        return published;
    }

    public Boolean isPublished() {
        return published;
    }

    public void setPublished(Boolean published) {
        this.published = published;
    }

    public Boolean getDeleted() {
        return deleted;
    }

    public Boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }

    public Boolean getOffline() {
        return offline;
    }

    public Boolean isOffline() {
        return offline;
    }

    public void setOffline(Boolean offline) {
        this.offline = offline;
    }

    public Boolean getIsprivate() {
        return isprivate;
    }

    public Boolean isIsprivate() {
        return isprivate;
    }

    public void setIsprivate(Boolean isprivate) {
        this.isprivate = isprivate;
    }

    public EntityType getEntitytype() {
        return entitytype;
    }

    public void setEntitytype(EntityType entitytype) {
        this.entitytype = entitytype;
    }

    public String getProductiontrcid() {
        return productiontrcid;
    }

    public void setProductiontrcid(String productiontrcid) {
        this.productiontrcid = productiontrcid;
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

    public List<Performer> getPerformers() {
        return performers;
    }

    public void setPerformers(List<Performer> performers) {
        this.performers = performers;
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

    public TrcitemRelation getTrcitemRelation() {
        return trcitemRelation;
    }

    public void setTrcitemRelation(TrcitemRelation trcitemRelation) {
        this.trcitemRelation = trcitemRelation;
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

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public String getLocationRef() {
        return locationRef;
    }

    public void setLocationRef(String locationRef) {
        this.locationRef = locationRef;
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

    public List<ExtraPriceInformation> getExtrapriceinformations() {
        return extrapriceinformations;
    }

    public void setExtrapriceinformations(List<ExtraPriceInformation> extrapriceinformations) {
        this.extrapriceinformations = extrapriceinformations;
    }

    public RouteInfo getRouteInfo() {
        return routeInfo;
    }

    public void setRouteInfo(RouteInfo routeInfo) {
        this.routeInfo = routeInfo;
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

    public SeoMetadata getSeoMetadata() {
        return seoMetadata;
    }

    public void setSeoMetadata(SeoMetadata seoMetadata) {
        this.seoMetadata = seoMetadata;
    }

    public Boolean getForceoverwrite() {
        return forceoverwrite;
    }

    public Boolean isForceoverwrite() {
        return forceoverwrite;
    }

    public void setForceoverwrite(Boolean forceoverwrite) {
        this.forceoverwrite = forceoverwrite;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(TRCItem.class, this)
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
                .add("slug", slug)
                .add("validator", validator)
                .add("validatedby", validatedby)
                .add("wfstatus", wfstatus)
                .add("cidn", cidn)
                .add("published", published)
                .add("deleted", deleted)
                .add("offline", offline)
                .add("isprivate", isprivate)
                .add("entitytype", entitytype)
                .add("productiontrcid", productiontrcid)
                .add("calendar", calendar)
                .add("contactinfo", contactinfo)
                .add("trcItemCategories", trcItemCategories)
                .add("performers", performers)
                .add("files", files)
                .add("trcItemDetails", trcItemDetails)
                .add("trcitemRelation", trcitemRelation)
                .add("keywords", keywords)
                .add("markers", markers)
                .add("location", location)
                .add("locationRef", locationRef)
                .add("userorganisation", userorganisation)
                .add("priceElements", priceElements)
                .add("extrapriceinformations", extrapriceinformations)
                .add("routeInfo", routeInfo)
                .add("translations", translations)
                .add("promotions", promotions)
                .add("seoMetadata", seoMetadata)
                .add("forceoverwrite", forceoverwrite)
                .build();
    }
}
