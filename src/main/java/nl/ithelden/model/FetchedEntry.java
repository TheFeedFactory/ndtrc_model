package nl.ithelden.model;


import java.util.Map;
import nl.ithelden.model.util.ToStringBuilder;
import org.joda.time.DateTime;

/**
 * Represents an entry fetched from an external feed.
 * Contains information about the source feed, identifiers, timestamps,
 * the raw data, and potentially structured location information.
 * Can also include an error message if fetching failed.
 */
public class FetchedEntry {
    private String feed;
    private String sourceId;
    private String sourceUrl;
    private String errorMessage;

    private String label;
    private DateTime created; // http://purl.org/dc/terms/created
    private DateTime modified; // http://purl.org/dc/terms/modified

    private String externalId;
    private Map<String, Object> data;
    private ExternalLocationInfo externalLocationInfo;

    public String getFeed() {
        return feed;
    }

    public void setFeed(String feed) {
        this.feed = feed;
    }

    public String getSourceId() {
        return sourceId;
    }

    public void setSourceId(String sourceId) {
        this.sourceId = sourceId;
    }

    public String getSourceUrl() {
        return sourceUrl;
    }

    public void setSourceUrl(String sourceUrl) {
        this.sourceUrl = sourceUrl;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public DateTime getCreated() {
        return created;
    }

    public void setCreated(DateTime created) {
        this.created = created;
    }

    public DateTime getModified() {
        return modified;
    }

    public void setModified(DateTime modified) {
        this.modified = modified;
    }

    public String getExternalId() {
        return externalId;
    }

    public void setExternalId(String externalId) {
        this.externalId = externalId;
    }

    public Map<String, Object> getData() {
        return data;
    }

    public void setData(Map<String, Object> data) {
        this.data = data;
    }

    public ExternalLocationInfo getExternalLocationInfo() {
        return externalLocationInfo;
    }

    public void setExternalLocationInfo(ExternalLocationInfo externalLocationInfo) {
        this.externalLocationInfo = externalLocationInfo;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(FetchedEntry.class, this)
                .add("feed", feed)
                .add("sourceId", sourceId)
                .add("sourceUrl", sourceUrl)
                .add("errorMessage", errorMessage)
                .add("label", label)
                .add("created", created)
                .add("modified", modified)
                .add("externalId", externalId)
                .add("data", data)
                .add("externalLocationInfo", externalLocationInfo)
                .build();
    }

    /**
     * Holds structured information about the location associated with the fetched entry,
     * including various identifiers (external, TRC, Feed Factory) and address details.
     */
    public static class ExternalLocationInfo {
        private String externalId; // location id used in feeds
        private String locationName; //name of location in original feed
        private String address;
        private String housenr;
        private String zipcode;
        private String city;
        private String latitude;
        private String longitude;
        private String trcid; // trcid as known in the TRC
        private String ffId; // location id in FF

        public String getExternalId() {
            return externalId;
        }

        public void setExternalId(String externalId) {
            this.externalId = externalId;
        }

        public String getLocationName() {
            return locationName;
        }

        public void setLocationName(String locationName) {
            this.locationName = locationName;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getHousenr() {
            return housenr;
        }

        public void setHousenr(String housenr) {
            this.housenr = housenr;
        }

        public String getZipcode() {
            return zipcode;
        }

        public void setZipcode(String zipcode) {
            this.zipcode = zipcode;
        }

        public String getCity() {
            return city;
        }

        public void setCity(String city) {
            this.city = city;
        }

        public String getLatitude() {
            return latitude;
        }

        public void setLatitude(String latitude) {
            this.latitude = latitude;
        }

        public String getLongitude() {
            return longitude;
        }

        public void setLongitude(String longitude) {
            this.longitude = longitude;
        }

        public String getTrcid() {
            return trcid;
        }

        public void setTrcid(String trcid) {
            this.trcid = trcid;
        }

        public String getFfId() {
            return ffId;
        }

        public void setFfId(String ffId) {
            this.ffId = ffId;
        }

        @Override
        public String toString() {
            return new ToStringBuilder(ExternalLocationInfo.class, this)
                    .add("externalId", externalId)
                    .add("locationName", locationName)
                    .add("address", address)
                    .add("housenr", housenr)
                    .add("zipcode", zipcode)
                    .add("city", city)
                    .add("latitude", latitude)
                    .add("longitude", longitude)
                    .add("trcid", trcid)
                    .add("ffId", ffId)
                    .build();
        }
    }
}
