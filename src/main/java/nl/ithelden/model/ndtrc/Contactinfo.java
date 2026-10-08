package nl.ithelden.model.ndtrc;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import nl.ithelden.model.util.StringUtils;
import nl.ithelden.model.util.ToStringBuilder;

/**
 * Represents contact information for a TRC item, including label, email, phone, fax,
 * URLs, and address details. Contains methods to convert between V1 (single contact)
 * and V2 (multiple contacts - deprecated fields) formats.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Contactinfo {
    @JsonProperty private String label;
    @Deprecated @JsonProperty private List<Mail> mails = new ArrayList<>();
    @Deprecated @JsonProperty private List<Phone> phones = new ArrayList<>();
    @Deprecated @JsonProperty private List<Fax> faxes = new ArrayList<>();
    @JsonProperty private List<Url> urls = new ArrayList<>();
    @Deprecated @JsonProperty private List<Address> addresses = new ArrayList<>();

    @JsonProperty private Mail mail;
    @JsonProperty private Phone phone;
    @JsonProperty private Fax fax;
    @JsonProperty private Address address;

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public List<Mail> getMails() {
        return mails;
    }

    public void setMails(List<Mail> mails) {
        this.mails = mails;
    }

    public List<Phone> getPhones() {
        return phones;
    }

    public void setPhones(List<Phone> phones) {
        this.phones = phones;
    }

    public List<Fax> getFaxes() {
        return faxes;
    }

    public void setFaxes(List<Fax> faxes) {
        this.faxes = faxes;
    }

    public List<Url> getUrls() {
        return urls;
    }

    public void setUrls(List<Url> urls) {
        this.urls = urls;
    }

    public List<Address> getAddresses() {
        return addresses;
    }

    public void setAddresses(List<Address> addresses) {
        this.addresses = addresses;
    }

    public Mail getMail() {
        return mail;
    }

    public void setMail(Mail mail) {
        this.mail = mail;
    }

    public Phone getPhone() {
        return phone;
    }

    public void setPhone(Phone phone) {
        this.phone = phone;
    }

    public Fax getFax() {
        return fax;
    }

    public void setFax(Fax fax) {
        this.fax = fax;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    /**
     * Represents an email address with an optional description code and translations.
     */
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class Mail {
        @JsonProperty private String email;
        @JsonProperty private String descriptioncode; // Type defining the format and data structure for codes.
        // Codes must have the format XXX-NNN where XXX a 3 char string and
        // XXX a 3-digit number
        @JsonProperty private Boolean reservations;
        @JsonProperty private List<DescriptionTranslation> descriptionTranslations = new ArrayList<>();

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getDescriptioncode() {
            return descriptioncode;
        }

        public void setDescriptioncode(String descriptioncode) {
            this.descriptioncode = descriptioncode;
        }

        public Boolean getReservations() {
            return reservations;
        }

        public Boolean isReservations() {
            return reservations;
        }

        public void setReservations(Boolean reservations) {
            this.reservations = reservations;
        }

        public List<DescriptionTranslation> getDescriptionTranslations() {
            return descriptionTranslations;
        }

        public void setDescriptionTranslations(List<DescriptionTranslation> descriptionTranslations) {
            this.descriptionTranslations = descriptionTranslations;
        }

        @JsonIgnore
        public boolean isEmpty() {
            return StringUtils.isEmpty(email);
        }

        @Override
        public String toString() {
            return new ToStringBuilder(Mail.class, this)
                    .add("email", email)
                    .add("descriptioncode", descriptioncode)
                    .add("reservations", reservations)
                    .add("descriptionTranslations", descriptionTranslations)
                    .add("empty", isEmpty())
                    .build();
        }
    }

    /**
     * Represents a phone number with an optional description code and translations.
     */
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class Phone {
        @JsonProperty private String number;
        @JsonProperty private String descriptioncode; // Type defining the format and data structure for codes.
        // Codes must have the format XXX-NNN where XXX a 3 char string and
        // XXX a 3-digit number
        @JsonProperty private Boolean reservations;
        @JsonProperty private List<DescriptionTranslation> descriptionTranslations = new ArrayList<>();

        public String getNumber() {
            return number;
        }

        public void setNumber(String number) {
            this.number = number;
        }

        public String getDescriptioncode() {
            return descriptioncode;
        }

        public void setDescriptioncode(String descriptioncode) {
            this.descriptioncode = descriptioncode;
        }

        public Boolean getReservations() {
            return reservations;
        }

        public Boolean isReservations() {
            return reservations;
        }

        public void setReservations(Boolean reservations) {
            this.reservations = reservations;
        }

        public List<DescriptionTranslation> getDescriptionTranslations() {
            return descriptionTranslations;
        }

        public void setDescriptionTranslations(List<DescriptionTranslation> descriptionTranslations) {
            this.descriptionTranslations = descriptionTranslations;
        }

        @JsonIgnore
        public boolean isEmpty() {
            return StringUtils.isEmpty(number);
        }

        @Override
        public String toString() {
            return new ToStringBuilder(Phone.class, this)
                    .add("number", number)
                    .add("descriptioncode", descriptioncode)
                    .add("reservations", reservations)
                    .add("descriptionTranslations", descriptionTranslations)
                    .add("empty", isEmpty())
                    .build();
        }
    }

    /**
     * Represents a fax number with an optional description code and translations.
     */
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class Fax {
        @JsonProperty private String number;
        @JsonProperty private String descriptioncode; // Type defining the format and data structure for codes.
        // Codes must have the format XXX-NNN where XXX a 3 char string and
        // XXX a 3-digit number
        @JsonProperty private Boolean reservations;
        @JsonProperty private List<DescriptionTranslation> descriptionTranslations = new ArrayList<>();

        public String getNumber() {
            return number;
        }

        public void setNumber(String number) {
            this.number = number;
        }

        public String getDescriptioncode() {
            return descriptioncode;
        }

        public void setDescriptioncode(String descriptioncode) {
            this.descriptioncode = descriptioncode;
        }

        public Boolean getReservations() {
            return reservations;
        }

        public Boolean isReservations() {
            return reservations;
        }

        public void setReservations(Boolean reservations) {
            this.reservations = reservations;
        }

        public List<DescriptionTranslation> getDescriptionTranslations() {
            return descriptionTranslations;
        }

        public void setDescriptionTranslations(List<DescriptionTranslation> descriptionTranslations) {
            this.descriptionTranslations = descriptionTranslations;
        }

        @JsonIgnore
        public boolean isEmpty() {
            return StringUtils.isEmpty(number);
        }

        @Override
        public String toString() {
            return new ToStringBuilder(Fax.class, this)
                    .add("number", number)
                    .add("descriptioncode", descriptioncode)
                    .add("reservations", reservations)
                    .add("descriptionTranslations", descriptionTranslations)
                    .add("empty", isEmpty())
                    .build();
        }
    }

    /**
     * Represents a URL associated with the contact information, including type, language,
     * and description.
     */
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class Url {
        @JsonProperty private URL url; // max len = 1000
        @JsonProperty private String descriptioncode; // Type defining the format and data structure for codes.
        // Codes must have the format XXX-NNN where XXX a 3 char string and
        // XXX a 3-digit number
        @JsonProperty private String targetLanguage;
        @JsonProperty private Boolean reservations;
        @JsonProperty private URLServiceType urlServiceType;

        // `booking` predates the ticket/reservation split below and is kept for existing data;
        // new URLs should use `ticket` (buy tickets) or `reservation` (reserve a table/slot/visit).
        public enum URLServiceType {
            general, booking, review, video, webshop, socialmedia, lastminute, virtualtour, dmo,
            sustainability, venuefinder, travelbase, homepage, ticket, reservation;

            // Groovy gives every enum these members; kept so the 1.x API is unchanged.
            public static final URLServiceType MIN_VALUE = general;
            public static final URLServiceType MAX_VALUE = reservation;

            public URLServiceType next() {
                URLServiceType[] values = values();
                int ordinal = ordinal() + 1;
                return values[ordinal >= values.length ? 0 : ordinal];
            }

            public URLServiceType previous() {
                URLServiceType[] values = values();
                int ordinal = ordinal() - 1;
                return values[ordinal < 0 ? values.length - 1 : ordinal];
            }
        }

        @JsonProperty private List<DescriptionTranslation> descriptionTranslations = new ArrayList<>();

        public URL getUrl() {
            return url;
        }

        public void setUrl(URL url) {
            this.url = url;
        }

        public String getDescriptioncode() {
            return descriptioncode;
        }

        public void setDescriptioncode(String descriptioncode) {
            this.descriptioncode = descriptioncode;
        }

        public String getTargetLanguage() {
            return targetLanguage;
        }

        public void setTargetLanguage(String targetLanguage) {
            this.targetLanguage = targetLanguage;
        }

        public Boolean getReservations() {
            return reservations;
        }

        public Boolean isReservations() {
            return reservations;
        }

        public void setReservations(Boolean reservations) {
            this.reservations = reservations;
        }

        public URLServiceType getUrlServiceType() {
            return urlServiceType;
        }

        public void setUrlServiceType(URLServiceType urlServiceType) {
            this.urlServiceType = urlServiceType;
        }

        public List<DescriptionTranslation> getDescriptionTranslations() {
            return descriptionTranslations;
        }

        public void setDescriptionTranslations(List<DescriptionTranslation> descriptionTranslations) {
            this.descriptionTranslations = descriptionTranslations;
        }

        @JsonIgnore
        public boolean isEmpty() {
            return url == null;
        }

        public static URLServiceType getTypeFromString(String type) {
            for (URLServiceType serviceType : URLServiceType.values()) {
                if (serviceType.toString().equals(type)) {
                    return serviceType;
                }
            }
            throw new IllegalArgumentException("No enum found with type: " + type);
        }

        @Override
        public String toString() {
            return new ToStringBuilder(Url.class, this)
                    .add("url", url)
                    .add("descriptioncode", descriptioncode)
                    .add("targetLanguage", targetLanguage)
                    .add("reservations", reservations)
                    .add("urlServiceType", urlServiceType)
                    .add("descriptionTranslations", descriptionTranslations)
                    .add("empty", isEmpty())
                    .build();
        }
    }

    /**
     * Represents a translation for a description field (e.g., for email, phone, fax, URL)
     * in a specific language.
     */
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class DescriptionTranslation {
        @JsonProperty private String lang;
        @JsonProperty private String label;

        public String getLang() {
            return lang;
        }

        public void setLang(String lang) {
            this.lang = lang;
        }

        public String getLabel() {
            return label;
        }

        public void setLabel(String label) {
            this.label = label;
        }

        @Override
        public String toString() {
            return new ToStringBuilder(DescriptionTranslation.class, this)
                    .add("lang", lang)
                    .add("label", label)
                    .build();
        }
    }

    public void convertToV1() {
        if (this.mails != null && !this.mails.isEmpty() && (this.mail == null || this.mail.isEmpty())) {
            this.mail = mails.get(0);
        }
        if (this.addresses != null && !this.addresses.isEmpty() && (this.address == null || this.address.isEmpty())) {
            this.address = addresses.get(0);
        }
        if (this.faxes != null && !this.faxes.isEmpty() && (this.fax == null || this.fax.isEmpty())) {
            this.fax = faxes.get(0);
        }
        if (this.phones != null && !this.phones.isEmpty() && (this.phone == null || this.phone.isEmpty())) {
            this.phone = phones.get(0);
        }
    }

    public void convertToV2() {
        // in V2 format we only support 1 phone and or email address
        if (this.mail != null) {
            this.mails = new ArrayList<>();
            this.mails.add(this.mail);
        } else {
            this.mails = new ArrayList<>();
        }
        if (this.address != null) {
            this.addresses = new ArrayList<>();
            this.addresses.add(this.address);
        } else {
            this.addresses = new ArrayList<>();
        }
        if (this.fax != null) {
            this.faxes = new ArrayList<>();
            this.faxes.add(this.fax);
        } else {
            this.faxes = new ArrayList<>();
        }
        if (this.phone != null) {
            this.phones = new ArrayList<>();
            this.phones.add(this.phone);
        } else {
            this.phones = new ArrayList<>();
        }
    }

    @Override
    public String toString() {
        return new ToStringBuilder(Contactinfo.class, this)
                .add("label", label)
                .add("mails", mails)
                .add("phones", phones)
                .add("faxes", faxes)
                .add("urls", urls)
                .add("addresses", addresses)
                .add("mail", mail)
                .add("phone", phone)
                .add("fax", fax)
                .add("address", address)
                .build();
    }
}
