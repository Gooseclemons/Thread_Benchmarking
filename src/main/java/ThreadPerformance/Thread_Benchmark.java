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

    static WikiSystem system;

    public static void main(String[] args) throws RunnerException {
        Options opt = new OptionsBuilder()
                .include(Benchmark.class.getSimpleName())
                .forks(2)
                .result("BenchmarkResults.json")
                .resultFormat(ResultFormatType.JSON)
                .build();
        new Runner(opt).run();
    }

    @Benchmark
    public void runIt() {
        system = new WikiSystem(10, 0.1);
        system.runClients();
    }

}
