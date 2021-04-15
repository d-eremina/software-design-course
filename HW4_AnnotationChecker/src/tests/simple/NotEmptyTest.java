package tests.simple;

import org.junit.jupiter.api.Test;
import validationLibrary.*;
import validationLibrary.annotations.*;
import tests.Util;

import static org.junit.jupiter.api.Assertions.*;

import java.util.*;

public class NotEmptyTest {
    @Constrained
    static
    class Form {
        @NotEmpty
        String name;

        @NotEmpty
        String surname;

        public Form(String name, String surname) {
            this.name = name;
            this.surname = surname;
        }
    }

    @Constrained
    static
    class Dictionary {
        @NotEmpty
        Map<String, String> words;
        public Dictionary(Map<String, String> words) {
            this.words = words;
        }
    }

    @Test
    void testEmptyString() throws Exception {
        Validator validator = new ValidatorImpl();

        Form form1 = new Form("", " ");

        var validationErrors = validator.validate(form1);

        Set<ValidationError> expectedErrors = new HashSet<>();
        expectedErrors.add(new ValidationErrorImpl("Must not be empty",
                "name", form1.name));

        assertEquals(expectedErrors.size(), validationErrors.size());
        assertTrue(Util.areEqual(expectedErrors, validationErrors));
    }

    @Test
    void testEmptyCollection() throws Exception {
        Validator validator = new ValidatorImpl();
        Map<String, String> words = new HashMap<>();
        Dictionary dictionary = new Dictionary(words);
        var validationErrors = validator.validate(dictionary);

        Set<ValidationError> expectedErrors = new HashSet<>();
        expectedErrors.add(new ValidationErrorImpl("Must not be empty",
                "words", words));

        assertEquals(expectedErrors.size(), validationErrors.size());
        assertTrue(Util.areEqual(expectedErrors, validationErrors));
    }
}