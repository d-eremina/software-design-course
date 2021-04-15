package tests.simple;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import validationLibrary.Validator;
import validationLibrary.ValidatorImpl;
import validationLibrary.annotations.*;

import java.util.Arrays;
import java.util.List;

public class ExceptionTest {
    @Constrained
    static
    class Mark {
        @Size(min = 1, max = 5)
        int score;

        public Mark(int score) {
            this.score = score;
        }
    }

    @Constrained
    static
    class Journal {
        @Positive
        List<Integer> marks;

        public Journal(List<Integer> marks) {
            this.marks = marks;
        }
    }

    @Constrained
    static
    class Form {
        @Negative
        byte value;

        @NotBlank
        List<Integer> list;

        public Form(byte value, List<Integer> list) {
            this.list = list;
            this.value = value;
        }
    }

    @Test
    void testExpectedExceptionFail() {
        Validator validator = new ValidatorImpl();

        Assertions.assertThrows(Exception.class, () -> {
            Mark mark = new Mark(3);
            validator.validate(mark);
        });

        Assertions.assertThrows(Exception.class, () -> {
            Journal journal = new Journal(Arrays.asList(2, 3, 4));
            validator.validate(journal);
        });

        Assertions.assertThrows(Exception.class, () -> {
            Form form = new Form((byte) -3, Arrays.asList(2, 3, 4));
            validator.validate(form);
        });
    }
}
