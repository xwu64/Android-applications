package edu.iit.xwu64.hw4_stock_watch;

/**
 * Created by xiaoliangwu on 2017/2/27.
 */

public class Stock {
    private String symbol;
    private String name;
    private double price;
    private double priceChange;
    private double changePercent;

    public Stock(String symbol, String name, double price, double priceChange, double changePercent) {
        this.symbol = symbol;
        this.name = name;
        this.price = price;
        this.priceChange = priceChange;
        this.changePercent = changePercent;
    }

    public Stock(String symbol, String name) {
        this.symbol = symbol;
        this.name = name;
    }

    public String getSymbol() {
        return symbol;
    }

    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }

    public double getPriceChange() {
        return priceChange;
    }

    public double getChangePercent() {
        return changePercent;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public void setPriceChange(double priceChange) {
        this.priceChange = priceChange;
    }

    public void setChangePercent(double changePercent) {
        this.changePercent = changePercent;
    }

}
