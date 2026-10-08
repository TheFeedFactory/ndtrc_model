package nl.ithelden.model.ndtrc;

import com.fasterxml.jackson.annotation.JsonProperty;

public class SimpleEntity {
    @JsonProperty private String name;
    @JsonProperty private Integer count;
    private Boolean active;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
