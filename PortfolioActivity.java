package com.example.test7;


import android.graphics.Color;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import java.util.Map;

public class PortfolioActivity extends AppCompatActivity {

    private Portfolio userPortfolio;
    private Map<String, Stock> stockMarket;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_portfolio);

        userPortfolio = (Portfolio) getIntent().getSerializableExtra("portfolio");
        stockMarket = StockMarket.getStockMarket(); // Method to get stock market data

        initializeViews();
    }

    private void initializeViews() {
        LinearLayout portfolioLayout = findViewById(R.id.portfolioLayout);

        for (Map.Entry<String, Integer> entry : userPortfolio.getHoldings().entrySet()) {
            String stockName = entry.getKey();
            int quantity = entry.getValue();
            Stock stock = stockMarket.get(stockName);
            double totalValue = stock.getPrice() * quantity;

            CardView cardView = new CardView(this);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            layoutParams.setMargins(16, 16, 16, 16);
            cardView.setLayoutParams(layoutParams);
            cardView.setCardElevation(8);
            cardView.setRadius(12);
            cardView.setContentPadding(24, 24, 24, 24);
            cardView.setCardBackgroundColor(Color.parseColor("#FFFFFF"));
            cardView.setUseCompatPadding(true);

            TextView stockInfo = new TextView(this);
            stockInfo.setText(stockName + ": " + quantity + " shares\nValue: $" + String.format("%.2f", totalValue));
            stockInfo.setTextSize(18);
            stockInfo.setTextColor(Color.BLACK);

            cardView.addView(stockInfo);
            portfolioLayout.addView(cardView);
        }
    }
}
