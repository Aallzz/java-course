package ru.ifmo.rain.Petrovski.concurrent;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Stream;

public class Worker<T, R> {

    private R result;
    private Thread thread;

    Worker(List<? extends T> list, Function<? super Stream<? extends T>, ? extends R> mapFunction) {
        thread = new Thread(() -> result = mapFunction.apply(list.stream()));
        thread.start();
    }

    R collectResult() throws InterruptedException {
        thread.join();
        return result;
    }
}
