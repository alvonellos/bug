package com.alvonellos.bug.error.exceptions;


public abstract class WebsiteAPIException extends Exception {
    protected WebsiteAPIException(String message) {
        super(message);
    }

    protected WebsiteAPIException(String message, Exception cause) {
        super(message, cause);
    }
}
