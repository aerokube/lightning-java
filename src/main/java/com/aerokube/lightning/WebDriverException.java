package com.aerokube.lightning;

import com.aerokube.lightning.model.ErrorCode;

import javax.annotation.Nonnull;

public class WebDriverException extends RuntimeException {

    private final ErrorCode errorCode;

    public WebDriverException(@Nonnull Throwable cause) {
        super(cause);
        this.errorCode = ErrorCode.UNKNOWN_ERROR;
    }

    public WebDriverException(@Nonnull String message) {
        super(message);
        this.errorCode = ErrorCode.UNKNOWN_ERROR;
    }

    public WebDriverException(@Nonnull String message, @Nonnull Throwable cause) {
        super(message, cause);
        this.errorCode = ErrorCode.UNKNOWN_ERROR;
    }

    public WebDriverException(@Nonnull String message, @Nonnull ErrorCode errorCode) {
        super(message);
        this.errorCode = errorCode;
    }

    public ErrorCode getErrorCode() {
        return errorCode;
    }
}
