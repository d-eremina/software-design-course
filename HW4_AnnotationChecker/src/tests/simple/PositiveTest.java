package tests.simple;

import org.junit.jupiter.api.Test;
import validationLibrary.*;
import validationLibrary.annotations.Constrained;
import validationLibrary.annotations.Positive;
import tests.Util;

import static org.junit.jupiter.api.Assertions.*;

import java.util.*;

public class PositiveTest {
    @Constrained
    static
    class StudentMarks {
        @Positive
        long amount;

        List<@Positive Long> marks;

        public StudentMarks(List<Long> marks) {
            this.marks = marks;
            this.amount = marks.size();
        }
    }

    @Test
    void testPositive() throws Exception {
        Validator validator = new ValidatorImpl();
        StudentMarks studentMarks = new StudentMarks(Arrays.asList((long) 1, (long) 4,
                (long) 0, (long) 3, (long) -2));
        var validationErrors = validator.validate(studentMarks);

        Set<ValidationError> expectedErrors = new HashSet<>();
        expectedErrors.add(new ValidationErrorImpl("Must be positive",
                "marks[2]", (long) 0));
        expectedErrors.add(new ValidationErrorImpl("Must be positive",
                "marks[4]", (long) -2));

        assertEquals(expectedErrors.size(), validationErrors.size());
        assertTrue(Util.areEqual(expectedErrors, validationErrors));
    }
}
