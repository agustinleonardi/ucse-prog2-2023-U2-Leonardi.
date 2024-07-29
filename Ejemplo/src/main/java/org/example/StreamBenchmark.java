package org.example;

import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
@State(Scope.Benchmark)
public class StreamBenchmark {
    private List<Integer> numbers;

    @Setup(Level.Trial)
    public void setup() {
        int size = 10000000;
        Random random = new Random();
        numbers = new ArrayList<>();

        for (int i = 0; i < size; i++) {
            int randomNumber = 1 + random.nextInt(50000 - 1); // Genera entre 0 (inclusive) y 49999 (exclusive), luego agrega 1
            numbers.add(randomNumber);
        }
    }

    @Benchmark
    public void concurrencia() {
        List<Integer> resultSecuencial = numbers.stream()
                .filter(n -> n % 2 == 0)
                .collect(Collectors.toList());
    }

    @Benchmark
    public void paralelismo() {
        List<Integer> resultParalelo = numbers.parallelStream()
                .filter(n -> n % 2 == 0)
                .collect(Collectors.toList());
    }

    public static void main(String[] args) throws RunnerException {
        Options opt = new OptionsBuilder()
                .include(StreamBenchmark.class.getSimpleName())
                .warmupIterations(5)
                .measurementIterations(10)
                .forks(1)
                .build();

        new Runner(opt).run();
    }
}
