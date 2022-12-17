package com.hawwaru.microtrader.backtesting.hoarder;

import com.binance.api.client.domain.market.CandlestickInterval;
import com.hawwaru.microtrader.backtesting.DataType;

import java.time.LocalDate;
import java.time.Period;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.hawwaru.microtrader.backtesting.DataType.AGG_TRADES;
import static com.hawwaru.microtrader.backtesting.DataType.TRADES;

public class FileNameGenerator {
    private static final String ARCHIVE_TYPE = ".zip";

    private final LocalDate startDate;
    private final LocalDate endDate;
    private final String coinPair;
    private final DataType dataType;
    private final CandlestickInterval candlestickInterval;

    public FileNameGenerator(LocalDate startDate, LocalDate endDate, String coinPair, DataType dataType, CandlestickInterval candlestickInterval) {
        this.startDate = startDate;
        this.endDate = endDate;
        this.coinPair = coinPair;
        this.dataType = dataType;
        this.candlestickInterval = candlestickInterval;
    }

    public List<String> generateFileNames() {
        if (startsAndEndsInSameMonth() && isSpanOfWholeMonth()) {
            return startDate.datesUntil(endDate.plusDays(1), Period.ofMonths(1))
                    .map(date -> buildMonthlyFileName(date.getYear(), date.getMonth().getValue()))
                    .toList();
        }

        if (startsAndEndsInSameMonth() && !isSpanOfWholeMonth()) {
            return startDate.datesUntil(endDate.plusDays(1))
                    .map(date -> buildDailyFileName(date.getYear(), date.getMonth().getValue(), date.getDayOfMonth()))
                    .toList();
        }

        var nextMonthFirstDay = startDate.plusMonths(1).with(TemporalAdjusters.firstDayOfMonth());

        List<String> firstMonthData;
        if (startDate.getDayOfMonth() == 1) {
            firstMonthData = Collections.singletonList(buildMonthlyFileName(startDate.getYear(), startDate.getMonth().getValue()));
        } else {
            firstMonthData = startDate.datesUntil(nextMonthFirstDay)
                    .map(date -> buildDailyFileName(date.getYear(), date.getMonth().getValue(), date.getDayOfMonth()))
                    .toList();
        }

        var endDateMonthFirstDay = endDate.with(TemporalAdjusters.firstDayOfMonth());

        List<String> lastMonthData;
        if (endsOnLastDayOfMonth(endDate) && !isToday(endDate)) { //today's data is not available on the same day
            lastMonthData = Collections.singletonList(buildMonthlyFileName(endDate.getYear(), endDate.getMonth().getValue()));
        } else {
            lastMonthData = endDateMonthFirstDay.datesUntil(endDate.plusDays(1))
                    .map(date -> buildDailyFileName(date.getYear(), date.getMonth().getValue(), date.getDayOfMonth()))
                    .toList();
        }

        var fullMonthsBetweenStartAndEnd = nextMonthFirstDay.datesUntil(endDateMonthFirstDay, Period.ofMonths(1))
                .map(date -> buildMonthlyFileName(date.getYear(), date.getMonth().getValue()))
                .toList();

        List<String> resultFileNames = new ArrayList<>();
        resultFileNames.addAll(firstMonthData);
        resultFileNames.addAll(fullMonthsBetweenStartAndEnd);
        resultFileNames.addAll(lastMonthData);

        return resultFileNames;
    }

    private boolean isToday(LocalDate date) {
        return date.isBefore(LocalDate.now()) && date.datesUntil(LocalDate.now(), Period.ofDays(1)).findAny().isEmpty();
    }

    private boolean endsOnLastDayOfMonth(LocalDate date) {
        return date.getDayOfMonth() == date.with(TemporalAdjusters.lastDayOfMonth()).getDayOfMonth();
    }

    private String buildDailyFileName(Integer year, Integer month, Integer day) {
        return switch (dataType) {
            case AGG_TRADES ->
                    String.join("-", coinPair, AGG_TRADES.getDataTypeId(), year.toString(), month.toString(), day.toString()) + ARCHIVE_TYPE;
            case KLINES ->
                    String.join("-", coinPair, candlestickInterval.getIntervalId(), year.toString(), month.toString(), day.toString()) + ARCHIVE_TYPE;
            case TRADES ->
                    String.join("-", coinPair, TRADES.getDataTypeId(), year.toString(), month.toString(), day.toString()) + ARCHIVE_TYPE;
        };
    }

    private String buildMonthlyFileName(Integer year, Integer month) {
        return switch (dataType) {
            case AGG_TRADES ->
                    String.join("-", coinPair, AGG_TRADES.getDataTypeId(), year.toString(), month.toString()) + ARCHIVE_TYPE;
            case KLINES ->
                    String.join("-", coinPair, candlestickInterval.getIntervalId(), year.toString(), month.toString()) + ARCHIVE_TYPE;
            case TRADES ->
                    String.join("-", coinPair, TRADES.getDataTypeId(), year.toString(), month.toString()) + ARCHIVE_TYPE;
        };
    }

    private boolean startsAndEndsInSameMonth() {
        return startDate.getMonth() == endDate.getMonth();
    }

    private boolean isSpanOfWholeMonth() {
        return (startDate.with(TemporalAdjusters.firstDayOfMonth()) == startDate)
                && endDate.with(TemporalAdjusters.lastDayOfMonth()) == endDate;
    }
}
