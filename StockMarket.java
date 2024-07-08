package com.example.test7;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class StockMarket extends BaseActivity {
    protected int getLayoutResourceId() {
        return R.layout.activity_stock_market;
    }


    private TextView currencyView, marketNewsView, riskLevelView;
    private EditText stockNameInput, amountInput;
    private Button evaluateButton, buyButton, sellButton, portfolioButton;
    private double userCurrency = 1000.0;
    private static Map<String, Stock> stockMarket = new HashMap<>();
    private Portfolio userPortfolio = new Portfolio();
    private Random random = new Random();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initializeViews();
        setupStockMarket();
        updateMarketNews();
        setButtonListeners();
    }

    private void initializeViews() {
        currencyView = findViewById(R.id.currencyView);
        marketNewsView = findViewById(R.id.marketNewsView);
        riskLevelView = findViewById(R.id.riskLevelView);
        stockNameInput = findViewById(R.id.stockNameInput);
        amountInput = findViewById(R.id.amountInput);
        evaluateButton = findViewById(R.id.evaluateButton);
        buyButton = findViewById(R.id.buyButton);
        sellButton = findViewById(R.id.sellButton);
        portfolioButton = findViewById(R.id.portfolioButton);

        updateCurrencyDDisplay();
    }

    private void setupStockMarket() {
        stockMarket.put("AAPL", new Stock("AAPL", 150.0, "Apple Inc. - Tech giant"));
        stockMarket.put("GOOGL", new Stock("GOOGL", 2800.0, "Alphabet Inc. - Search and cloud"));
        stockMarket.put("AMZN", new Stock("AMZN", 3300.0, "Amazon.com Inc. - E-commerce leader"));
        stockMarket.put("MSFT", new Stock("MSFT", 300.0, "Microsoft Corporation - Software leader"));
        stockMarket.put("TSLA", new Stock("TSLA", 700.0, "Tesla, Inc. - Electric vehicles and energy"));
    }

    private void updateMarketNews() {
        StringBuilder news = new StringBuilder();
        for (Stock stock : stockMarket.values()) {
            updateStockPrice(stock);
            news.append(stock.getName()).append(": $").append(String.format("%.2f", stock.getPrice()))
                    .append(" - ").append(stock.getDescription()).append("\n\n");
        }
        marketNewsView.setText(news.toString());
    }

    private void updateStockPrice(Stock stock) {
        double change = (random.nextDouble() - 0.5) * 10; // -5% to +5% change
        stock.setPrice(stock.getPrice() * (1 + change / 100));
    }

    private void setButtonListeners() {
        evaluateButton.setOnClickListener(v -> evaluateStock());
        buyButton.setOnClickListener(v -> buyStock());
        sellButton.setOnClickListener(v -> sellStock());
        portfolioButton.setOnClickListener(v -> openPortfolio());
    }

    private void updateCurrencyDDisplay() {
        currencyView.setText(String.format("Currency: $%.2f", userCurrency));
    }

    private void evaluateStock() {
        String stockName = stockNameInput.getText().toString().toUpperCase();
        if (stockMarket.containsKey(stockName)) {
            Stock stock = stockMarket.get(stockName);
            String riskLevel = stock.getRiskLevel();
            riskLevelView.setText("Risk Level: " + riskLevel);
            switch (riskLevel) {
                case "Low":
                    riskLevelView.setTextColor(getResources().getColor(android.R.color.holo_green_dark));
                    break;
                case "Medium":
                    riskLevelView.setTextColor(getResources().getColor(android.R.color.holo_orange_dark));
                    break;
                case "High":
                    riskLevelView.setTextColor(getResources().getColor(android.R.color.holo_red_dark));
                    break;
            }
        } else {
            Toast.makeText(this, "Stock not found", Toast.LENGTH_SHORT).show();
        }
    }

    private void buyStock() {
        String stockName = stockNameInput.getText().toString().toUpperCase();
        String amountStr = amountInput.getText().toString();
        if (stockName.isEmpty() || amountStr.isEmpty()) {
            Toast.makeText(this, "Please enter both stock name and amount", Toast.LENGTH_SHORT).show();
            return;
        }
        int amount = Integer.parseInt(amountStr);
        if (stockMarket.containsKey(stockName)) {
            Stock stock = stockMarket.get(stockName);
            double totalCost = stock.getPrice() * amount;
            if (userCurrency >= totalCost) {
                userCurrency -= totalCost;
                userPortfolio.addStock(stock, amount);
                updateCurrencyDDisplay();
                Toast.makeText(this, "Stock purchased", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Insufficient funds", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "Stock not found", Toast.LENGTH_SHORT).show();
        }
    }

    private void sellStock() {
        String stockName = stockNameInput.getText().toString().toUpperCase();
        String amountStr = amountInput.getText().toString();
        if (stockName.isEmpty() || amountStr.isEmpty()) {
            Toast.makeText(this, "Please enter both stock name and amount", Toast.LENGTH_SHORT).show();
            return;
        }
        int amount = Integer.parseInt(amountStr);
        if (userPortfolio.hasStock(stockName, amount)) {
            Stock stock = stockMarket.get(stockName);
            double totalValue = stock.getPrice() * amount;
            userCurrency += totalValue;
            userPortfolio.removeStock(stockName, amount);
            updateCurrencyDDisplay();
            Toast.makeText(this, "Stock sold", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Not enough stocks to sell", Toast.LENGTH_SHORT).show();
        }
    }

    private void openPortfolio() {
        Intent intent = new Intent(StockMarket.this, PortfolioActivity.class);
        intent.putExtra("portfolio", userPortfolio);
        startActivity(intent);
    }

    public static Map<String, Stock> getStockMarket() {
        return stockMarket;
    }
}
