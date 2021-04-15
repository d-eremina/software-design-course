package tests.simple;

import org.junit.jupiter.api.Test;
import validationLibrary.*;
import validationLibrary.annotations.*;
import tests.Util;

import static org.junit.jupiter.api.Assertions.*;

import java.util.*;

public class NullTest {
    @Constrained
    static
    class Book {
        @NotNull
        @NotBlank
        String heading;

        public Book(String heading) {
            this.heading = heading;
        }
    }

    @Constrained
    static
    class BookStore {
        @NotNull
        List<@NotNull Book> books;

        public BookStore(List<Book> books) {
            this.books = books;
        }
    }

    @Test
    void testNull() throws Exception {
        Validator validator = new ValidatorImpl();

        Book book1 = new Book(null);
        Book book2 = new Book(" ");
        Book book3 = new Book(" A ");
        BookStore bookStore = new BookStore(Arrays.asList(book1, book2, book3));

        var validationErrors = validator.validate(bookStore);

        Set<ValidationError> expectedErrors = new HashSet<>();
        expectedErrors.add(new ValidationErrorImpl("Must not be null",
                "books[0].heading", book1.heading));
        expectedErrors.add(new ValidationErrorImpl("Must not be blank",
                "books[1].heading", book2.heading));

        assertEquals(expectedErrors.size(), validationErrors.size());
        assertTrue(Util.areEqual(expectedErrors, validationErrors));
    }

    @Test
    void testNullList() throws Exception {
        Validator validator = new ValidatorImpl();

        BookStore bookStore = new BookStore(null);

        var validationErrors = validator.validate(bookStore);

        Set<ValidationError> expectedErrors = new HashSet<>();
        expectedErrors.add(new ValidationErrorImpl("Must not be null",
                "books", null));

        assertEquals(expectedErrors.size(), validationErrors.size());
        assertTrue(Util.areEqual(expectedErrors, validationErrors));
    }
}
