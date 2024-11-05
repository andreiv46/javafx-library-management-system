package org.ardeu.librarymanagementsystem.domain.controllers.result;

import java.util.function.Function;

public class Result<T> {
    private final boolean success;
    private final String errorMessage;
    private final T data;

    private Result(boolean success, String errorMessage, T data) {
        this.success = success;
        this.errorMessage = errorMessage;
        this.data = data;
    }

    public static <T> Result<T> success() {
        return new Result<>(true, null, null);
    }

    public static <T> Result<T> success(T data) {
        return new Result<>(true, null, data);
    }

    public static <T> Result<T> failure(String errorMessage) {
        return new Result<>(false, errorMessage, null);
    }

    public boolean isSuccess() {
        return success;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public T getData() {
        return data;
    }

    public <R> Result<R> map(Function<T, R> mapper) {
        if (isSuccess()) {
            return Result.success(mapper.apply(data));
        } else {
            return Result.failure(errorMessage);
        }
    }
}
