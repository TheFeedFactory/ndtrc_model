package nl.ithelden.model.ndtrc

import groovy.transform.ToString

/**
 * Holds information about the primary language and available languages
 * for translations within a TRC item or group.
 */
@ToString(includeNames = true)
 class Translations {
    String primaryLanguage = "nl" // ISO name of language
    List<String> availableLanguages = ["nl"]
}
