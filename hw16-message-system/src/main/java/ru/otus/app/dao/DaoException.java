package ru.otus.app.dao;

public class DaoException extends RuntimeException {

    public DaoException() {
        super();
    }

    public DaoException(final Throwable cause) {
        super(cause);
    }
}
