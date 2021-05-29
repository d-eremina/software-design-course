package phonebook.contact;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.regex.Pattern;

/***
 * Class for contact's fields validation
 */
public class ContactValidator {
    /**
     * Checks if all fields of contact are valid
     *
     * @param contact Contact to check
     * @return True if contact is valid, false otherwise
     */
    public static boolean isContactValid(Contact contact) {
        return isSurnameValid(contact.getSurname()).isEmpty() &&
                isNameValid(contact.getName()).isEmpty() &&
                isPatronymicValid(contact.getPatronymic()).isEmpty() &&
                isDateValid(contact.getBirthDate()).isEmpty() &&
                isAddressValid(contact.getAddress()).isEmpty() &&
                isCommentValid(contact.getComment()).isEmpty() &&
                (contact.getMobilePhone().isEmpty() && isPhoneValid(contact.getHomePhone(), true).isEmpty() ||
                        contact.getHomePhone().isEmpty() &&
                                isPhoneValid(contact.getMobilePhone(), true).isEmpty() ||
                        isPhoneValid(contact.getMobilePhone(), true).isEmpty()
                                && isPhoneValid(contact.getHomePhone(), true).isEmpty());
    }

    /**
     * Checks if surname is valid
     *
     * @param surname Surname to check
     * @return String containing error message
     */
    public static String isSurnameValid(String surname) {
        // Surname can not be empty
        if (surname.isEmpty() || surname.isBlank()) {
            return ">>> Surname can not be empty or blank\n";
        }
        // Only roman and cyrillic alphabets and spaces are allowed
        if (!Pattern.matches("^[\\p{L} ]+$", surname)) {
            return ">>> Surname can contain only letters and spaces\n";
        }
        if (surname.toCharArray().length > 16) {
            return ">>> Length of surname must be <= 16 symbols\n";
        }
        return "";
    }

    /**
     * Checks if name is valid
     *
     * @param name Name to check
     * @return String containing error message
     */
    public static String isNameValid(String name) {
        // Surname can not be empty
        if (name.isEmpty() || name.isBlank()) {
            return ">>> Name can not be empty or blank\n";
        }
        // Only roman and cyrillic alphabets and spaces are allowed
        if (!Pattern.matches("^[\\p{L} ]+$", name)) {
            return ">>> Name can contain only letters and spaces\n";
        }
        if (name.toCharArray().length > 16) {
            return ">>> Length of name must be <= 16 symbols\n";
        }
        return "";
    }

    /**
     * Checks if patronymic is valid
     *
     * @param patronymic Patronymic to check
     * @return String containing error message
     */
    public static String isPatronymicValid(String patronymic) {
        // Not entering patronymic is allowed
        if (patronymic.isEmpty()) {
            return "";
        }
        // Error if patronymic contains only spaces
        if (patronymic.isBlank()) {
            return ">>> Patronymic can't be blank\n";
        }
        // Only roman and cyrillic alphabets and spaces are allowed
        if (!Pattern.matches("^[\\p{L} ]+$", patronymic)) {
            return ">>> Patronymic can contain only letters and spaces\n";
        }
        if (patronymic.toCharArray().length > 16) {
            return ">>> Length of patronymic must be <= 16 symbols\n";
        }
        return "";
    }

    /**
     * Checks if phone number is valid
     *
     * @param phone    Phone number
     * @param required Is number required
     * @return String containing error message
     */
    public static String isPhoneValid(String phone, Boolean required) {
        if (required && phone.isEmpty()) {
            return ">>> You must have at least one phone number\n";
        }
        // Not having one number is allowed
        if (phone.isEmpty()) {
            return "";
        }
        // Error if phone contains only spaces
        if (phone.isBlank()) {
            return ">>> Phone number can not be blank\n";
        }
        if (phone.toCharArray().length > 16) {
            return ">>> Length of phone number must be <= 16 symbols\n";
        }
        // Only "+" and digits are allowed
        if (phone.startsWith("+") && Pattern.matches("[0-9]+", phone.substring(1))
                || Pattern.matches("[0-9]+", phone)) {
            return "";
        }
        return "Phone number must contain only digits and start with +\n";
    }

    /**
     * Checks if address is valid
     *
     * @param address Address to check
     * @return String containing error message
     */
    public static String isAddressValid(String address) {
        // Empty address is allowed
        if (address.isEmpty()) {
            return "";
        }
        // Error if address contains only spaces
        if (address.isBlank()) {
            return ">>> Address can't be blank\n";
        }
        if (address.toCharArray().length > 32) {
            return ">>> Length of address must be <= 32 symbols\n";
        }
        // For correct import/export with delimiter ";"
        if (address.indexOf(';') >= 0) {
            return ">>> Please, avoid symbol \";\"\n";
        }
        return "";
    }

    /**
     * Checks if comment is valid
     *
     * @param comment Comment to check
     * @return String containing error message
     */
    public static String isCommentValid(String comment) {
        // Empty comment is allowed
        if (comment.isEmpty()) {
            return "";
        }
        // Error if comment contains only spaces
        if (comment.isBlank()) {
            return ">>> Comment can not be blank\n";
        }
        if (comment.toCharArray().length > 32) {
            return ">>> Length of comment must be <= 32\n";
        }
        // For correct import/export with delimiter ";"
        if (comment.indexOf(';') >= 0) {
            return ">>> Please, avoid symbol \";\"\n";
        }
        return "";
    }

    /**
     * Checks if birth date is valid
     *
     * @param date Date to check
     * @return String containing error message
     */
    public static String isDateValid(String date) {
        // Not entering birthday is allowed
        if (date.isEmpty()) {
            return "";
        }
        if (date.isBlank()) {
            return ">>> Date can not be blank\n";
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        LocalDate birthDate;
        try{
            birthDate = LocalDate.parse(date, formatter);
        } catch (Exception e) {
            return ">>> Incorrect format of date\n";
        }
        if (birthDate == null) {
            return ">>> Incorrect format of date\n";
        }
        // Only dates before today are allowed
        if (birthDate.isAfter(LocalDate.now())) {
            return ">>> Incorrect date - birth date can't be in the future\n";
        }
        return "";
    }
}
