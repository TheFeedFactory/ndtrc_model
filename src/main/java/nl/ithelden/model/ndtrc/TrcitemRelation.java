package nl.ithelden.model.ndtrc;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import nl.ithelden.model.util.ToStringBuilder;

/**
 * Information of the relations of the item with other items
 * (related to, parent item, child item, subitems, ...)
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TrcitemRelation {
    @JsonProperty private List<SubItemGroup> subItemGroups;

    public List<SubItemGroup> getSubItemGroups() {
        return subItemGroups;
    }

    public void setSubItemGroups(List<SubItemGroup> subItemGroups) {
        this.subItemGroups = subItemGroups;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(TrcitemRelation.class, this)
                .add("subItemGroups", subItemGroups)
                .build();
    }
}
