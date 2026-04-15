package com.walletcore.common.response;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record ApiResponse<T> (
        boolean status,
        int code,
        T data,
        ApiError error,
        ApiMeta meta
) {
    public static <T> ApiResponse<T> success(T data) {
        return new ApiResponse<>(true, 200, data, null, null);
    }
    public static <T> ApiResponse<T> success(T data, ApiMeta meta) {
        return new ApiResponse<>(true, 200, data, null, meta);
    }
    public static <T> ApiResponse<T> failure(int code, ApiError error) {
        return new ApiResponse<>(false, code, null, error, null);
    }
    public static <T> ApiResponse<T> failure(ApiError error, ApiMeta meta) {
        return new ApiResponse<>(false, 400, null, error, meta);
    }
}
