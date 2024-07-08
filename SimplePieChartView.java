package com.example.test7;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SimplePieChartView extends View {
    private List<Float> values = new ArrayList<>();
    private List<Integer> colors = new ArrayList<>();
    private List<String> categories = new ArrayList<>();
    private List<Float> percentages = new ArrayList<>();
    private Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private RectF rectF = new RectF();

    private static final Map<String, Integer> CATEGORY_COLORS = new HashMap<String, Integer>() {{
        put("Food", Color.rgb(255, 0, 0));       // Red
        put("Transport", Color.rgb(0, 255, 0));  // Green
        put("Housing", Color.rgb(0, 0, 255));    // Blue
        put("Entertainment", Color.rgb(255, 255, 0)); // Yellow
        put("Utilities", Color.rgb(255, 0, 255));    // Magenta
        put("Other", Color.rgb(0, 255, 255));    // Cyan
    }};

    public SimplePieChartView(Context context) {
        super(context);
        initDefaultData();
    }

    public SimplePieChartView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initDefaultData();
    }

    private void initDefaultData() {
        Map<String, Float> defaultData = new HashMap<>();
        defaultData.put("No Data", 100f);
        setData(defaultData);
    }

    public void setData(Map<String, Float> data) {
        values.clear();
        colors.clear();
        categories.clear();
        percentages.clear();

        float total = 0;
        for (float value : data.values()) {
            total += value;
        }

        for (Map.Entry<String, Float> entry : data.entrySet()) {
            float percentage = entry.getValue() / total * 100;
            values.add(360 * (entry.getValue() / total));
            colors.add(CATEGORY_COLORS.getOrDefault(entry.getKey(), Color.GRAY));
            categories.add(entry.getKey());
            percentages.add(percentage);
        }

        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        float midX = getWidth() / 2f;
        float midY = getHeight() / 2f;
        float radius = Math.min(midX, midY) * 0.8f; // Increased radius to make pie chart bigger
        rectF.set(midX - radius, midY - radius, midX + radius, midY + radius);

        float startAngle = 0;
        for (int i = 0; i < values.size(); i++) {
            paint.setColor(colors.get(i));
            canvas.drawArc(rectF, startAngle, values.get(i), true, paint);
            startAngle += values.get(i);
        }

        // Draw legend
        float legendX = 10;
        float legendY = 30;
        float legendSquareSize = 20;
        paint.setTextSize(24);

        for (int i = 0; i < categories.size(); i++) {
            paint.setColor(colors.get(i));
            canvas.drawRect(legendX, legendY, legendX + legendSquareSize, legendY + legendSquareSize, paint);
            paint.setColor(Color.BLACK);
            String legendText = String.format("%s (%.1f%%)", categories.get(i), percentages.get(i));
            canvas.drawText(legendText, legendX + legendSquareSize + 10, legendY + legendSquareSize, paint);
            legendY += legendSquareSize + 15;

            // If legend reaches bottom of view, start a new column
            if (legendY + legendSquareSize > getHeight()) {
                legendY = 30;
                legendX += getWidth() / 2f;
            }
        }
    }
}