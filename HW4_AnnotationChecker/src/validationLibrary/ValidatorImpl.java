package validationLibrary;

import validationLibrary.annotations.*;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedParameterizedType;
import java.util.*;

public class ValidatorImpl implements Validator {
    Set<ValidationError> validationErrors = new HashSet<>();
    String rootPath;
    String fieldName;
    String path;

    public ValidatorImpl() {
        rootPath = "";
        path = "";
    }

    private ValidatorImpl(String rootPath) {
        this.rootPath = rootPath;
    }

    /**
     * Checks if all values are correct with given annotation in object's class
     *
     * @param object Object to check
     * @return Set with all errors found in values
     * @throws Exception If annotations is presented by invalid type
     */
    @Override
    public Set<ValidationError> validate(Object object) throws Exception {
        // If we just started validation and class wasn't marked as constrained, exception is thrown
        if (rootPath.isEmpty() && !object.getClass().isAnnotationPresent(Constrained.class)) {
            throw new Exception("Validated object must be constrained");
        }
        // We do not check object inside if it is not constrained
        if (!object.getClass().isAnnotationPresent(Constrained.class)) {
            return validationErrors;
        }
        // We get all declared fields and check all annotations
        for (var field : object.getClass().getDeclaredFields()) {
            field.setAccessible(true);
            fieldName = field.getName();
            path = rootPath + fieldName;
            // If object is custom class and not constrained, we skip it
            if (!isConstrained(object)) {
                continue;
            }
            // Get all field's annotations
            var annotations = field.getAnnotatedType().getDeclaredAnnotations();
            // And check if it's valid for all of them
            for (var annotation : annotations) {
                checkAnnotation(field.get(object), annotation);
            }
            // Special case if we have list
            if (field.get(object) instanceof List) {
                path = rootPath + fieldName;
                // Recursive check
                checkList(field.get(object), (AnnotatedParameterizedType) field.getAnnotatedType());
            }
        }
        return validationErrors;
    }

    /**
     * Checks if value firs given annotation
     *
     * @param object     Object to check
     * @param annotation Presented annotation
     * @throws Exception If annotations is presented by invalid type
     */
    private void checkAnnotation(Object object, Annotation annotation) throws Exception {
        // Check all presented types of annotations

        if (annotation instanceof NotNull && isNull(object)) {
            catchError("Must not be null", path, null);
        } else if (annotation instanceof Positive && !isPositive(object)) {
            catchError("Must be positive", path, object);
        } else if (annotation instanceof Negative && !isNegative(object)) {
            catchError("Must be negative", path, object);
        } else if (annotation instanceof NotBlank && isBlank(object)) {
            catchError("Must not be blank", path, object);
        } else if (annotation instanceof NotEmpty && isEmpty(object)) {
            catchError("Must not be empty", path, object);
        } else if (annotation instanceof Size && !hasProperSize(object, (Size) annotation)) {
            catchError(String.format("Must have size in range [%d, %d]",
                    ((Size) annotation).min(), ((Size) annotation).max()),
                    path, object);
        } else if (annotation instanceof InRange && !isInRange(object, (InRange) annotation)) {
            catchError(String.format("Must be in range [%d, %d]",
                    ((InRange) annotation).min(), ((InRange) annotation).max()),
                    path, object);
        } else if (annotation instanceof AnyOf && !isAnyOf(object, (AnyOf) annotation)) {
            catchError("Has to be contained in array", path, object);
        }
    }

    /**
     * Checks elements of list recursive
     *
     * @param object        List to check
     * @param annotatedType Type to check annotations on it
     * @throws Exception If annotations is presented by invalid type
     */
    private void checkList(Object object, AnnotatedParameterizedType annotatedType) throws Exception {
        // Get presented annotations inside
        var annotations = annotatedType
                .getAnnotatedActualTypeArguments()[0]
                .getAnnotations();
        // Indicator for path
        int i = 0;
        for (var obj : (List<?>) object) {
            path += String.format("[%d]", i);
            // If elements of list are other lists, we go recursive into them
            if (obj instanceof List) {
                checkList(obj,
                        (AnnotatedParameterizedType) annotatedType.getAnnotatedActualTypeArguments()[0]);
            } else { // Otherwise we check all annotations for every element
                for (Annotation annotation : annotations) {
                    checkAnnotation(obj, annotation);
                }
                // If object is also constrained, we go recursive to check its fields
                if (isConstrained(obj)) {
                    ValidatorImpl validator = new ValidatorImpl(path + ".");
                    validationErrors.addAll(validator.validate(obj));
                }
            }
            path = path.substring(0, path.length() - 3);
            ++i;
        }
    }

    /**
     * Checks if object has Constrained annotation or is default from java packages
     *
     * @param object Object to check
     * @return True if object is Constrained or from packages
     */
    private boolean isConstrained(Object object) {
        if (isNull(object)) {
            return false;
        }
        if (Utils.isJavaLang(object) || Utils.isJavaUtil(object)) {
            return true;
        }
        return object.getClass().isAnnotationPresent(Constrained.class);
    }

