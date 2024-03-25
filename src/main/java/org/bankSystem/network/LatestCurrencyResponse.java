package org.bankSystem.network;

import java.util.Map;

public class LatestCurrencyResponse {
    private long amount;
    private String base;
    private String date;
    private Map<String, Double> rates;

    public long getAmount() { return amount; }
    public void setAmount(long value) { this.amount = value; }

    public String getBase() { return base; }
    public void setBase(String value) { this.base = value; }

    public String getDate() { return date; }
    public void setDate(String value) { this.date = value; }

    public Map<String, Double> getRates() { return rates; }
    public void setRates(Map<String, Double> value) { this.rates = value; }
}
