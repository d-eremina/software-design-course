import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import phonebook.contact.Contact;
import phonebook.database.DataBaseAssistant;


public class AddTest {
    String driver = "org.apache.derby.jdbc.EmbeddedDriver";
    String dbName = "PHONE_BOOK_TEST";

    DataBaseAssistant dataBaseAssistant;

    @Test
    void addContact() {
        try {
            dataBaseAssistant = new DataBaseAssistant(dbName);
        } catch (Exception e) {
            System.err.println(e.getMessage());
            return;
        }
        assertDoesNotThrow(dataBaseAssistant::createTable);
        dataBaseAssistant.addContact(new Contact("", "", "", "",
                "", "", "", ""));
        assertEquals(dataBaseAssistant.getAllContacts().size(), 0);

        dataBaseAssistant.addContact(new Contact("A", "BC", "", "",
                "+1234", "", "", ""));
        assertEquals(dataBaseAssistant.getAllContacts().size(), 1);

        dataBaseAssistant.addContact(new Contact("A", "BC", "", "",
                "", "", "", ""));
        assertEquals(dataBaseAssistant.getAllContacts().size(), 1);

        dataBaseAssistant.addContact(new Contact("A", "BC", "", "",
                "ads", "", "", ""));
        assertEquals(dataBaseAssistant.getAllContacts().size(), 1);
        assertDoesNotThrow(dataBaseAssistant::deleteTable);
        assertDoesNotThrow(() -> dataBaseAssistant.shutDown(driver));
    }
}
