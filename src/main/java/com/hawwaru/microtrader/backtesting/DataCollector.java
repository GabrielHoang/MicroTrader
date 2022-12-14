package com.hawwaru.microtrader.backtesting;

import com.binance.connector.client.SpotClient;
import com.hawwaru.microtrader.converter.LocalDateConverter;
import jakarta.inject.Inject;

import java.time.LocalDate;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static picocli.CommandLine.Command;
import static picocli.CommandLine.Option;

@Command(name = "collect",
        description = """
                Collects data for further use. Date range format: yyyy-MM-dd
                """)
public class DataCollector implements Runnable {

    public static final String DATA_FOLDER = "historical-data";
    @Option(names = {"-s", "--start-date"}, description = "Start date for data collection",
            converter = LocalDateConverter.class)
    private LocalDate startDate;
    @Option(names = {"-e", "--end-date"}, description = "End date for data collection",
            converter = LocalDateConverter.class)
    private LocalDate endDate;
    @Option(names = {"-c", "--coin"}, description = "Coin to build a pair with base coin (ex. USDT)")
    private String coin;
    @Inject
    private SpotClient apiClient;

    @Override
    public void run() {
        if (startDate.isAfter(endDate)) {
            throw new IllegalArgumentException("Start date cannot be after end date");
        }

        var url = "https://data.binance.vision/data/spot/monthly/aggTrades/BTCUSDT/BTCUSDT-aggTrades-2022-11.zip";
        var url2 = "https://data.binance.vision/data/spot/monthly/aggTrades/BTCUSDT/BTCUSDT-aggTrades-2022-10.zip";
        var url3 = "https://data.binance.vision/data/spot/monthly/aggTrades/BTCUSDT/BTCUSDT-aggTrades-2022-09.zip";

        ExecutorService executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
        HistoricalDataDownloadJob job = new HistoricalDataDownloadJob("test", url, DATA_FOLDER, "BTCUSDT");
        HistoricalDataDownloadJob job2 = new HistoricalDataDownloadJob("test2", url2, DATA_FOLDER, "BTCUSDT");
        HistoricalDataDownloadJob job3 = new HistoricalDataDownloadJob("test3", url3, DATA_FOLDER, "BTCUSDT");
        executor.submit(job);
        executor.submit(job2);
        executor.submit(job3);


    }


}
