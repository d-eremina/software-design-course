import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import phonebook.contact.Contact;
import phonebook.database.DataBaseAssistant;


public class EditTest {
    String driver = "org.apache.derby.jdbc.EmbeddedDriver";
    String dbName = "PHONE_BOOK_TEST";

    DataBaseAssistant dataBaseAssistant;

    @Test
    void editContact() {
        try {
            dataBaseAssistant = new DataBaseAssistant(dbName);
        } catch (Exception e) {
            System.err.println(e.getMessage());
            return;
        }

        assertDoesNotThrow(dataBaseAssistant::createTable);
        Contact testContact = new Contact("abc", "def", "",
                "78902223344", "+1234", "", "", "");
        dataBaseAssistant.addContact(testContact);
        assertEquals(dataBaseAssistant.getAllContacts().size(), 1);

        // Can't edit non-existing contact
        dataBaseAssistant.editContact("a", "b", "",
                new Contact("a", "a", "", "+789", "",
                        "", "", ""));
        assertEquals(dataBaseAssistant.getAllContacts().size(), 1);
        assertFalse(dataBaseAssistant.isContactPresented("a", "b", ""));
        assertEquals(dataBaseAssistant.getAllContacts().get(0), testContact);

        // Can't edit contact to contact with invalid fields
        dataBaseAssistant.editContact("abc", "def", "",
                new Contact("abc", "def", "", "", "",
                        "", "", ""));
        assertEquals(dataBaseAssistant.getAllContacts().size(), 1);
        assertEquals(dataBaseAssistant.getAllContacts().get(0), testContact);

        // Correct editing
        Contact editedContact = new Contact("abc", "def", "",
                "456789", "+234", "Wysteria Lane", "", "-");
        dataBaseAssistant.editContact("abc", "def", "", editedContact);
        assertEquals(dataBaseAssistant.getAllContacts().size(), 1);
        assertEquals(dataBaseAssistant.getAllContacts().get(0), editedContact);
        assertDoesNotThrow(dataBaseAssistant::deleteTable);
        assertDoesNotThrow(() -> dataBaseAssistant.shutDown(driver));
    }
}
