package tests.forms.booking;

import validationLibrary.annotations.Positive;

public class Unrelated {
    @Positive
    private int x;

    public Unrelated(int x) {
        this.x = x;
    }
}