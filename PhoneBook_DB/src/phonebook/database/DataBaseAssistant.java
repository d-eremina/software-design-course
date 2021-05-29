package phonebook.database;

import phonebook.contact.Contact;
import phonebook.contact.ContactValidator;

import java.io.*;
import java.sql.*;
import java.util.ArrayList;

/**
 * Class for interaction with database with given name
 */
public class DataBaseAssistant {
    private final String dbName;
    private final Connection connection;

    public DataBaseAssistant(String dbName) throws SQLException {
        this.dbName = dbName;
        // Define the Derby connection URL to use
        String connectionURL = "jdbc:derby:" + this.dbName + ";create=true";
        this.connection = DriverManager.getConnection(connectionURL);
        System.out.println("Created assistant for database " + this.dbName);
    }

    public void createTable() {
        try (PreparedStatement createStatement = this.connection.prepareStatement("CREATE TABLE " + dbName +
                " (id INTEGER NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1)," +
                "surname VARCHAR(16), name VARCHAR(16)," +
                "patronymic VARCHAR(16), mobile_phone VARCHAR(16), home_phone VARCHAR(16)," +
                "address VARCHAR(32), birth_date VARCHAR(10), comment VARCHAR(32))")) {
            if (!this.isTablePresented()) {
                System.out.println(" . . . . Creating table " + dbName);
                createStatement.executeUpdate();
            }
        } catch (SQLException e) {
            System.out.println("Can't create table: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Error during process: " + e.getMessage());
        }
    }

    /**
     * Checks if table already exists
     *
     * @return True if table already exists, false otherwise
     */
    public boolean isTablePresented() {
        try {
            DatabaseMetaData dbm = this.connection.getMetaData();
            ResultSet tables = dbm.getTables(null, null, dbName, null);
            return tables.next();
        } catch (SQLException e) {
            System.out.println("Can not check the table: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Error during checking process: " + e.getMessage());
        }
        return false;
    }

    /**
     * Exports data from table to .csv file
     *
     * @param file File for export
     */
    public void exportData(File file) {
        try (PreparedStatement exportStatement = this.connection.prepareStatement("SELECT * FROM " + dbName);
             var writer = new BufferedWriter(new FileWriter(file))) {
            ResultSet contacts = exportStatement.executeQuery();
            while (contacts.next()) {
                String[] fields = {contacts.getString(2),
                        contacts.getString(3), contacts.getString(4),
                        contacts.getString(5), contacts.getString(6),
                        contacts.getString(7), contacts.getString(8),
                        contacts.getString(9)};
                // Delimiter is ; by default
                writer.write(String.join(";", fields));
                writer.write("\n");
            }
        } catch (SQLException e) {
            System.out.println("Error with database: " + e.getMessage());
        } catch (IOException e) {
            System.out.println("File error while export: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Error during export: " + e.getMessage());
        }
    }

