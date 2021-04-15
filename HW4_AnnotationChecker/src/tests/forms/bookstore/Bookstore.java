package tests.forms.bookstore;

import validationLibrary.annotations.Constrained;
import validationLibrary.annotations.NotNull;

import java.util.List;

@Constrained
public class Bookstore {
    @NotNull
    List<@NotNull List<@NotNull Book>> books;

    public Bookstore(List<@NotNull List<@NotNull Book>> books) {
        this.books = books;
    }
}
