package tests.simple;

import org.junit.jupiter.api.Test;
import tests.Util;
import validationLibrary.*;
import validationLibrary.annotations.*;

import static org.junit.jupiter.api.Assertions.*;

import java.util.*;

public class SizeTest {
    @Constrained
    static
    class Form {
        @Size(min = 1, max = 20)
        String name;

        @Size(min = 1, max = 20)
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
    void testSize() throws Exception {
        Validator validator = new ValidatorImpl();

        Form form1 = new Form("", "ABC");
        Form form2 = new Form("aaaaaaaaaaaaaaaaaaaaa", " ");
        Form form3 = new Form(null, " abc");

        Forms forms = new Forms(Arrays.asList(form1, form2, form3));

        var validationErrors = validator.validate(forms);

        Set<ValidationError> expectedErrors = new HashSet<>();
        expectedErrors.add(new ValidationErrorImpl("Must have size in range [1, 20]",
                "forms[0].name", form1.name));
        expectedErrors.add(new ValidationErrorImpl("Must have size in range [1, 20]",
                "forms[1].name", form2.name));

        assertEquals(expectedErrors.size(), validationErrors.size());
        assertTrue(Util.areEqual(expectedErrors, validationErrors));
    }
}