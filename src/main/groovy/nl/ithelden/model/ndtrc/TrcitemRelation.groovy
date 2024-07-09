package nl.ithelden.model.ndtrc

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import groovy.transform.ToString

/**
 * Information of the relations of the item with other items
 * (related to, parent item, child item, subitems, ...)
 */
@ToString(includeNames = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
class TrcitemRelation {
    @JsonProperty List<SubItemGroup> subItemGroups

    void cleanEmptyItems() {
        subItemGroups?.each {
            it?.cleanEmptyItems()
        }
    }
}