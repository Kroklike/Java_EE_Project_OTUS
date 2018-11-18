package ru.otus.akn.project.gwt.shared.exception;

public class WrongCredentialsException extends RuntimeException {

    public WrongCredentialsException() {
    }

    public WrongCredentialsException(String message) {
        super(message);
    }

}
