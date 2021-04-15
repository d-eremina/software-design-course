package tests.simple;

import org.junit.jupiter.api.Test;
import validationLibrary.*;
import validationLibrary.annotations.*;
import tests.Util;

import static org.junit.jupiter.api.Assertions.*;

import java.util.*;

public class AnyOfTest {
    @Constrained
    static
    class ClassLanguage {
        @AnyOf({"Russian", "English"})
        String language;

        List<@AnyOf({"Advanced", "Beginner", "Intermediate"}) String> levels;
        public ClassLanguage(String language, List<String> levels) {
            this.language = language;
            this.levels = levels;
        }
    }

    @Test
    void testAnyOf() throws Exception {
        Validator validator = new ValidatorImpl();
        ClassLanguage classLanguage = new ClassLanguage("German",
                Arrays.asList("Beginner", "None", "Advanced", "Advanced", null));
        var validationErrors = validator.validate(classLanguage);

        Set<ValidationError> expectedErrors = new HashSet<>();
        expectedErrors.add(new ValidationErrorImpl("Has to be contained in array",
                "language", "German"));
        expectedErrors.add(new ValidationErrorImpl("Has to be contained in array",
                "levels[1]", "None"));

        assertEquals(expectedErrors.size(), validationErrors.size());
        assertTrue(Util.areEqual(expectedErrors, validationErrors));
    }
}