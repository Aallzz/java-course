package ru.ifmo.rain.Petrovski.mapper;

import info.kgeorgiy.java.advanced.mapper.ParallelMapper;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ParallelMapperImpl implements ParallelMapper {

    private final Queue<Runnable> queue = new ArrayDeque<>();
    private final List<Thread> threads;

    public ParallelMapperImpl(int threadAmount) {
        Runnable worker = () -> {
            Runnable f;
            try {
                while (true) {
                    synchronized (this) {
                        while (queue.isEmpty()) {
                            this.wait();
                        }

                        f = queue.poll();
                    }
                    f.run();
                }
            } catch (InterruptedException e) {
            }
        };

        threads = Stream.generate(() -> new Thread(worker))
                .limit(threadAmount)
                .collect(Collectors.toList());
        threads.forEach(Thread::start);
    }

    @Override
    public <T, R> List<R> map(Function<? super T, ? extends R> f, List<? extends T> args) throws InterruptedException {

        Latch latch = new Latch(args.size());
        List<R> results = new ArrayList<>(Collections.nCopies(args.size(), null));
        for (int i = 0; i < args.size(); i++) {
            int finalI = i;
            add(() -> {
                results.set(finalI, f.apply(args.get(finalI)));
                latch.inc();
            });
        }
        latch.await();
        return results;
    }

    private synchronized void add(Runnable r) {
        queue.add(r);
        this.notify();
    }

    @Override
    public void close() {
        threads.forEach(Thread::interrupt);
        threads.forEach(t -> {
            try {
                t.join();
            } catch (InterruptedException e) {
            }
        });
    }
}
