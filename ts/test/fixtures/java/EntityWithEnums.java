package nl.ithelden.model.ndtrc;

public class EntityWithEnums {
    private String label;
    private Status status;
    private DetailType detailType;

    public enum Status { active, inactive, archived }

    public static enum DetailType {
        simple,
        complex,
        composite;

        public static final DetailType MIN_VALUE = simple;

        public DetailType next() {
            DetailType[] values = values();
            int ordinal = ordinal() + 1;
            return values[ordinal >= values.length ? 0 : ordinal];
        }
    }

    public static class Detail {
        private String lang, text;
    }
}
