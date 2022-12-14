package com.hawwaru.microtrader.backtesting;

import me.tongfei.progressbar.ProgressBar;
import me.tongfei.progressbar.ProgressBarBuilder;

import java.io.BufferedInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;

public class HistoricalDataDownloadJob implements Runnable {

    public static final Integer MB = 1048576;

    private String fileName;
    private String fileUrl;
    private String destinationFolder;
    private String coinPair;


    public HistoricalDataDownloadJob(String fileName, String fileUrl, String destinationFolder, String coinPair) {
        this.fileName = fileName;
        this.fileUrl = fileUrl;
        this.destinationFolder = destinationFolder;
        this.coinPair = coinPair;
    }

    @Override
    public void run() {
        try {
            Files.createDirectories(Paths.get(destinationFolder + "/" + coinPair));
        } catch (IOException e) {
            System.out.println("Could not create destination directories. Skipping download of %s".formatted(fileName));
        }

        var filePath = String.join("/", destinationFolder, coinPair, fileName);

        try (ProgressBar progressBar = new ProgressBarBuilder()
                .setUnit("MB", MB)
                .setInitialMax(getFileSize(new URL(fileUrl)))
                .setTaskName(fileName)
                .showSpeed()
                .build();
             BufferedInputStream in = new BufferedInputStream(new URL(fileUrl).openStream());
             FileOutputStream fileOutputStream = new FileOutputStream(filePath)) {

            byte[] dataBuffer = new byte[MB];
            int bytesRead;
            while ((bytesRead = in.read(dataBuffer, 0, MB)) != -1) {
                fileOutputStream.write(dataBuffer, 0, bytesRead);
                progressBar.stepBy(bytesRead);
            }

        } catch (IOException e) {
            System.out.println("Error occurred during download of " + fileName);
            System.out.println(e.getLocalizedMessage());
        }
    }


    public long getFileSize(URL url) {
        HttpURLConnection conn = null;
        try {
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("HEAD");
            return conn.getContentLengthLong();
        } catch (IOException e) {
            System.out.println("Could not open connection to url");
            e.getLocalizedMessage();
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }
        return -1;
    }
}
