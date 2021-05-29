import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import phonebook.contact.Contact;
import phonebook.database.DataBaseAssistant;


public class DeleteTest {
    String driver = "org.apache.derby.jdbc.EmbeddedDriver";
    String dbName = "PHONE_BOOK_TEST";

    DataBaseAssistant dataBaseAssistant;

    @Test
    void deleteContact() {
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

        // Does not delete non-existing contacts
        dataBaseAssistant.deleteContact("", "", "");
        dataBaseAssistant.deleteContact("abc", "def", "g");

        // Deletes correct
        dataBaseAssistant.deleteContact("abc", "def", "");
        assertEquals(dataBaseAssistant.getAllContacts().size(), 0);

        assertDoesNotThrow(dataBaseAssistant::deleteTable);
        assertDoesNotThrow(() -> dataBaseAssistant.shutDown(driver));
    }
}
