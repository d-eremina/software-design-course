package tests.simple;

import org.junit.jupiter.api.Test;
import validationLibrary.*;
import validationLibrary.annotations.*;
import tests.Util;

import static org.junit.jupiter.api.Assertions.*;

import java.util.*;

public class NotBlankTest {
    @Constrained
    static
    class Form {
        @NotBlank
        String name;

        @NotBlank
        String surname;

        public Form(String name, String surname) {
            this.name = name;
            this.surname = surname;
        }
    }

    @Constrained
    static
    class Forms {
        List<Form> forms;

        public Forms(List<Form> forms) {
            this.forms = forms;
        }
    }

    @Test
    void testBlank() throws Exception {
        Validator validator = new ValidatorImpl();

        Form form1 = new Form(" ", "ABC");
        Form form2 = new Form(" a ", "");
        Form form3 = new Form(null, " abc");

        Forms forms = new Forms(Arrays.asList(form1, form2, form3));

        var validationErrors = validator.validate(forms);

        Set<ValidationError> expectedErrors = new HashSet<>();
        expectedErrors.add(new ValidationErrorImpl("Must not be blank",
                "forms[0].name", form1.name));
        expectedErrors.add(new ValidationErrorImpl("Must not be blank",
                "forms[1].surname", form2.surname));

        assertEquals(expectedErrors.size(), validationErrors.size());
        assertTrue(Util.areEqual(expectedErrors, validationErrors));
    }
}