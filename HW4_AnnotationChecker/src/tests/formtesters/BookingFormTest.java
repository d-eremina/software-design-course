package tests.formtesters;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import tests.Util;
import tests.forms.booking.BookingForm;
import tests.forms.booking.GuestForm;
import tests.forms.booking.Unrelated;
import validationLibrary.*;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class BookingFormTest {
    @Test
    void validate() throws Exception {
        Validator validator = new ValidatorImpl();
        List<GuestForm> guests = List.of(
                new GuestForm(null, "Def", 21),
                new GuestForm("", "Ijk", -3)
        );
        Unrelated unrelated = new Unrelated(-1);
        BookingForm bookingForm = new BookingForm(
                guests, List.of("TV", "Piano"), "Apartment", unrelated
        );
        Set<ValidationError> validationErrors = validator.validate(bookingForm);

        Set<ValidationError> expectedErrors = new HashSet<>();
        expectedErrors.add(new ValidationErrorImpl("Must not be null",
                "guests[0].firstName", null));
        expectedErrors.add(new ValidationErrorImpl("Must be in range [0, 200]",
                "guests[1].age", -3));
        expectedErrors.add(new ValidationErrorImpl("Must not be blank",
                "guests[1].firstName", ""));
        expectedErrors.add(new ValidationErrorImpl("Has to be contained in array",
                "amenities[1]", "Piano"));
        expectedErrors.add(new ValidationErrorImpl("Has to be contained in array",
                "propertyType", "Apartment"));

        assertEquals(expectedErrors.size(), validationErrors.size());
        Assertions.assertTrue(Util.areEqual(expectedErrors, validationErrors));
    }
}