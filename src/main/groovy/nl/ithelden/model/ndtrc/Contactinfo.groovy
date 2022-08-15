package nl.ithelden.model.ndtrc

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonProperty
import groovy.transform.ToString
import nl.ithelden.model.util.StringUtils

@ToString(includeNames = true)
class Contactinfo {
    @JsonProperty String label
    @Deprecated @JsonProperty List<Mail> mails = []
    @Deprecated @JsonProperty List<Phone> phones = []
    @Deprecated @JsonProperty List<Fax> faxes = []
    @JsonProperty List<Url> urls = []
    @Deprecated @JsonProperty List<Address> addresses = []

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

        @JsonIgnore
        boolean isEmpty() {
            return StringUtils.isEmpty(email)
        }
    }

    @ToString(includeNames = true)
    static class Phone {
        @JsonProperty String number
        @JsonProperty String descriptioncode // Type defining the format and data structure for codes.
        // Codes must have the format XXX-NNN where XXX a 3 char string and
        // XXX a 3-digit number
        @JsonProperty Boolean reservations
        @JsonProperty List<DescriptionTranslation> descriptionTranslations = []

        @JsonIgnore
        boolean isEmpty() {
            return StringUtils.isEmpty(number)
        }
    }

    @ToString(includeNames = true)
    static class Fax {
        @JsonProperty String number
        @JsonProperty String descriptioncode // Type defining the format and data structure for codes.
        // Codes must have the format XXX-NNN where XXX a 3 char string and
        // XXX a 3-digit number
        @JsonProperty Boolean reservations
        @JsonProperty List<DescriptionTranslation> descriptionTranslations = []

        @JsonIgnore
        boolean isEmpty() {
            return StringUtils.isEmpty(number)
        }
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

        @JsonIgnore
        boolean isEmpty() {
            return url == null
        }
    }

    @ToString(includeNames = true)
    static class DescriptionTranslation {
        @JsonProperty String lang
        @JsonProperty String label
    }

    void convertToV1() {
        if (this.mails && (!this.mail || this.mail.isEmpty())) {
            this.mail = mails.first()
        }
        if (this.addresses && (!this.address || this.address.isEmpty())) {
            this.address = addresses.first()
        }
        if (this.faxes && (!this.fax || this.fax.isEmpty())) {
            this.fax = faxes.first()
        }
        if (this.phones && (!this.phone || this.phone.isEmpty())) {
            this.phone = phones.first()
        }
    }

    void convertToV2() {
        // in V2 format we only support 1 phone and or email address
        if (this.mail) {
            this.mails = [this.mail]
        } else {
            this.mails = []
        }
        if (this.address) {
            this.addresses = [this.address]
        } else {
            this.addresses = []
        }
        if (this.fax) {
            this.faxes = [this.fax]
        } else {
            this.faxes = []
        }
        if (this.phone) {
            this.phones = [this.phone]
        } else {
            this.phones = []
        }
    }
}