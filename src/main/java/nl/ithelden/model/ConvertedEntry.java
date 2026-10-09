package nl.ithelden.model;


import nl.ithelden.model.ndtrc.TRCItem;
import nl.ithelden.model.util.ToStringBuilder;
import org.joda.time.DateTime;

/**
 * Represents an entry that has been converted, potentially from an external source.
 * Contains metadata like creation and modification timestamps, an external identifier,
 * and the associated TRCItem. It can also hold an error message if conversion failed.
 */
public class ConvertedEntry {
    private String label;
    private DateTime created; // http://purl.org/dc/terms/created
    private DateTime modified; // http://purl.org/dc/terms/modified
    private String errorMessage;

    private String externalId;
    private TRCItem trcItem;

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

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String getExternalId() {
        return externalId;
    }

    public void setExternalId(String externalId) {
        this.externalId = externalId;
    }

    public TRCItem getTrcItem() {
        return trcItem;
    }

    public void setTrcItem(TRCItem trcItem) {
        this.trcItem = trcItem;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(ConvertedEntry.class, this)
                .add("label", label)
                .add("created", created)
                .add("modified", modified)
                .add("errorMessage", errorMessage)
                .add("externalId", externalId)
                .add("trcItem", trcItem)
                .build();
    }
}
