package tests;

import validationLibrary.ValidationError;

import java.util.Set;

public class Util {
    public static boolean areEqual(Set<ValidationError> a, Set<ValidationError> b){
        for(var i: a) {
            boolean found = false;
            for (var j: b) {
                if (i.equals(j)) {
                    found = true;
                    break;
                }
            }
            if (!found) {
                return false;
            }
        }
        return true;
    }
}
