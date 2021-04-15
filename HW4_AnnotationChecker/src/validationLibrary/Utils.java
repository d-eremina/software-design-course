package validationLibrary;

public class Utils {
    /**
     * Checks if object's class is in java.lang package
     *
     * @param check Object to check
     * @return True if object's class is in java.lang package
     */
    public static boolean isJavaLang(Object check) {
        return check.getClass().getName().startsWith("java.lang");
    }

    /**
     * Checks if object's class is in java.util package
     *
     * @param check Object to check
     * @return True if object's class is in java.lang package
     */
    public static boolean isJavaUtil(Object check) {
        return check.getClass().getName().startsWith("java.util");
    }
}
