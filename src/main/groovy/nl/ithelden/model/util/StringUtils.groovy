package nl.ithelden.model.util

/**
 * Utility class for string manipulation.
 */
class StringUtils {

    /**
     * Checks if a string is null, empty, or contains only whitespace.
     * @param value The string to check.
     * @return true if the string is effectively empty, false otherwise.
     */
    static boolean isEmpty(String value) {
        if (value == null) return true

        return value.trim().isEmpty()
    }


    /**
     * Checks if a string is not null, not empty, and does not contain only whitespace.
     * @param value The string to check.
     * @return true if the string is effectively not empty, false otherwise.
     */
    static boolean isNotEmpty(String value) {
        return !isEmpty(value)
    }
}