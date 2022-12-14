package com.hawwaru.microtrader;

import com.hawwaru.microtrader.backtesting.DataCollector;
import io.micronaut.configuration.picocli.PicocliRunner;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

@Command(name = "trader",
        mixinStandardHelpOptions = true
        , subcommands = {
        DataCollector.class
})
public class MicroTraderApplication implements Runnable {

    @Option(names = {"-v", "--verbose"}, description = "...")
    boolean verbose;

    public static void main(String[] args) {
        PicocliRunner.run(MicroTraderApplication.class, args);
    }

    public void run() {
        // business logic here
        if (verbose) {
            System.out.println("Hi!");
        }
    }
}
