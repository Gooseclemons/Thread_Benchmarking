package ThreadPerformance;

import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.infra.Blackhole;
import org.openjdk.jmh.results.format.ResultFormatType;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.util.concurrent.TimeUnit;

@State(Scope.Benchmark)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@BenchmarkMode(Mode.AverageTime)
public class Thread_Benchmark {

    private WikiSystem system;

    @Param({"10"})
    private int num_clients;

    @Param({"0.1"})
    private double writeChance;

    public static void main(String[] args) throws RunnerException {
        Options opt = new OptionsBuilder()
                .include(Benchmark.class.getSimpleName())
                .forks(1)
//                .warmupIterations(2)
//                .measurementIterations(2)
                .result("BenchmarkResults.json")
                .resultFormat(ResultFormatType.JSON)
                .build();
        new Runner(opt).run();
    }

    @Benchmark
    public void runCustomRWL() {
        system = new WikiSystem(num_clients, writeChance);
        system.runClients();
    }

    @Setup(Level.Iteration)
    public void setupBenchmark() {
        //System.out.println("Iter");
        system = new WikiSystem(num_clients, writeChance);
    }

}
