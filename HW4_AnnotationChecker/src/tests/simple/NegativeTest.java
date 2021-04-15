package tests.simple;

import org.junit.jupiter.api.Test;
import validationLibrary.*;
import validationLibrary.annotations.Constrained;
import validationLibrary.annotations.Negative;

import static org.junit.jupiter.api.Assertions.*;
import tests.Util;

import java.util.*;

public class NegativeTest {
    @Constrained
    static
    class WinterTemperature {
        @Negative
        int minimal = 0;

        List<@Negative Integer> measurements;
        public WinterTemperature(List<Integer> measurements) {
            this.measurements = measurements;
            for (var i: measurements) {
                if (i < minimal) {
                    minimal = i;
                }
            }
        }
    }

    @Test
    void testNegative() throws Exception {
        Validator validator = new ValidatorImpl();
        WinterTemperature winterTemperature = new WinterTemperature(Arrays.asList(-10, -2, 0, -3));
        var validationErrors = validator.validate(winterTemperature);

        Set<ValidationError> expectedErrors = new HashSet<>();
        expectedErrors.add(new ValidationErrorImpl("Must be negative",
                "measurements[2]", 0));

        assertEquals(expectedErrors.size(), validationErrors.size());
        assertTrue(Util.areEqual(expectedErrors, validationErrors));
    }
}