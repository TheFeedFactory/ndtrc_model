package nl.ithelden.model.ndtrc;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.ArrayList;
import java.util.List;

public class NestedEntity {
    @JsonProperty private String id;
    @JsonProperty private List<Inner> items = new ArrayList<>();

    public static class Inner {
        @JsonProperty private String value;
        @JsonProperty private List<Deep> nested = new ArrayList<>();

        public static class Deep {
            private String key;

            public String getKey() {
                return key;
            }
        }
    }
}
