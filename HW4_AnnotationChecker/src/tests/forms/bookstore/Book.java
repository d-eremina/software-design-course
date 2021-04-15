package tests.forms.bookstore;

import validationLibrary.annotations.Constrained;
import validationLibrary.annotations.NotBlank;
import validationLibrary.annotations.NotNull;

@Constrained
public class Book {
    @NotNull
    Author author;
    @NotNull
    @NotBlank
    String heading;

    public Book(Author author, String heading) {
        this.author = author;
        this.heading = heading;
    }
}
