package com.example.test7;



import java.util.HashMap;
import java.util.Map;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class Portfolio implements Serializable {
    private Map<String, Integer> holdings = new HashMap<>();

    public void addStock(Stock stock, int quantity) {
        holdings.put(stock.getName(), holdings.getOrDefault(stock.getName(), 0) + quantity);
    }

    public void removeStock(String stockName, int quantity) {
        int currentQuantity = holdings.getOrDefault(stockName, 0);
        if (currentQuantity >= quantity) {
            holdings.put(stockName, currentQuantity - quantity);
            if (holdings.get(stockName) == 0) {
                holdings.remove(stockName);
            }
        }
    }

    public boolean hasStock(String stockName, int quantity) {
        return holdings.getOrDefault(stockName, 0) >= quantity;
    }

    public Map<String, Integer> getHoldings() {
        return holdings;
    }
}
