package tests.simple;

import tests.Util;
import validationLibrary.ValidationError;
import validationLibrary.ValidationErrorImpl;
import validationLibrary.Validator;
import validationLibrary.ValidatorImpl;
import validationLibrary.annotations.Constrained;
import validationLibrary.annotations.InRange;
import validationLibrary.annotations.Negative;
import validationLibrary.annotations.Positive;

import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class IncorrectTest {
    @Constrained
    static
    class Example {
        @Positive
        @Negative
        short value;

        public Example(Short value) {
            this.value = value;
        }
    }

    @Test
    void testNegative() throws Exception {
        Example example = new Example((short) -1);

        Validator validator = new ValidatorImpl();
        Set<ValidationError> validationErrors = validator.validate(example);

        Set<ValidationError> expectedErrors = new HashSet<>();
        expectedErrors.add(new ValidationErrorImpl("Must be positive",
                "value", (short) -1));

        assertEquals(expectedErrors.size(), validationErrors.size());
        assertTrue(Util.areEqual(expectedErrors, validationErrors));
    }

    @Constrained
    static
    class Range {
        @InRange(min = -1, max = -4)
        byte value;

        public Range(byte value) {
            this.value = value;
        }
    }

    @Test
    void test() throws Exception {
        Example example = new Example((short) 0);

        Validator validator = new ValidatorImpl();
        Set<ValidationError> validationErrors = validator.validate(example);

        Set<ValidationError> expectedErrors = new HashSet<>();
        expectedErrors.add(new ValidationErrorImpl("Must be positive",
                "value", (short) 0));
        expectedErrors.add(new ValidationErrorImpl("Must be negative",
                "value", (short) 0));

        assertEquals(expectedErrors.size(), validationErrors.size());
        assertTrue(Util.areEqual(expectedErrors, validationErrors));
    }

    @Test
    void testRange() throws Exception {
        Range range = new Range((byte) -2);

        Validator validator = new ValidatorImpl();
        Set<ValidationError> validationErrors = validator.validate(range);

        Set<ValidationError> expectedErrors = new HashSet<>();
        expectedErrors.add(new ValidationErrorImpl("Must be in range [-1, -4]",
                "value", (byte) -2));

        assertEquals(expectedErrors.size(), validationErrors.size());
        assertTrue(Util.areEqual(expectedErrors, validationErrors));
    }
}
