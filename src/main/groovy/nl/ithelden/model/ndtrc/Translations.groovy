package nl.ithelden.model.ndtrc

import groovy.transform.ToString

@ToString(includeNames = true)
 class Translations {
    String primaryLanguage = "nl" // ISO name of language
    List<String> availableLanguages = ["nl"]
}
