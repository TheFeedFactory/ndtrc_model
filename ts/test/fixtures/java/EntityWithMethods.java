package nl.ithelden.model.ndtrc;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/**
 * An entity whose Javadoc mentions a class Foo and an {@code enum Bar} and
 * contains braces { like this }.
 */
public class EntityWithMethods {
    private static final Pattern WORD = Pattern.compile("(\\w)(\\w*)");
    public static final String CONSTANT = "x; private String notAField;";

    @JsonProperty private String name;
    @JsonProperty private String country = "NL";
    @Deprecated @JsonProperty private List<String> tags = new ArrayList<>();
    private Translations translations = new Translations(); // a trailing comment; private String commented;

    /* private String blockCommented; */

    @JsonIgnore
    public boolean isEmpty() {
        return name == null;
    }

    public void normalize() {
        if (name != null && !name.isEmpty()) {
            name = name.trim();
        }
    }

    public static String helper(String input) {
        return input == null ? null : input.toLowerCase();
    }

    @Override
    public String toString() {
        return "EntityWithMethods(" + name + ")";
    }
}
