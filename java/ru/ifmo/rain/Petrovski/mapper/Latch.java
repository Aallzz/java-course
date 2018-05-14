package ru.ifmo.rain.Petrovski.mapper;

public class Latch {
    private volatile int counter;

    public Latch(int counter) {
        this.counter = counter;
    }

    public synchronized void await() throws InterruptedException {
        while (counter > 0) {
            this.wait();
        }
    }

    public synchronized void inc() {
        counter--;
        if (counter == 0) {
            this.notifyAll();
        }
    }
}