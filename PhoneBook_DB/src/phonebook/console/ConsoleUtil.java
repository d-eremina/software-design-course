package phonebook.console;

import phonebook.contact.Contact;
import phonebook.contact.ContactValidator;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;

/***
 * Class with static methods for user's interaction
 */
public class ConsoleUtil {
    private static final String COMMAND_LIST =
            "********************************************************\n" +
                    "*                     ENTER COMMAND                    *\n" +
                    "********************************************************\n" +
                    "*            list - view all contacts                  *\n" +
                    "*            add - add new contact                     *\n" +
                    "*            delete - delete contact                   *\n" +
                    "*            edit - edit contact                       *\n" +
                    "*            about - view info                         *\n" +
                    "*            import - import contacts from file        *\n" +
                    "*            export - export contacts to .csv file     *\n" +
                    "*            exit - quit working                       *\n" +
                    "********************************************************\n";

    private static final String INFO =
            "******************************\n" +
                    "*         Created by         *\n" +
                    "*        Daria Eremina       *\n" +
                    "*           BSE196           *\n" +
                    "*          May, 2021         *\n" +
                    "*     all rights reserved    *\n" +
                    "******************************\n";

    /**
     * Prints info about program
     */
    public static void printInfo() {
        System.out.println(INFO);
    }

    /**
     * Gets command from command line
     *
     * @return Chosen command
     */
    public static Command getNextCommand() {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        Command ans = Command.INCORRECT;
        try {
            while (ans == Command.INCORRECT) {
                System.out.println(COMMAND_LIST);
                ans = converter(br.readLine());
            }
        } catch (java.io.IOException e) {
            System.out.println("Could not read response from stdin");
        }
        return ans;
    }

    /**
     * Prints database's rows from given set
     *
     * @param contacts List with contacts
     */
    public static void printRows(ArrayList<Contact> contacts) {
        try {
            System.out.println("-".repeat(185));
            System.out.printf("| %-2s |", "ID");
            System.out.printf(" %-16s |", "SURNAME");
            System.out.printf(" %-16s |", "NAME");
            System.out.printf(" %-16s |", "PATRONYMIC");
            System.out.printf(" %-16s |", "MOBILE_PHONE");
            System.out.printf(" %-16s |", "HOME_PHONE");
            System.out.printf(" %-32s |", "ADDRESS");
            System.out.printf(" %-11s |", "BIRTHDAY");
            System.out.printf(" %-32s |\n", "COMMENT");
            System.out.println("-".repeat(185));
            for (var contact : contacts) {
                System.out.printf("| %-2s |", contact.getId());
                System.out.printf(" %-16s |", contact.getSurname());
                System.out.printf(" %-16s |", contact.getName());
                System.out.printf(" %-16s |", contact.getPatronymic());
                System.out.printf(" %-16s |", contact.getMobilePhone());
                System.out.printf(" %-16s |", contact.getHomePhone());
                System.out.printf(" %-32s |", contact.getAddress());
                System.out.printf(" %-11s |", contact.getBirthDate());
                System.out.printf(" %-32s |\n", contact.getComment());
            }
            System.out.println("-".repeat(185));
        } catch (Exception e) {
            System.out.println("Can't get result of query because of error: " + e.getMessage());
        }
    }

    /**
     * Reads line from console
     *
     * @param prompt Prompt for user
     * @return String from command line
     */
    public static String getLine(String prompt) {
        System.out.println(prompt);
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        try {
            return br.readLine();
        } catch (java.io.IOException e) {
            System.out.println("Could not read response from stdin");
        } catch (Exception e) {
            System.out.println("Error during reading from command line: " + e.getMessage());
        }
        return "";
    }

    /**
     * Gets surname of a contact from command line
     *
     * @return Entered surname
     */
    public static String getSurname() {
        System.out.println(">> Enter Surname");
        String surname = "";
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        try {
            surname = br.readLine();
            String response = ContactValidator.isSurnameValid(surname);
            while (!response.isEmpty()) {
                System.out.println(response);
                System.out.println(">> Enter Surname");
                surname = br.readLine();
                response = ContactValidator.isSurnameValid(surname);
            }
        } catch (java.io.IOException e) {
            System.out.println("Could not read response from stdin");
        } catch (Exception e) {
            System.out.println("Error during reading surname from command line: " + e.getMessage());
        }
        return surname;
    }

    /**
     * Gets name of a contact from command line
     *
     * @return Entered name
     */
    public static String getName() {
        System.out.println(">> Enter Name");
        String name = "";
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        try {
            name = br.readLine();
            String response = ContactValidator.isNameValid(name);
            while (!response.isEmpty()) {
                System.out.println(response);
                System.out.println(">> Enter Name");
                name = br.readLine();
                response = ContactValidator.isNameValid(name);
            }
        } catch (java.io.IOException e) {
            System.out.println("Could not read response from stdin");
        } catch (Exception e) {
            System.out.println("Error during reading name from command line: " + e.getMessage());
        }
        return name;
    }

    /**
     * Gets patronymic of a contact from command line
     *
     * @return Entered patronymic
     */
    public static String getPatronymic() {
        System.out.println(">> Enter Patronymic [optional -> press enter to continue]");
        String patronymic = "";
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        try {
            patronymic = br.readLine();
            String response = ContactValidator.isPatronymicValid(patronymic);
            while (!response.isEmpty()) {
                System.out.println(response);
                System.out.println(">> Enter Patronymic [optional -> press enter to continue]");
                patronymic = br.readLine();
                response = ContactValidator.isPatronymicValid(patronymic);
            }
        } catch (java.io.IOException e) {
            System.out.println("Could not read response from stdin");
        } catch (Exception e) {
            System.out.println("Error during reading patronymic from command line: " + e.getMessage());
        }
        return patronymic;
    }

