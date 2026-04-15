package com.walletcore.common.response;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record ApiMeta(
        String traceId,
        String timestamp // ISO-8601 string if you want to set it
) {}
