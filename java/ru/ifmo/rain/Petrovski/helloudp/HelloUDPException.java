package ru.ifmo.rain.Petrovski.helloudp;

class HelloUDPException extends Exception {

    public HelloUDPException() {

    }

    public HelloUDPException(final String message) {
        super(message);
    }

    public HelloUDPException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public HelloUDPException(final Throwable cause) {
        super(cause);
    }

}