    /**
     * Checks if object equals null
     *
     * @param object Object to check
     * @return True if object is null
     */
    private boolean isNull(Object object) {
        return object == null;
    }

    /**
     * Checks if object's value is positive
     *
     * @param object Object to check its value
     * @return True if object's value is positive
     * @throws Exception If object is of inappropriate type
     */
    private boolean isPositive(Object object) throws Exception {
        if (isNull(object)) {
            return true;
        }
        if (object instanceof Byte) {
            return (Byte) object > 0;
        } else if (object instanceof Short) {
            return (Short) object > 0;
        } else if (object instanceof Integer) {
            return (Integer) object > 0;
        } else if (object instanceof Long) {
            return (Long) object > 0;
        }
        throw new Exception();
    }

    /**
     * Checks if object's value is negative
     *
     * @param object Object to check its value
     * @return True if object's value is negative
     * @throws Exception If object is of inappropriate type
     */
    private boolean isNegative(Object object) throws Exception {
        if (isNull(object)) {
            return true;
        }
        if (object instanceof Byte) {
            return (Byte) object < 0;
        } else if (object instanceof Short) {
            return (Short) object < 0;
        } else if (object instanceof Integer) {
            return (Integer) object < 0;
        } else if (object instanceof Long) {
            return (Long) object < 0;
        }
        throw new Exception();
    }

    /**
     * Checks if string is blank
     *
     * @param object Object to check its value
     * @return True if string is blank
     * @throws Exception If object is not an instance of String
     */
    private boolean isBlank(Object object) throws Exception {
        if (isNull(object)) {
            return false;
        }
        if (object instanceof String) {
            return ((String) object).isBlank();
        }
        throw new Exception();
    }

    /**
     * Checks if object is empty
     *
     * @param object Object to check its capacity
     * @return True if object is empty
     * @throws Exception If object is of inappropriate type
     */
    private boolean isEmpty(Object object) throws Exception {
        if (isNull(object)) {
            return false;
        }
        if (object instanceof String) {
            return ((String) object).isEmpty();
        }
        if (object instanceof List) {
            return ((List<?>) object).isEmpty();
        }
        if (object instanceof Set) {
            return ((Set<?>) object).isEmpty();
        }
        if (object instanceof Map) {
            return ((Map<?, ?>) object).isEmpty();
        }
        throw new Exception();
    }

    /**
     * Checks if object has proper size
     *
     * @param object Object to check its size
     * @param size   Size-annotation to get values from
     * @return True if object has proper size
     * @throws Exception If object is of inappropriate type
     */
    private boolean hasProperSize(Object object, Size size) throws Exception {
        if (isNull(object)) {
            return true;
        }
        if (object instanceof List) {
            var listSize = ((List<?>) object).size();
            return listSize >= size.min() && listSize <= size.max();
        }
        if (object instanceof Set) {
            var setSize = ((Set<?>) object).size();
            return setSize >= size.min() && setSize <= size.max();
        }
        if (object instanceof Map) {
            var mapSize = ((Map<?, ?>) object).size();
            return mapSize >= size.min() && mapSize <= size.max();
        }
        if (object instanceof String) {
            var length = ((String) object).length();
            return length >= size.min() && length <= size.max();
        }
        throw new Exception();
    }

    /**
     * Checks if object's value is in given range
     *
     * @param object Object to check its value
     * @param range  InRange-annotation to get values from
     * @return True if object is in given range
     * @throws Exception If object is of inappropriate type
     */
    private boolean isInRange(Object object, InRange range) throws Exception {
        if (isNull(object)) {
            return true;
        }
        if (object instanceof Byte) {
            byte value = (Byte) object;
            return value >= range.min() && value <= range.max();
        }
        if (object instanceof Short) {
            short value = (Short) object;
            return value >= range.min() && value <= range.max();
        }
        if (object instanceof Long) {
            long value = (Long) object;
            return value >= range.min() && value <= range.max();
        }
        if (object instanceof Integer) {
            int value = (Integer) object;
            return value >= range.min() && value <= range.max();
        }
        throw new Exception();
    }

    /**
     * Checks if object is contained in array
     *
     * @param object Object to check its value
     * @param anyOf  AnyOf-annotation to get array of values from
     * @return True if object is contained in array
     * @throws Exception If object is of inappropriate type
     */
    private boolean isAnyOf(Object object, AnyOf anyOf) throws Exception {
        if (isNull(object)) {
            return true;
        }
        if (object instanceof String) {
            return Arrays.asList(anyOf.value()).contains(object);
        }
        throw new Exception();
    }

    /**
     * Adds error to set of errors
     *
     * @param message     Message of error
     * @param path        Path of error
     * @param failedValue Value of error
     */
    private void catchError(String message, String path, Object failedValue) {
        validationErrors.add(new ValidationErrorImpl(message, path, failedValue));
    }
}
