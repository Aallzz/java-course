package ru.ifmo.rain.Petrovski.walk;

public class RecursiveWalkerException extends Exception {
    RecursiveWalkerException(String msg) {
        super(msg);
    }

    RecursiveWalkerException(String msg, Exception e) {
        super(msg, e);
    }
}