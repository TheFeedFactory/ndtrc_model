package nl.ithelden.model.ndtrc;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import nl.ithelden.model.util.StringUtils;
import nl.ithelden.model.util.ToStringBuilder;

/**
 * Represents a GIS coordinate with x (longitude) and y (latitude) values,
 * and an optional label. Includes a method to check if the coordinate is empty.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class GISCoordinate {
    @JsonProperty private String xcoordinate;
    @JsonProperty private String ycoordinate;
    @JsonProperty private String label;

    public String getXcoordinate() {
        return xcoordinate;
    }

    public void setXcoordinate(String xcoordinate) {
        this.xcoordinate = xcoordinate;
    }

    public String getYcoordinate() {
        return ycoordinate;
    }

    public void setYcoordinate(String ycoordinate) {
        this.ycoordinate = ycoordinate;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    @JsonIgnore
    public boolean isEmpty() {
        return StringUtils.isEmpty(xcoordinate) &&
               StringUtils.isEmpty(ycoordinate);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(GISCoordinate.class, this)
                .add("xcoordinate", xcoordinate)
                .add("ycoordinate", ycoordinate)
                .add("label", label)
                .add("empty", isEmpty())
                .build();
    }
}
