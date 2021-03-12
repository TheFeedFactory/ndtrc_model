package nl.ithelden.model.ndtrc

import com.fasterxml.jackson.annotation.JsonProperty
import groovy.transform.ToString

@ToString(includeNames = true)
class Contactinfo {
    @JsonProperty String label
    @JsonProperty List<Mail> mails = []
    @JsonProperty List<Phone> phones = []
    @JsonProperty List<Fax> faxes = []
    @JsonProperty List<Url> urls = []
    @JsonProperty List<Address> addresses = []

    @JsonProperty Mail mail
    @JsonProperty Phone phone
    @JsonProperty Fax fax
    @JsonProperty Address address

    @ToString(includeNames = true)
    static class Mail {
        @JsonProperty String email
        @JsonProperty String descriptioncode // Type defining the format and data structure for codes.
        // Codes must have the format XXX-NNN where XXX a 3 char string and
        // XXX a 3-digit number
        @JsonProperty Boolean reservations
        @JsonProperty List<DescriptionTranslation> descriptionTranslations = []
    }

    @ToString(includeNames = true)
    static class Phone {
        @JsonProperty String number
        @JsonProperty String descriptioncode // Type defining the format and data structure for codes.
        // Codes must have the format XXX-NNN where XXX a 3 char string and
        // XXX a 3-digit number
        @JsonProperty Boolean reservations
        @JsonProperty List<DescriptionTranslation> descriptionTranslations = []
    }

    @ToString(includeNames = true)
    static class Fax {
        @JsonProperty String number
        @JsonProperty String descriptioncode // Type defining the format and data structure for codes.
        // Codes must have the format XXX-NNN where XXX a 3 char string and
        // XXX a 3-digit number
        @JsonProperty Boolean reservations
        @JsonProperty List<DescriptionTranslation> descriptionTranslations = []
    }

    @ToString(includeNames = true)
    static class Url {
        @JsonProperty URL url // max len = 1000
        @JsonProperty String descriptioncode // Type defining the format and data structure for codes.
        // Codes must have the format XXX-NNN where XXX a 3 char string and
        // XXX a 3-digit number
        @JsonProperty String targetLanguage
        @JsonProperty Boolean reservations
        @JsonProperty URLServiceType urlServiceType

        enum URLServiceType { general, booking, review, video, webshop, socialmedia, lastminute, virtualtour }

        @JsonProperty List<DescriptionTranslation> descriptionTranslations = []
    }

    @ToString(includeNames = true)
    static class DescriptionTranslation {
        @JsonProperty String lang
        @JsonProperty String label
    }

    void convertToV1() {
        if (this.mails && !this.mail) {
            this.mail = mails.first()
        }
        if (this.addresses && !this.address) {
            this.address = addresses.first()
        }
        if (this.faxes && !this.fax) {
            this.fax = faxes.first()
        }
        if (this.phones && !this.phone) {
            this.phone = phones.first()
        }
    }

    void convertToV2() {
        if (!this.mails && this.mail) {
            this.mails = [this.mail]
        }
        if (!this.addresses && this.address) {
            this.addresses = [this.address]
        }
        if (!this.faxes && this.fax) {
            this.faxes = [this.fax]
        }
        if (!this.phones && this.phone) {
            this.phones = [this.phone]
        }
    }
}