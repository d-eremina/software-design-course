import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import phonebook.database.DataBaseAssistant;

import java.io.File;
import java.nio.file.Path;


public class ImportTest {
    String driver = "org.apache.derby.jdbc.EmbeddedDriver";
    String dbName = "PHONE_BOOK_TEST";

    DataBaseAssistant dataBaseAssistant;

    @Test
    void importContacts() {
        try {
            dataBaseAssistant = new DataBaseAssistant(dbName);
        } catch (Exception e) {
            System.err.println(e.getMessage());
            return;
        }

        assertDoesNotThrow(dataBaseAssistant::createTable);

        System.err.println();
        dataBaseAssistant.importFile(
                new File(Path.of("").toAbsolutePath() + "/tests/files/correctFile.csv"));
        assertEquals(dataBaseAssistant.getAllContacts().size(), 6);

        // Don't double values
        dataBaseAssistant.importFile(
                new File(Path.of("").toAbsolutePath() + "/tests/files/correctFile.csv"));
        assertEquals(dataBaseAssistant.getAllContacts().size(), 6);

        // Don't import incorrect values and don't double it
        dataBaseAssistant.importFile(
                new File(Path.of("").toAbsolutePath() + "/tests/files/incorrectFile.csv"));
        assertEquals(dataBaseAssistant.getAllContacts().size(), 6);

        assertDoesNotThrow(dataBaseAssistant::deleteTable);
        assertDoesNotThrow(() -> dataBaseAssistant.shutDown(driver));
    }
}
