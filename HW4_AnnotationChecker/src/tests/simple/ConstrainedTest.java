package tests.simple;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import validationLibrary.*;
import validationLibrary.annotations.*;

import static org.junit.jupiter.api.Assertions.*;

import java.util.*;

public class ConstrainedTest {
    static
    class SimpleClass {
        @Positive
        int value = -1;
    }

    @Constrained
    static
    class Bank {
        List<Account> accounts;

        public Bank(List<Account> accounts) {
            this.accounts = accounts;
        }
    }

    static
    class Account {
        @AnyOf({"Credit"})
        String type;

        public Account(String type) {
            this.type = type;
        }
    }

    @Test
    void testConstrained() {
        Validator validator = new ValidatorImpl();
        SimpleClass simpleClass = new SimpleClass();

        Assertions.assertThrows(Exception.class, () -> {
            validator.validate(simpleClass);
        });
    }

    @Test
    void testInsideConstrained() throws Exception {
        Validator validator = new ValidatorImpl();
        Bank bank = new Bank(Arrays.asList(new Account(" "), new Account("Custom")));
        var validationErrors = validator.validate(bank);

        assertEquals(0, validationErrors.size());
    }
}