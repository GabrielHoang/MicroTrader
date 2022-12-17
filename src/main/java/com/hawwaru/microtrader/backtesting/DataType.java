package com.hawwaru.microtrader.backtesting;

public enum DataType {
    AGG_TRADES("aggTrades"),
    KLINES("klines"),
    TRADES("trades");

    private final String dataTypeId;

    DataType(String dataTypeId) {
        this.dataTypeId = dataTypeId;
    }

    public String getDataTypeId() {
        return this.dataTypeId;
    }
}
