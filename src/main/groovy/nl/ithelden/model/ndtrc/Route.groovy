package nl.ithelden.model.ndtrc

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import groovy.transform.ToString
import org.joda.time.DateTime


@ToString(includeNames = true)
class Route {
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
    @JsonProperty String slug

    @JsonProperty String validator
    @JsonProperty WFStatus wfstatus

    @JsonProperty EntityType entitytype

    @JsonProperty Contactinfo contactinfo
    @JsonProperty TRCItemCategories trcItemCategories
    @JsonProperty List<File> files = []
    @JsonProperty List<TRCItemDetail> trcItemDetails = []
    @JsonProperty TrcitemRelation trcitemRelation
    @JsonProperty String keywords
    @JsonProperty String markers
    @JsonProperty Location location
    @JsonProperty RouteInfo routeInfo

    @JsonProperty String userorganisation

    @JsonProperty Translations translations = new Translations()

    enum WFStatus { draft, readyforvalidation, approved, rejected, deleted, archived }
    enum EntityType { ROUTE }

    @ToString(includeNames = true)
    static class Category {
        String id
        List<Translation> translations = []

        static class Translation {
            String lang, text
        }
    }

    @ToString(includeNames = true)
    static class RouteInfo {
        Type type
        String url
        Double distanceInKilometers
        Integer durationInMinutes

        static enum Type {
            route_maker
        }
    }

    // below are workflow related fields, they are not part of the XSD of TRItem
    @JsonProperty Boolean forceoverwrite
}