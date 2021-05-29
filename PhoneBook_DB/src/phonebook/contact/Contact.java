package phonebook.contact;

import java.io.Serializable;
import java.util.Objects;

/**
 * Class for better interaction with table rows
 */
public class Contact implements Serializable {
    private final int id;

    private final String name;
    private final String surname;
    private final String patronymic;

    private final String mobilePhone;
    private final String homePhone;

    private final String address;
    private final String birthDate;
    private final String comment;

    public Contact(int id, String surname, String name, String patronymic, String mobilePhone,
                   String homePhone, String address, String birthDate, String note) {
        this.id = id;
        this.name = name;
        this.surname = surname;
        this.patronymic = patronymic;
        this.mobilePhone = mobilePhone;
        this.homePhone = homePhone;
        this.address = address;
        this.birthDate = birthDate;
        this.comment = note;
    }

    /**
     * For temporary contacts
     */
    public Contact(String surname, String name, String patronymic, String mobilePhone,
                   String homePhone, String address, String birthDate, String note) {
        this(0, surname, name, patronymic, mobilePhone, homePhone, address, birthDate, note);
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getSurname() {
        return surname;
    }

    public String getPatronymic() {
        return patronymic;
    }

    public String getMobilePhone() {
        return mobilePhone;
    }

    public String getHomePhone() {
        return homePhone;
    }

    public String getAddress() {
        return address;
    }

    public String getBirthDate() {
        return birthDate;
    }

    public String getComment() {
        return comment;
    }

    /**
     * Compares two contacts based on their full names
     *
     * @param o Other object
     * @return True if objects are equal, else otherwise
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Contact contact = (Contact) o;
        return Objects.equals(name, contact.name) &&
                Objects.equals(surname, contact.surname) &&
                Objects.equals(patronymic, contact.patronymic);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, surname, patronymic);
    }
}