    /**
     * Gets mobile phone number of a contact from command line
     *
     * @return Entered mobile phone number
     */
    public static String getMobilePhone() {
        System.out.println(">> Enter Mobile Phone\n" +
                "[If you leave it empty, you must enter Home Phone on the next step]");
        String mobilePhone = "";
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        try {
            mobilePhone = br.readLine();
            String response = ContactValidator.isPhoneValid(mobilePhone, false);
            while (!response.isEmpty()) {
                System.out.println(response);
                System.out.println(">> Enter Mobile Phone\n" +
                        "[If you leave it empty, you must enter Home Phone on the next step]");
                mobilePhone = br.readLine();
                response = ContactValidator.isPhoneValid(mobilePhone, false);
            }
        } catch (java.io.IOException e) {
            System.out.println("Could not read response from stdin");
        } catch (Exception e) {
            System.out.println("Error during reading phone number from command line: " + e.getMessage());
        }
        return mobilePhone;
    }

    /**
     * Gets mobile phone number of a contact from command line
     *
     * @param required If person must enter home phone number
     * @return Entered home phone number
     */
    public static String getHomePhone(Boolean required) {
        System.out.println(">> Enter Home Phone\n" +
                "[You may leave it empty if you already entered mobile phone]");
        String homePhone = "";
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        try {
            homePhone = br.readLine();
            String response = ContactValidator.isPhoneValid(homePhone, required);
            while (!response.isEmpty()) {
                System.out.println(response);
                System.out.println(">> Enter Home Phone\n" +
                        "[You may leave it empty if you already entered mobile phone]");
                homePhone = br.readLine();
                response = ContactValidator.isPhoneValid(homePhone, required);
            }
        } catch (java.io.IOException e) {
            System.out.println("Could not read response from stdin");
        } catch (Exception e) {
            System.out.println("Error during reading home phone number from command line: " + e.getMessage());
        }
        return homePhone;
    }

    /**
     * Gets address of a contact from command line
     *
     * @return Entered address
     */
    public static String getAddress() {
        System.out.println(">> Enter Address [optional -> press enter to continue]");
        String address = "";
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        try {
            address = br.readLine();
            String response = ContactValidator.isAddressValid(address);
            while (!response.isEmpty()) {
                System.out.println(response);
                System.out.println(">> Enter Addresss [optional -> press enter to continue]");
                address = br.readLine();
                response = ContactValidator.isAddressValid(address);
            }
        } catch (java.io.IOException e) {
            System.out.println("Could not read response from stdin");
        } catch (Exception e) {
            System.out.println("Error during reading address from command line: " + e.getMessage());
        }
        return address;
    }

    /**
     * Gets comment about a contact from command line
     *
     * @return Entered comment
     */
    public static String getComment() {
        System.out.println(">> Enter Comment [optional -> press enter to continue]");
        String comment = "";
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        try {
            comment = br.readLine();
            String response = ContactValidator.isCommentValid(comment);
            while (!response.isEmpty()) {
                System.out.println(response);
                System.out.println(">> Enter Comment [optional -> press enter to continue]");
                comment = br.readLine();
                response = ContactValidator.isCommentValid(comment);
            }
        } catch (java.io.IOException e) {
            System.out.println("Could not read response from stdin");
        } catch (Exception e) {
            System.out.println("Error during reading comment from command line: " + e.getMessage());
        }
        return comment;
    }

    /**
     * Gets birth date of a contact from command line
     *
     * @return Entered birth date
     */
    public static String getBirthDate() {
        System.out.println(">> Enter Date in format dd.MM.yyyy [optional -> press enter to continue]");
        String date = "";
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        try {
            date = br.readLine();
            String response = ContactValidator.isDateValid(date);
            while (!response.isEmpty()) {
                System.out.println(response);
                System.out.println(">> Enter Date in format dd.MM.yyyy [optional -> press enter to continue]");
                date = br.readLine();
                response = ContactValidator.isDateValid(date);
            }
        } catch (java.io.IOException e) {
            System.out.println("Could not read response from stdin");
        } catch (Exception e) {
            System.out.println("Error during reading birth date from command line: " + e.getMessage());
        }
        return date;
    }

    /**
     * Gets new contact from command line
     *
     * @return New contact
     */
    public static Contact getNewContact() {
        String surname = ConsoleUtil.getSurname();
        String name = ConsoleUtil.getName();
        String patronymic = ConsoleUtil.getPatronymic();
        return getNewContact(surname, name, patronymic);
    }

    /**
     * Gets new contact from command line
     *
     * @param surname    Surname of contact
     * @param name       Name of contact
     * @param patronymic Patronymic of contact
     * @return New contact
     */
    public static Contact getNewContact(String surname, String name, String patronymic) {
        String mobilePhone = ConsoleUtil.getMobilePhone();
        String homePhone = ConsoleUtil.getHomePhone(mobilePhone.isEmpty());
        String address = ConsoleUtil.getAddress();
        String birthday = ConsoleUtil.getBirthDate();
        String comment = ConsoleUtil.getComment();
        return new Contact(surname, name, patronymic, mobilePhone, homePhone, address, birthday, comment);
    }

    /**
     * Converts string from command line to command for program
     *
     * @param string User's input
     * @return Corresponding command
     */
    private static Command converter(String string) {
        switch (string) {
            case "list":
                return Command.LIST;
            case "add":
                return Command.ADD;
            case "delete":
                return Command.DELETE;
            case "edit":
                return Command.EDIT;
            case "about":
                return Command.ABOUT;
            case "import":
                return Command.IMPORT;
            case "export":
                return Command.EXPORT;
            case "exit":
                return Command.EXIT;
            default:
                return Command.INCORRECT;
        }
    }
}
