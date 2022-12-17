package com.hawwaru.microtrader.backtesting.hoarder

import com.binance.api.client.domain.market.CandlestickInterval
import spock.lang.Specification

import java.time.Month

import static com.hawwaru.microtrader.backtesting.DataType.*
import static java.time.LocalDate.of

class FileNameGeneratorSpec extends Specification {

    def "period #reason"() {
        given:
        var coinPair = "BTCUSDT"
        var interval = CandlestickInterval.ONE_MINUTE

        def generator = new FileNameGenerator(startDate, endDate, coinPair, dataType, interval)

        when:
        var result = generator.generateFileNames()

        then:
        result == expected

        where:
        startDate                   | endDate                      | dataType   | expected                                                                                                      | reason
        of(2022, Month.JANUARY, 1)  | of(2022, Month.JANUARY, 31)  | AGG_TRADES | List.of("BTCUSDT-aggTrades-2022-1.zip")                                                                       | "start and end span whole month"
        of(2022, Month.JANUARY, 1)  | of(2022, Month.JANUARY, 3)   | AGG_TRADES | List.of("BTCUSDT-aggTrades-2022-1-1.zip", "BTCUSDT-aggTrades-2022-1-2.zip", "BTCUSDT-aggTrades-2022-1-3.zip") | "ends within 3 days of start"
        of(2022, Month.JANUARY, 1)  | of(2022, Month.JANUARY, 31)  | KLINES     | List.of("BTCUSDT-1m-2022-1.zip")                                                                              | "start and end span whole month"
        of(2022, Month.JANUARY, 1)  | of(2022, Month.JANUARY, 3)   | KLINES     | List.of("BTCUSDT-1m-2022-1-1.zip", "BTCUSDT-1m-2022-1-2.zip", "BTCUSDT-1m-2022-1-3.zip")                      | "ends within 3 days of start"
        of(2022, Month.JANUARY, 1)  | of(2022, Month.JANUARY, 31)  | TRADES     | List.of("BTCUSDT-trades-2022-1.zip")                                                                          | "start and end span whole month"
        of(2022, Month.JANUARY, 1)  | of(2022, Month.JANUARY, 3)   | TRADES     | List.of("BTCUSDT-trades-2022-1-1.zip", "BTCUSDT-trades-2022-1-2.zip", "BTCUSDT-trades-2022-1-3.zip")          | "ends within 3 days of start"
        of(2022, Month.JANUARY, 31) | of(2022, Month.FEBRUARY, 1)  | AGG_TRADES | List.of("BTCUSDT-aggTrades-2022-1-31.zip", "BTCUSDT-aggTrades-2022-2-1.zip")                                  | "start and end on months border"
        of(2022, Month.JANUARY, 31) | of(2022, Month.FEBRUARY, 1)  | KLINES     | List.of("BTCUSDT-1m-2022-1-31.zip", "BTCUSDT-1m-2022-2-1.zip")                                                | "start and end on months border"
        of(2022, Month.JANUARY, 31) | of(2022, Month.FEBRUARY, 1)  | TRADES     | List.of("BTCUSDT-trades-2022-1-31.zip", "BTCUSDT-trades-2022-2-1.zip")                                        | "start and end on months border"
        of(2022, Month.JANUARY, 31) | of(2022, Month.MARCH, 1)     | AGG_TRADES | List.of("BTCUSDT-aggTrades-2022-1-31.zip", "BTCUSDT-aggTrades-2022-2.zip", "BTCUSDT-aggTrades-2022-3-1.zip")  | "captures days on borders and full month between"
        of(2022, Month.JANUARY, 31) | of(2022, Month.MARCH, 1)     | KLINES     | List.of("BTCUSDT-1m-2022-1-31.zip", "BTCUSDT-1m-2022-2.zip", "BTCUSDT-1m-2022-3-1.zip")                       | "captures days on borders and full month between"
        of(2022, Month.JANUARY, 31) | of(2022, Month.MARCH, 1)     | TRADES     | List.of("BTCUSDT-trades-2022-1-31.zip", "BTCUSDT-trades-2022-2.zip", "BTCUSDT-trades-2022-3-1.zip")           | "captures days on borders and full month between"
        of(2022, Month.JANUARY, 1)  | of(2022, Month.FEBRUARY, 28) | AGG_TRADES | List.of("BTCUSDT-aggTrades-2022-1.zip", "BTCUSDT-aggTrades-2022-2.zip")                                       | "captures full months between start of one and end of the next one"
        of(2022, Month.JANUARY, 1)  | of(2022, Month.FEBRUARY, 28) | KLINES     | List.of("BTCUSDT-1m-2022-1.zip", "BTCUSDT-1m-2022-2.zip")                                                     | "captures full months between start of one and end of the next one"
        of(2022, Month.JANUARY, 1)  | of(2022, Month.FEBRUARY, 28) | TRADES     | List.of("BTCUSDT-trades-2022-1.zip", "BTCUSDT-trades-2022-2.zip")                                             | "captures full months between start of one and end of the next one"


    }
}
