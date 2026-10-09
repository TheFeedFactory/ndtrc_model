package nl.ithelden.model.ndtrc;

import com.fasterxml.jackson.annotation.JsonInclude;
import nl.ithelden.model.util.ToStringBuilder;

/**
 * Represents a performer associated with a TRC item,
 * including their role identifier, label (name), and role label.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Performer {
    private String roleid;
    private String label;
    private String rolelabel;

    public String getRoleid() {
        return roleid;
    }

    public void setRoleid(String roleid) {
        this.roleid = roleid;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getRolelabel() {
        return rolelabel;
    }

    public void setRolelabel(String rolelabel) {
        this.rolelabel = rolelabel;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(Performer.class, this)
                .add("roleid", roleid)
                .add("label", label)
                .add("rolelabel", rolelabel)
                .build();
    }
}
