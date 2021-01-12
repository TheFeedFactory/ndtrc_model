package nl.ithelden.model.ndtrc

import com.fasterxml.jackson.annotation.JsonProperty
import groovy.transform.ToString

/**
 * Information of the relations of the item with other items
 * (related to, parent item, child item, subitems, ...)
 */
@ToString(includeNames = true)
class TrcitemRelation {
    @JsonProperty List<SubItemGroup> subItemGroups
}