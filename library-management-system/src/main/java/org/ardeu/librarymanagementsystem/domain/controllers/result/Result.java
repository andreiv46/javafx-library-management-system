package org.ardeu.librarymanagementsystem.domain.controllers.result;

import java.util.function.Function;

/**
 * A generic class that represents the result of an operation, which can either be successful or a failure.
 * The result can carry additional data in case of success or an error message in case of failure.
 *
 * @param <T> the type of data that is carried in the result if the operation is successful
 */
public class Result<T> {
    private final boolean success;
    private final String errorMessage;
    private final T data;

    /**
     * Private constructor for creating a Result instance. It is used internally by the static methods.
     *
     * @param success whether the operation was successful or not
     * @param errorMessage the error message if the operation failed, or null if successful
     * @param data the data associated with the result if the operation was successful, or null if failed
     */
    private Result(boolean success, String errorMessage, T data) {
        this.success = success;
        this.errorMessage = errorMessage;
        this.data = data;
    }

    /**
     * Creates a successful Result with no data.
     *
     * @param <T> the type of data that is carried in the result
     * @return a Result indicating success with no data
     */
    public static <T> Result<T> success() {
        return new Result<>(true, null, null);
    }

    /**
     * Creates a successful Result with the provided data.
     *
     * @param <T> the type of data that is carried in the result
     * @param data the data associated with the successful result
     * @return a Result indicating success with the provided data
     */
    public static <T> Result<T> success(T data) {
        return new Result<>(true, null, data);
    }

    /**
     * Creates a failed Result with the provided error message.
     *
     * @param <T> the type of data that is carried in the result
     * @param errorMessage the error message associated with the failure
     * @return a Result indicating failure with the provided error message
     */
    public static <T> Result<T> failure(String errorMessage) {
        return new Result<>(false, errorMessage, null);
    }

    /**
     * Checks if the operation was successful.
     *
     * @return true if the operation was successful, false otherwise
     */
    public boolean isSuccess() {
        return success;
    }

    /**
     * Gets the error message associated with the failure, if any.
     *
     * @return the error message, or null if the operation was successful
     */
    public String getErrorMessage() {
        return errorMessage;
    }

    /**
     * Gets the data associated with the result, if any.
     *
     * @return the data, or null if the operation failed
     */
    public T getData() {
        return data;
    }

    /**
     * Transforms the data of a successful Result using the provided mapping function.
     * If the result is a failure, the failure is propagated.
     *
     * @param <R> the type of the transformed data
     * @param mapper the function to apply to the current data
     * @return a new Result with the transformed data, or the same failure if the original result was a failure
     */
    public <R> Result<R> map(Function<T, R> mapper) {
        if (isSuccess()) {
            return Result.success(mapper.apply(data));
        } else {
            return Result.failure(errorMessage);
        }
    }
}

