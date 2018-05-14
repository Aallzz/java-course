package ru.ifmo.rain.Petrovski.concurrent;

import info.kgeorgiy.java.advanced.mapper.ParallelMapper;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Task {

    private final ParallelMapper mapper;

    Task() {
        mapper = null;
    }

    Task(ParallelMapper mapper) {
        this.mapper = mapper;
    }

    private <T> Stream<List<? extends T>> split(int count, List<? extends T> list) {
        int blockSize = Math.max(1, list.size() / count);
        int left = list.size() - blockSize * count;
        int curSize = blockSize;
        List<List<? extends T>> splitList = new ArrayList<>();
        for (int position = 0; position < list.size(); ) {
            if (left > 0) {
                left--;
                curSize++;
            }
            int endPosition = Math.min(position + curSize, list.size());
            splitList.add(list.subList(position, endPosition));
            position = endPosition;

        }
        return splitList.stream();
    }


    public <T, R> R process(int threads, List<? extends T> list,
                            final Function<Stream<? extends T>, R> mapFunction,
                            final Function<? super Stream<R>, R> reduceFunction) throws InterruptedException {

        List<R> resList;
        Stream<List<? extends T>> parts = split(threads, list);
        if (mapper != null) {
            resList = mapper.map(mapFunction, parts.map(List::stream).collect(Collectors.toList()));
        } else {
            resList = new ArrayList<>();
            List<Worker<T, R>> workerList = parts
                    .map(v -> new Worker<T, R>(v, mapFunction))
                    .collect(Collectors.toList());
            for (Worker<T, R> worker : workerList) {
                resList.add(worker.collectResult());
            }
        }
        return reduceFunction.apply(resList.stream());
    }

}
