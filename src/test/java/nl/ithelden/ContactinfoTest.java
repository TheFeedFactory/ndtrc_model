package nl.ithelden;

import java.net.URL;
import nl.ithelden.model.ndtrc.Address;
import nl.ithelden.model.ndtrc.Contactinfo;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class ContactinfoTest {
    private static Contactinfo.Mail mail(String email) {
        Contactinfo.Mail mail = new Contactinfo.Mail();
        mail.setEmail(email);
        return mail;
    }

    private static Contactinfo.Phone phone(String number) {
        Contactinfo.Phone phone = new Contactinfo.Phone();
        phone.setNumber(number);
        return phone;
    }

    @Test
    void convertToV1TakesTheFirstListEntryWhenTheSingleValueIsMissingOrEmpty() {
        Contactinfo contactinfo = new Contactinfo();
        contactinfo.getMails().add(mail("first@example.com"));
        contactinfo.getMails().add(mail("second@example.com"));
        contactinfo.getPhones().add(phone("010"));
        contactinfo.setPhone(phone(" "));
        Address address = new Address();
        address.setCity("Utrecht");
        contactinfo.getAddresses().add(address);

        contactinfo.convertToV1();

        Assertions.assertEquals("first@example.com", contactinfo.getMail().getEmail());
        Assertions.assertEquals("010", contactinfo.getPhone().getNumber());
        Assertions.assertSame(address, contactinfo.getAddress());
        Assertions.assertNull(contactinfo.getFax());
    }

    @Test
    void convertToV1KeepsAUsableSingleValue() {
        Contactinfo contactinfo = new Contactinfo();
        contactinfo.getMails().add(mail("list@example.com"));
        contactinfo.setMail(mail("single@example.com"));

        contactinfo.convertToV1();

        Assertions.assertEquals("single@example.com", contactinfo.getMail().getEmail());
    }

    @Test
    void convertToV1ToleratesNullLists() {
        Contactinfo contactinfo = new Contactinfo();
        contactinfo.setMails(null);
        contactinfo.setPhones(null);
        contactinfo.setFaxes(null);
        contactinfo.setAddresses(null);

        Assertions.assertDoesNotThrow(contactinfo::convertToV1);
        Assertions.assertNull(contactinfo.getMail());
    }

    @Test
    void convertToV2WrapsEachSingleValueInAFreshMutableList() {
        Contactinfo contactinfo = new Contactinfo();
        Contactinfo.Mail mail = mail("a@example.com");
        contactinfo.setMail(mail);
        contactinfo.getPhones().add(phone("stale"));
        contactinfo.setMails(null);

        contactinfo.convertToV2();

        Assertions.assertEquals(1, contactinfo.getMails().size());
        Assertions.assertSame(mail, contactinfo.getMails().get(0));
        Assertions.assertTrue(contactinfo.getPhones().isEmpty(), "no single phone, so the list is reset");
        Assertions.assertTrue(contactinfo.getFaxes().isEmpty());
        Assertions.assertTrue(contactinfo.getAddresses().isEmpty());
        contactinfo.getMails().add(mail("b@example.com"));
        contactinfo.getPhones().add(phone("1"));
    }

    @Test
    void convertToV2WrapsEvenAnEmptySingleValue() {
        // Groovy truth of an object is "not null", so an empty Mail still counts
        Contactinfo contactinfo = new Contactinfo();
        contactinfo.setMail(mail(""));
        contactinfo.convertToV2();
        Assertions.assertEquals(1, contactinfo.getMails().size());
    }

    @Test
    void emptinessOfTheParts() throws Exception {
        Assertions.assertTrue(mail(" ").isEmpty());
        Assertions.assertFalse(mail("x").isEmpty());
        Assertions.assertTrue(phone(null).isEmpty());
        Assertions.assertTrue(new Contactinfo.Fax().isEmpty());
        Contactinfo.Url url = new Contactinfo.Url();
        Assertions.assertTrue(url.isEmpty());
        url.setUrl(new URL("https://example.com"));
        Assertions.assertFalse(url.isEmpty());
    }

    @Test
    void getTypeFromStringIsExactAndCaseSensitive() {
        Assertions.assertEquals(Contactinfo.Url.URLServiceType.ticket, Contactinfo.Url.getTypeFromString("ticket"));
        Assertions.assertEquals(Contactinfo.Url.URLServiceType.booking, Contactinfo.Url.getTypeFromString("booking"));
        IllegalArgumentException e = Assertions.assertThrows(IllegalArgumentException.class, () -> Contactinfo.Url.getTypeFromString("TICKET"));
        Assertions.assertEquals("No enum found with type: TICKET", e.getMessage());
        Assertions.assertThrows(IllegalArgumentException.class, () -> Contactinfo.Url.getTypeFromString(null));
    }
}
