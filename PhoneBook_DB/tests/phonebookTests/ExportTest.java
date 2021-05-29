import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import phonebook.database.DataBaseAssistant;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.nio.file.Path;


public class ExportTest {
    String driver = "org.apache.derby.jdbc.EmbeddedDriver";
    String dbName = "PHONE_BOOK_TEST";

    DataBaseAssistant dataBaseAssistant;

    @Test
    void exportContacts() {
        try {
            dataBaseAssistant = new DataBaseAssistant(dbName);
        } catch (Exception e) {
            System.err.println(e.getMessage());
            return;
        }

        assertDoesNotThrow(dataBaseAssistant::createTable);

        System.err.println();
        File importFile = new File(Path.of("").toAbsolutePath() + "/tests/files/correctFile.csv");
        dataBaseAssistant.importFile(importFile);
        assertEquals(dataBaseAssistant.getAllContacts().size(), 6);
        dataBaseAssistant.exportData(new File(Path.of("").toAbsolutePath()
                + "/tests/files/exportFile.csv"));

        try (BufferedReader br = new BufferedReader(new FileReader(importFile))) {
            int i = 0;
            for (String line; (line = br.readLine()) != null; ) {
                var data = line.split(";(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)", -1);
                assertEquals(data[0], dataBaseAssistant.getAllContacts().get(i).getSurname());
                assertEquals(data[1], dataBaseAssistant.getAllContacts().get(i).getName());
                assertEquals(data[2], dataBaseAssistant.getAllContacts().get(i).getPatronymic());
                assertEquals(data[3], dataBaseAssistant.getAllContacts().get(i).getMobilePhone());
                assertEquals(data[4], dataBaseAssistant.getAllContacts().get(i).getHomePhone());
                assertEquals(data[5], dataBaseAssistant.getAllContacts().get(i).getAddress());
                assertEquals(data[6], dataBaseAssistant.getAllContacts().get(i).getBirthDate());
                assertEquals(data[7], dataBaseAssistant.getAllContacts().get(i).getComment());
                ++i;
            }
        } catch (Exception e) {
            return;
        }

        assertDoesNotThrow(dataBaseAssistant::deleteTable);
        assertDoesNotThrow(() -> dataBaseAssistant.shutDown(driver));
    }
}
