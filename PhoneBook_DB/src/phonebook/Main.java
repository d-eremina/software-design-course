package phonebook;

import phonebook.console.Command;
import phonebook.console.ConsoleUtil;
import phonebook.database.DataBaseAssistant;

import java.io.File;
import java.sql.*;

public class Main {
    public static void main(String[] args) {
        String driver = "org.apache.derby.jdbc.EmbeddedDriver";
        String dbName = "PHONE_BOOK";
        DataBaseAssistant dataBaseAssistant;
        try {
            dataBaseAssistant = new DataBaseAssistant(dbName);
        } catch (SQLException e) {
            System.out.println("Can't do anything with database: " + e.getMessage());
            return;
        } catch (Exception e) {
            System.out.println("Error during connection to database: " + e.getMessage());
            return;
        }
        dataBaseAssistant.createTable();
        Command answer;
        do {
            answer = ConsoleUtil.getNextCommand();
            if (answer == Command.LIST) {
                ConsoleUtil.printRows(dataBaseAssistant.getAllContacts());
            } else if (answer == Command.ADD) {
                dataBaseAssistant.addContact(ConsoleUtil.getNewContact());
            } else if (answer == Command.DELETE) {
                deleteContact(dataBaseAssistant);
            } else if (answer == Command.EDIT) {
                editContact(dataBaseAssistant);
            } else if (answer == Command.ABOUT) {
                ConsoleUtil.printInfo();
            } else if (answer == Command.IMPORT) {
                String path = ConsoleUtil.getLine(">> Enter path to .csv file:");
                File importFile = new File(path);
                dataBaseAssistant.importFile(importFile);
            } else if (answer == Command.EXPORT) {
                String path = ConsoleUtil.getLine(">> Enter path to .csv file:");
                File exportFile = new File(path);
                dataBaseAssistant.exportData(exportFile);
            } else if (answer == Command.INCORRECT) {
                System.out.println(">>> Incorrect command's name");
            }
        } while (!(answer == Command.EXIT));
        dataBaseAssistant.shutDown(driver);
        System.out.println("See you soon!");
    }

    static void editContact(DataBaseAssistant dataBaseAssistant) {
        String surname = ConsoleUtil.getLine(">> Enter surname of contact");
        String name = ConsoleUtil.getLine(">> Enter name of contact");
        String patronymic = ConsoleUtil.getLine(">> Enter patronymic of contact");
        if (dataBaseAssistant.isContactPresented(surname, name, patronymic)) {
            dataBaseAssistant.editContact(surname, name, patronymic,
                    ConsoleUtil.getNewContact(surname, name, patronymic));
        }
    }

    static void deleteContact(DataBaseAssistant dataBaseAssistant) {
        String surname = ConsoleUtil.getLine(">> Enter Surname:");
        String name = ConsoleUtil.getLine(">> Enter Name:");
        String patronymic = ConsoleUtil.getLine(">> Enter Patronymic:");
        dataBaseAssistant.deleteContact(surname, name, patronymic);
    }
}
