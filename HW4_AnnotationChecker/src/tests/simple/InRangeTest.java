package tests.simple;

import org.junit.jupiter.api.Test;
import validationLibrary.*;
import validationLibrary.annotations.*;
import tests.Util;

import static org.junit.jupiter.api.Assertions.*;

import java.util.*;

public class InRangeTest {
    @Constrained
    static
    class StudentMarks {
        List<@InRange(min = 1, max = 5) Integer> marks;
        public StudentMarks(List<Integer> marks) {
            this.marks = marks;
        }
    }

    @Test
    void testInRange() throws Exception {
        Validator validator = new ValidatorImpl();
        StudentMarks studentMarks = new StudentMarks(Arrays.asList(3, 4, 0, 6, 5));
        var validationErrors = validator.validate(studentMarks);

        Set<ValidationError> expectedErrors = new HashSet<>();
        expectedErrors.add(new ValidationErrorImpl("Must be in range [1, 5]",
                "marks[2]", 0));
        expectedErrors.add(new ValidationErrorImpl("Must be in range [1, 5]",
                "marks[3]", 6));

        assertEquals(expectedErrors.size(), validationErrors.size());
        assertTrue(Util.areEqual(expectedErrors, validationErrors));
    }
}