package com.aerokube.lightning;

public class WebDriverException extends RuntimeException {

    public WebDriverException(Exception e) {
        super(e);
    }

    public WebDriverException(String error) {
        super(error);
    }
}
