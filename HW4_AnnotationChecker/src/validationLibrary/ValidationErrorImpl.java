package validationLibrary;

import java.util.Objects;

public class ValidationErrorImpl implements ValidationError {
    private final String message;
    private final String path;
    private final Object failedValue;

    public ValidationErrorImpl(String message, String path, Object failedValue) {
        this.message = message;
        this.path = path;
        this.failedValue = failedValue;
    }

    /**
     * Gets error's message
     *
     * @return Error's message
     */
    @Override
    public String getMessage() {
        return message;
    }

    /**
     * Gets error's path
     *
     * @return Error's path
     */
    @Override
    public String getPath() {
        return path;
    }

    /**
     * Gets failed value
     *
     * @return Error's failed value
     */
    @Override
    public Object getFailedValue() {
        return failedValue;
    }

    // Overriding to be able comparing errors in set later
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ValidationErrorImpl that = (ValidationErrorImpl) o;
        return Objects.equals(message, that.getMessage()) &&
                Objects.equals(path, that.getPath()) &&
                Objects.equals(failedValue, that.getFailedValue());
    }

    @Override
    public int hashCode() {
        return Objects.hash(message, path, failedValue);
    }
}