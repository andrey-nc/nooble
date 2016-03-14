package com.ghost.lucene;

public class CreateDirectoryException extends Exception {

    public CreateDirectoryException(String message) {
        super(message);
    }

    public CreateDirectoryException() {
        super();
    }

    public CreateDirectoryException(String message, Throwable cause) {
        super(message, cause);
    }

    public CreateDirectoryException(Throwable cause) {
        super(cause);
    }

    protected CreateDirectoryException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
