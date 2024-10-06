package com.estifie.expensetracker.response;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResponse<T> {
    private String status;
    private String message;
    private T data;

    public ApiResponse() {
    }

    public T getData() {
        return data;
    }

    public String getMessage() {
        return message;
    }

    public String getStatus() {
        return status;
    }

    public static <T> ApiResponse<T> success() {
        ApiResponse<T> response = new ApiResponse<>();
        response.status = "success";
        return response;
    }

    public static <T> ApiResponse<T> error() {
        ApiResponse<T> response = new ApiResponse<>();
        response.status = "error";
        return response;
    }

    public ApiResponse<T> message(String message) {
        this.message = message;
        return this;
    }

    public ApiResponse<T> data(T data) {
        this.data = data;
        return this;
    }

    public ApiResponse<T> status(String status) {
        this.status = status;
        return this;
    }
}
