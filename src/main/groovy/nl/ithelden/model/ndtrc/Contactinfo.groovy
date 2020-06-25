package nl.ithelden.model.ndtrc

import com.fasterxml.jackson.annotation.JsonProperty

class Contactinfo {
    @JsonProperty String label
    @JsonProperty Mail mail
    @JsonProperty Phone phone
    @JsonProperty Fax fax
    @JsonProperty List<Url> urls = []
    @JsonProperty Address address

    static class Mail {
        @JsonProperty String email
        @JsonProperty String descriptioncode // Type defining the format and data structure for codes.
        // Codes must have the format XXX-NNN where XXX a 3 char string and
        // XXX a 3-digit number
        @JsonProperty Boolean reservations
        @JsonProperty List<DescriptionTranslation> descriptionTranslations = []
    }

    static class Phone {
        @JsonProperty String number
        @JsonProperty String descriptioncode // Type defining the format and data structure for codes.
        // Codes must have the format XXX-NNN where XXX a 3 char string and
        // XXX a 3-digit number
        @JsonProperty Boolean reservations
        @JsonProperty List<DescriptionTranslation> descriptionTranslations = []
    }

    static class Fax {
        @JsonProperty String number
        @JsonProperty String descriptioncode // Type defining the format and data structure for codes.
        // Codes must have the format XXX-NNN where XXX a 3 char string and
        // XXX a 3-digit number
        @JsonProperty Boolean reservations
        @JsonProperty List<DescriptionTranslation> descriptionTranslations = []
    }

    static class Url {
        @JsonProperty URL url // max len = 1000
        @JsonProperty String descriptioncode // Type defining the format and data structure for codes.
        // Codes must have the format XXX-NNN where XXX a 3 char string and
        // XXX a 3-digit number
        @JsonProperty String targetLanguage
        @JsonProperty Boolean reservations
        @JsonProperty URLServiceType urlServiceType

        enum URLServiceType { general, booking, review, video, webshop, socialmedia, lastminute }

        @JsonProperty List<DescriptionTranslation> descriptionTranslations = []
    }

    static class DescriptionTranslation {
        @JsonProperty String lang
        @JsonProperty String label
    }
}