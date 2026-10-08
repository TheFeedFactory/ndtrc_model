package nl.ithelden.model.ndtrc;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import nl.ithelden.model.util.ToStringBuilder;

/**
 * Represents a location associated with a TRC item, including its address,
 * label, and a nested LocationItem with identifiers.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Location {
    @JsonProperty private Address address;
    @JsonProperty private String label;
    @JsonProperty private LocationItem locationItem;
    @JsonProperty private LocationItem venueItem;

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public LocationItem getLocationItem() {
        return locationItem;
    }

    public void setLocationItem(LocationItem locationItem) {
        this.locationItem = locationItem;
    }

    public LocationItem getVenueItem() {
        return venueItem;
    }

    public void setVenueItem(LocationItem venueItem) {
        this.venueItem = venueItem;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(Location.class, this)
                .add("address", address)
                .add("label", label)
                .add("locationItem", locationItem)
                .add("venueItem", venueItem)
                .build();
    }

    /**
     * Represents a specific item within a location, holding its ID, TRC ID, and text label.
     */
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class LocationItem {
        @JsonProperty private String id;
        @JsonProperty private String trcid;
        @JsonProperty private String text;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getTrcid() {
            return trcid;
        }

        public void setTrcid(String trcid) {
            this.trcid = trcid;
        }

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }

        @Override
        public String toString() {
            return new ToStringBuilder(LocationItem.class, this)
                    .add("id", id)
                    .add("trcid", trcid)
                    .add("text", text)
                    .build();
        }
    }
}
