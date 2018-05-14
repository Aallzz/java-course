package ru.ifmo.rain.Petrovski.concurrent;

import info.kgeorgiy.java.advanced.concurrent.ListIP;
import info.kgeorgiy.java.advanced.mapper.ParallelMapper;

import java.util.Comparator;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class IterativeParallelism implements ListIP {

    private final Task task;

    public IterativeParallelism() {
        task = new Task();
    }

    public IterativeParallelism(ParallelMapper mapper) {
        task = new Task(mapper);
    }

    @Override
    public String join(int threads, List<?> values) throws InterruptedException {
        Function<Stream<?>, String> mapFunction = s -> s.map(Object::toString).collect(Collectors.joining());
        return task.process(threads, values, mapFunction, mapFunction);
    }

    @Override
    public <T> List<T> filter(int threads, List<? extends T> values, Predicate<? super T> predicate) throws InterruptedException {
        Function<Stream<? extends T>, List<T>> mapFunction = s -> s.filter(predicate).collect(Collectors.toList());
        Function<Stream<List<T>>, List<T>> reduceFunction = s -> s.flatMap(List::stream).collect(Collectors.toList());
        return task.process(threads, values, mapFunction, reduceFunction);
    }

    @Override
    public <T, U> List<U> map(int threads, List<? extends T> values, Function<? super T, ? extends U> f) throws InterruptedException {
        Function<Stream<? extends T>, List<U>> mapFunction = s -> s.map(f).collect(Collectors.toList());
        Function<Stream<List<U>>, List<U>> reduceFunction = s -> s.flatMap(List::stream).collect(Collectors.toList());
        return task.process(threads, values, mapFunction, reduceFunction);
    }

    @Override
    public <T> T maximum(int threads, List<? extends T> values, Comparator<? super T> comparator) throws InterruptedException {
        Function<Stream<? extends T>, T> function = s -> s.max(comparator).orElse(null);
        return task.process(threads, values, function, function);
    }

    @Override
    public <T> T minimum(int threads, List<? extends T> values, Comparator<? super T> comparator) throws InterruptedException {
        return maximum(threads, values, comparator.reversed());
    }

    @Override
    public <T> boolean all(int threads, List<? extends T> values, Predicate<? super T> predicate) throws InterruptedException {
        Function<Stream<? extends T>, Boolean> mapFunction = s -> s.allMatch(predicate);
        Function<Stream<Boolean>, Boolean> reduceFunction = s -> s.allMatch(Boolean::booleanValue);
        return task.process(threads, values, mapFunction, reduceFunction);
    }

    @Override
    public <T> boolean any(int threads, List<? extends T> values, Predicate<? super T> predicate) throws InterruptedException {
        Function<Stream<? extends T>, Boolean> mapFunction = s -> s.anyMatch(predicate);
        Function<Stream<Boolean>, Boolean> reduceFunction = s -> s.anyMatch(Boolean::booleanValue);
        return task.process(threads, values, mapFunction, reduceFunction);
    }
}

