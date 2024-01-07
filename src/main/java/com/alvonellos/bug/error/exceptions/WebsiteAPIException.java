package com.alvonellos.bug.error.exceptions;


public abstract class WebsiteAPIException extends Exception {
    public WebsiteAPIException(String message) {
        super(message);
    }

    public WebsiteAPIException(String message, Exception cause) {
        super(message, cause);
    }
}
