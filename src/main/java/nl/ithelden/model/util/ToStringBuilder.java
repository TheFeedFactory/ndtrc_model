package nl.ithelden.model.util;

import java.lang.reflect.Array;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

/**
 * Builds {@code toString()} output in the exact format the 1.x (Groovy) model produced with
 * {@code @ToString(includeNames = true)}: {@code fully.qualified.Name(name:value, other:value)},
 * where nested classes keep their binary name ({@code Outer$Inner}), {@code null} prints as
 * {@code null}, lists and arrays print as {@code [a, b]} and maps as {@code [key:value]}
 * (empty map: {@code [:]}).
 *
 * <p>Internal to the model: kept public only because the model spans two packages. Log lines and
 * anything else that captured the old output stay byte-identical.</p>
 */
public final class ToStringBuilder {
    private final Object owner;
    private final StringBuilder sb;
    private boolean first = true;

    public ToStringBuilder(Class<?> type, Object owner) {
        this.owner = owner;
        this.sb = new StringBuilder(type.getName()).append('(');
    }

    public ToStringBuilder add(String name, Object value) {
        if (!first) {
            sb.append(", ");
        }
        first = false;
        sb.append(name).append(':');
        if (value == owner && value != null) {
            sb.append("(this)");
        } else {
            sb.append(format(value));
        }
        return this;
    }

    public String build() {
        return sb.append(')').toString();
    }

    /**
     * Formats a value the way Groovy's {@code InvokerHelper.toString} does.
     */
    public static String format(Object value) {
        if (value == null) {
            return "null";
        }
        if (value instanceof String) {
            return (String) value;
        }
        if (value instanceof char[]) {
            return new String((char[]) value);
        }
        if (value.getClass().isArray()) {
            StringBuilder out = new StringBuilder("[");
            int length = Array.getLength(value);
            for (int i = 0; i < length; i++) {
                if (i > 0) {
                    out.append(", ");
                }
                out.append(format(Array.get(value, i)));
            }
            return out.append(']').toString();
        }
        if (value instanceof Collection) {
            Collection<?> collection = (Collection<?>) value;
            StringBuilder out = new StringBuilder("[");
            boolean firstItem = true;
            for (Object item : collection) {
                if (!firstItem) {
                    out.append(", ");
                }
                firstItem = false;
                out.append(item == collection ? "(this Collection)" : format(item));
            }
            return out.append(']').toString();
        }
        if (value instanceof Map) {
            Map<?, ?> map = (Map<?, ?>) value;
            if (map.isEmpty()) {
                return "[:]";
            }
            StringBuilder out = new StringBuilder("[");
            Iterator<? extends Map.Entry<?, ?>> it = map.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry<?, ?> entry = it.next();
                out.append(entry.getKey() == map ? "(this Map)" : format(entry.getKey()));
                out.append(':');
                out.append(entry.getValue() == map ? "(this Map)" : format(entry.getValue()));
                if (it.hasNext()) {
                    out.append(", ");
                }
            }
            return out.append(']').toString();
        }
        return value.toString();
    }
}
