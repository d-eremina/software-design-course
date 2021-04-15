package tests.forms.bookstore;

import validationLibrary.annotations.Constrained;
import validationLibrary.annotations.InRange;
import validationLibrary.annotations.NotBlank;
import validationLibrary.annotations.NotNull;

@Constrained
public class Author {
    @NotNull
    @NotBlank
    @InRange(min = 1, max = 30)
    String name;

    public Author(String name) {
        this.name = name;
    }
}
