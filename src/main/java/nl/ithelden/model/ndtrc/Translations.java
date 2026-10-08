package nl.ithelden.model.ndtrc;

import java.util.ArrayList;
import java.util.List;
import nl.ithelden.model.util.ToStringBuilder;

/**
 * Holds information about the primary language and available languages
 * for translations within a TRC item or group.
 */
public class Translations {
    private String primaryLanguage = "nl"; // ISO name of language
    private List<String> availableLanguages = new ArrayList<>(List.of("nl"));

    public String getPrimaryLanguage() {
        return primaryLanguage;
    }

    public void setPrimaryLanguage(String primaryLanguage) {
        this.primaryLanguage = primaryLanguage;
    }

    public List<String> getAvailableLanguages() {
        return availableLanguages;
    }

    public void setAvailableLanguages(List<String> availableLanguages) {
        this.availableLanguages = availableLanguages;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(Translations.class, this)
                .add("primaryLanguage", primaryLanguage)
                .add("availableLanguages", availableLanguages)
                .build();
    }
}
