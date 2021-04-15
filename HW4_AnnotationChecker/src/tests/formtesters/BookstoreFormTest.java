package tests.formtesters;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import tests.Util;
import tests.forms.bookstore.Author;
import tests.forms.bookstore.Book;
import tests.forms.bookstore.Bookstore;
import validationLibrary.*;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

public class BookstoreFormTest {
    @Test
    void testNull() throws Exception {
        Validator validator = new ValidatorImpl();
        Bookstore bookstore = new Bookstore(null);
        Set<ValidationError> validationErrors = validator.validate(bookstore);

        Set<ValidationError> expectedErrors = new HashSet<>();
        expectedErrors.add(new ValidationErrorImpl("Must not be null",
                "books", null));

        assertEquals(expectedErrors.size(), validationErrors.size());
        Assertions.assertTrue(Util.areEqual(expectedErrors, validationErrors));
    }

    @Test
    void testNullElement() throws Exception {
        Validator validator = new ValidatorImpl();
        List<List<Book>> books = new ArrayList<>();
        List<Book> firstLine = new ArrayList<>();
        firstLine.add(null);
        books.add(firstLine);
        Bookstore bookstore = new Bookstore(books);
        var validationErrors = validator.validate(bookstore);

        Set<ValidationError> expectedErrors = new HashSet<>();
        expectedErrors.add(new ValidationErrorImpl("Must not be null",
                "books[0][0]", null));

        assertEquals(expectedErrors.size(), validationErrors.size());
        Assertions.assertTrue(Util.areEqual(expectedErrors, validationErrors));
    }

    @Test
    void test() throws Exception {
        Validator validator = new ValidatorImpl();
        List<List<Book>> books = new ArrayList<>();
        List<Book> firstLine = new ArrayList<>();
        List<Book> secondLine = new ArrayList<>();

        Author shakespeare = new Author("William Shakespeare");

        firstLine.add(new Book(null, "ABC"));
        firstLine.add(new Book(shakespeare, "Hamlet"));
        secondLine.add(new Book(shakespeare, " "));

        books.add(firstLine);
        books.add(secondLine);

        Bookstore bookstore = new Bookstore(books);
        var validationErrors = validator.validate(bookstore);

        Set<ValidationError> expectedErrors = new HashSet<>();
        expectedErrors.add(new ValidationErrorImpl("Must not be null",
                "books[0][0].author", null));
        expectedErrors.add(new ValidationErrorImpl("Must not be blank",
                "books[1][0].heading", " "));

        assertEquals(expectedErrors.size(), validationErrors.size());
        Assertions.assertTrue(Util.areEqual(expectedErrors, validationErrors));
    }
}
