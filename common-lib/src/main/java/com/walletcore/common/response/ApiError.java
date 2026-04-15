package com.walletcore.common.response;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record ApiError(
        String code,      // e.g. "VALIDATION_FAILED"
        String message,   // safe, user-facing
        Object details   // optional extra info
) {

    public static ApiError of(String code, String message) {
        return new ApiError(code, message, null);
    }
    public static ApiError of(String code, String message, Object details) {
        return new ApiError(code, message, details);
    }
}
