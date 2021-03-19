package nl.ithelden.model.util

class StringUtils {

    static boolean isEmpty(String value) {
        if (value == null) return true

        return value.trim().isEmpty()
    }
}