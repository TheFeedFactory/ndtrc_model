package nl.ithelden.model.ndtrc

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import groovy.transform.ToString
import nl.ithelden.model.util.StringUtils

/**
 * Represents contact information for a TRC item, including label, email, phone, fax,
 * URLs, and address details. Contains methods to convert between V1 (single contact)
 * and V2 (multiple contacts - deprecated fields) formats.
 */
@ToString(includeNames = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
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

    /**
     * Represents an email address with an optional description code and translations.
     */
    @ToString(includeNames = true)
    @JsonInclude(JsonInclude.Include.NON_NULL)
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

    /**
     * Represents a phone number with an optional description code and translations.
     */
    @ToString(includeNames = true)
    @JsonInclude(JsonInclude.Include.NON_NULL)
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

    /**
     * Represents a fax number with an optional description code and translations.
     */
    @ToString(includeNames = true)
    @JsonInclude(JsonInclude.Include.NON_NULL)
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

    /**
     * Represents a URL associated with the contact information, including type, language,
     * and description.
     */
    @ToString(includeNames = true)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    static class Url {
        @JsonProperty URL url // max len = 1000
        @JsonProperty String descriptioncode // Type defining the format and data structure for codes.
        // Codes must have the format XXX-NNN where XXX a 3 char string and
        // XXX a 3-digit number
        @JsonProperty String targetLanguage
        @JsonProperty Boolean reservations
        @JsonProperty URLServiceType urlServiceType

        enum URLServiceType { general, booking, review, video, webshop, socialmedia, lastminute, virtualtour, dmo,
                              sustainability, venuefinder, travelbase }

        @JsonProperty List<DescriptionTranslation> descriptionTranslations = []

        @JsonIgnore
        boolean isEmpty() {
            return url == null
        }

        static URLServiceType getTypeFromString(String type) {
            for (URLServiceType serviceType : URLServiceType.values()) {
                if (serviceType.toString() == type) {
                    return serviceType;
                }
            }
            throw new IllegalArgumentException("No enum found with type: " + type);
        }
    }

    /**
     * Represents a translation for a description field (e.g., for email, phone, fax, URL)
     * in a specific language.
     */
    @ToString(includeNames = true)
    @JsonInclude(JsonInclude.Include.NON_NULL)
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