    /**
     * Imports contacts from file
     *
     * @param file File where data is imported from
     */
    public void importFile(File file) {
        try (var reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                var data = line.split(";(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)", -1);
                // We have exactly 8 columns (except id)
                if (data.length != 8) {
                    continue;
                }
                Contact importedContact = new Contact(data[0], data[1], data[2], data[3], data[4], data[5],
                        data[6], data[7]);
                addContact(importedContact);
            }
            System.out.println("Database will contain only correct values.");
        } catch (IOException e) {
            System.out.println("File error during import: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Error during import: " + e.getMessage());
        }
    }

    /**
     * Shows all contacts from database
     */
    public ArrayList<Contact> getAllContacts() {
        try (var listStatement =
                     this.connection.prepareStatement("SELECT * FROM " + dbName)) {
            ResultSet resultSet = listStatement.executeQuery();
            var contacts = new ArrayList<Contact>();
            while (resultSet.next()) {
                int id = resultSet.getInt(1);
                String surname = resultSet.getString(2);
                String name = resultSet.getString(3);
                String patronymic = resultSet.getString(4);
                String mobilePhone = resultSet.getString(5);
                String homePhone = resultSet.getString(6);
                String address = resultSet.getString(7);
                String birthday = resultSet.getString(8);
                String comment = resultSet.getString(9);
                contacts.add(new Contact(id, surname, name, patronymic, mobilePhone, homePhone,
                        address, birthday, comment));
            }
            return contacts;
        } catch (SQLException e) {
            System.out.println("Could not get all table rows: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Error during process: " + e.getMessage());
        }
        return new ArrayList<>();
    }

    /**
     * Deletes contact based on full name
     *
     * @param surname    Surname of contact
     * @param name       Name of contact
     * @param patronymic Patronymic of contact
     */
    public void deleteContact(String surname, String name, String patronymic) {
        if (!isContactPresented(surname, name, patronymic)) {
            System.out.printf("Contact with full name \"%s %s %s\" does not exist\n",
                    surname, name, patronymic);
            return;
        }
        try (var deleteStatement = this.connection
                .prepareStatement("DELETE FROM " + dbName + " WHERE " + "surname='" + surname +
                        "' AND name='" + name + "' AND patronymic='" + patronymic + "'")) {
            deleteStatement.executeUpdate();
            System.out.println("Successfully");
        } catch (SQLException e) {
            System.out.println("Could not delete contact: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Error during process: " + e.getMessage());
        }
    }

    /**
     * Adds contact to database
     *
     * @param contact Contact to add to database
     */
    public void addContact(Contact contact) {
        if (!ContactValidator.isContactValid(contact)) {
            System.out.println("Can't add contact because it has incorrect values in it's fields");
            return;
        }
        String checkString = "SELECT * FROM " + dbName +
                " WHERE surname='" + contact.getSurname() + "' AND name='" + contact.getName()
                + "' AND patronymic='" + contact.getPatronymic() + "'";
        String addString = "INSERT INTO " + dbName +
                " (surname, name, patronymic, mobile_phone, home_phone, address, birth_date, comment) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try (var checkStatement = this.connection.prepareStatement(checkString);
             var addStatement = this.connection.prepareStatement(addString)) {
            if (checkStatement.executeQuery().next()) {
                System.out.printf("Contact with full name \"%s %s %s\" already exists\n",
                        contact.getSurname(), contact.getName(), contact.getPatronymic());
                return;
            }
            addStatement.setString(1, contact.getSurname());
            addStatement.setString(2, contact.getName());
            addStatement.setString(3, contact.getPatronymic());
            addStatement.setString(4, contact.getMobilePhone());
            addStatement.setString(5, contact.getHomePhone());
            addStatement.setString(6, contact.getAddress());
            addStatement.setString(7, contact.getBirthDate());
            addStatement.setString(8, contact.getComment());
            addStatement.executeUpdate();
            System.out.println("Successfully added");
        } catch (SQLException e) {
            System.out.println("Can't add contact: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Error during adding: " + e.getMessage());
        }
    }

    /**
     * Checks if contact with given full name exists in table
     *
     * @param surname    Surname of contact
     * @param name       Name of contact
     * @param patronymic Patronymic of contact
     * @return True if contact is presented in table, false otherwise
     */
    public boolean isContactPresented(String surname, String name, String patronymic) {
        String checkString = "SELECT * FROM " + dbName +
                " WHERE surname='" + surname + "' AND name='" + name
                + "' AND patronymic='" + patronymic + "'";
        try (var checkStatement = this.connection.prepareStatement(checkString)) {
            if (!checkStatement.executeQuery().next()) {
                System.out.printf("Contact with full name \"%s %s %s\" does not exist\n",
                        surname, name, patronymic);
                return false;
            }
            return true;
        } catch (SQLException e) {
            System.out.println("Error with table while checking contact: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Error during checking contact: " + e.getMessage());
        }
        return false;
    }

    /**
     * Updates contact's fields in database
     */
    public void editContact(String surname, String name, String patronymic, Contact newContact) {
        String checkString = "SELECT * FROM " + dbName +
                " WHERE surname='" + surname + "' AND name='" + name
                + "' AND patronymic='" + patronymic + "'";
        String updateString = "UPDATE " + dbName +
                " SET mobile_phone = ?, home_phone = ?, address = ?, birth_date = ?, comment = ? " +
                "WHERE id = ?";

        try (var checkStatement = this.connection.prepareStatement(checkString);
             var updateStatement = this.connection.prepareStatement(updateString)) {
            if (!ContactValidator.isContactValid(newContact)) {
                System.out.println("Contact has fields with incorrect values");
                return;
            }
            ResultSet selected = checkStatement.executeQuery();
            selected.next();
            int id = selected.getInt(1);
            updateStatement.setString(1, newContact.getMobilePhone());
            updateStatement.setString(2, newContact.getHomePhone());
            updateStatement.setString(3, newContact.getAddress());
            updateStatement.setString(4, newContact.getBirthDate());
            updateStatement.setString(5, newContact.getComment());
            updateStatement.setInt(6, id);
            updateStatement.executeUpdate();
            System.out.println("Successfully updated");
        } catch (SQLException e) {
            System.out.println("Can't edit contact: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Error during editing process: " + e.getMessage());
        }
    }

    /**
     * Shuts database down
     *
     * @param driver Driver for database
     */
    public void shutDown(String driver) {
        if (driver.equals("org.apache.derby.jdbc.EmbeddedDriver")) {
            boolean gotSQLExc = false;
            try {
                connection.close();
                DriverManager.getConnection("jdbc:derby:" + this.dbName + ";shutdown=true");
            } catch (SQLException se) {
                gotSQLExc = se.getSQLState().equals("08006");
            }
            if (!gotSQLExc) {
                System.out.println("Database did not shut down normally");
            } else {
                System.out.println("Database shut down normally");
            }
        }
    }

    /**
     * Deletes table
     */
    public void deleteTable() {
        try (var statement = connection.prepareStatement("DROP TABLE " + dbName)) {
            statement.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Can't drop table: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Error during process: " + e.getMessage());
        }
    }
}
