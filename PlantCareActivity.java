package com.example.test7;

import android.Manifest;
import android.content.pm.PackageManager;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.annotation.NonNull;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class PlantCareActivity extends BaseActivity {
    protected int getLayoutResourceId() {
        return R.layout.activity_plant_care;
    }

    private TextView greetingText, coinCount, plantNeedsText, streakText, marketNewsView;
    private ImageView plantImage;
    private Button waterButton, fertilizeButton, pesticideButton, sunlightButton, lessonsButton, quizButton, nurseryButton,stickerButton;
    private ProgressBar streakProgress, loadingProgress;

    private int coins = 200;
    private int streak = 0;
    private Random random = new Random();
    private Handler handler = new Handler();
    private String currentNeed = "";
    private String lastNeed = "";
    private boolean isActionNeeded = false;
    private static final int NOTIFICATION_PERMISSION_REQUEST_CODE = 123;

    // Stock market variables
    private Map<String, Stock> stockMarket = new HashMap<>();

    // Notification variables
    private static final String CHANNEL_ID = "PlantCareChannel";
    private static final int PLANT_NEED_NOTIFICATION_ID = 1;
    private static final int DAILY_REMINDER_NOTIFICATION_ID = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initializeViews();
        setGreeting();
        updateCoinCount();
        updateStreakProgress();
        setButtonListeners();
        setupStockMarket(); // Initialize stock market data
        updateMarketNews(); // Update market news initially
        schedulePlantNeeds();
        createNotificationChannel();
        scheduleDailyReminder();

        stickerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PlantCareActivity.this,StickerStore.class);
                startActivity(intent);
            }
        });


    }

    private void initializeViews() {
        nurseryButton = findViewById(R.id.nurseryButton);
        greetingText = findViewById(R.id.greetingText);
        coinCount = findViewById(R.id.coinCount);
        plantImage = findViewById(R.id.plantImage);
        waterButton = findViewById(R.id.waterButton);
        fertilizeButton = findViewById(R.id.fertilizeButton);
        pesticideButton = findViewById(R.id.pesticideButton);
        sunlightButton = findViewById(R.id.sunlightButton);
        plantNeedsText = findViewById(R.id.plantNeedsText);
        streakProgress = findViewById(R.id.streakProgress);
        streakText = findViewById(R.id.streakText);
        loadingProgress = findViewById(R.id.loadingProgress);
        lessonsButton = findViewById(R.id.lessonsButton);
        quizButton = findViewById(R.id.quizButton);
        marketNewsView = findViewById(R.id.marketNewsView);
        stickerButton = findViewById(R.id.stickerButton);

    }



    private void setGreeting() {
        Calendar c = Calendar.getInstance();
        int timeOfDay = c.get(Calendar.HOUR_OF_DAY);

        String greeting;
        if (timeOfDay >= 0 && timeOfDay < 12) {
            greeting = "Good Morning";
        } else if (timeOfDay >= 12 && timeOfDay < 16) {
            greeting = "Good Afternoon";
        } else if (timeOfDay >= 16 && timeOfDay < 21) {
            greeting = "Good Evening";
        } else {
            greeting = "Good Night";
        }

        greetingText.setText("Hello User! " + greeting);
    }

    private void updateCoinCount() {
        coinCount.setText("Coins: " + coins);
    }

    private void updateStreakProgress() {
        streakProgress.setProgress(streak);
        streakText.setText("Streak: " + streak + "/7");
    }

    private void setButtonListeners() {
        View.OnClickListener actionListener = v -> {
            if (isActionNeeded && coins >= 25) {
                coins -= 25;
                updateCoinCount();
                plantNeedsText.setVisibility(View.GONE);
                int actionDrawable = R.drawable.normal;
                if (v == waterButton && currentNeed.equals("water")) {
                    actionDrawable = R.drawable.plantwater;
                } else if (v == fertilizeButton && currentNeed.equals("fertilizer")) {
                    actionDrawable = R.drawable.plantfert;
                } else if (v == pesticideButton && currentNeed.equals("pesticide")) {
                    actionDrawable = R.drawable.plantpesticide;
                } else if (v == sunlightButton && currentNeed.equals("sunlight")) {
                    actionDrawable = R.drawable.plantsun;
                } else {
                    Toast.makeText(this, "Wrong action! The plant needs " + currentNeed, Toast.LENGTH_SHORT).show();
                    coins += 25; // Refund the coins
                    updateCoinCount();
                    return;
                }
                performAction(actionDrawable);
                isActionNeeded = false;
                streak++;
                updateStreakProgress();
                if (streak == 7) {
                    coins += 100;
                    updateCoinCount();
                    Toast.makeText(this, "Streak complete! You earned 100 coins!", Toast.LENGTH_LONG).show();
                    streak = 0;
                }
            } else if (!isActionNeeded) {
                Toast.makeText(this, "The plant doesn't need anything right now!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Not enough coins!", Toast.LENGTH_SHORT).show();
            }
        };

        waterButton.setOnClickListener(actionListener);
        fertilizeButton.setOnClickListener(actionListener);
        pesticideButton.setOnClickListener(actionListener);
        sunlightButton.setOnClickListener(actionListener);

        lessonsButton.setOnClickListener(v -> {
            Intent intent = new Intent(PlantCareActivity.this, LessonActivity.class);
            startActivity(intent);
        });

        quizButton.setOnClickListener(v -> {
            Intent intent = new Intent(PlantCareActivity.this, CategorySelectionActivity.class);
            startActivity(intent);
        });

        nurseryButton.setOnClickListener(v -> {
            Intent intent = new Intent(PlantCareActivity.this, NurseryActivity.class);
            startActivity(intent);
        });
    }

    private void performAction(int actionDrawable) {
        loadingProgress.setVisibility(View.VISIBLE);
        disableAllButtons();

        handler.postDelayed(() -> {
            loadingProgress.setVisibility(View.GONE);
            plantImage.setImageResource(actionDrawable);

            handler.postDelayed(() -> {
                plantImage.setImageResource(R.drawable.normal);
                currentNeed = "";
                enableAllButtons();
                schedulePlantNeeds();
            }, 2000);
        }, 2000);
    }

    private void disableAllButtons() {
        waterButton.setEnabled(false);
        fertilizeButton.setEnabled(false);
        pesticideButton.setEnabled(false);
        sunlightButton.setEnabled(false);
        lessonsButton.setEnabled(false);
        quizButton.setEnabled(false);
    }

    private void enableAllButtons() {
        waterButton.setEnabled(true);
        fertilizeButton.setEnabled(true);
        pesticideButton.setEnabled(true);
        sunlightButton.setEnabled(true);
        lessonsButton.setEnabled(true);
        quizButton.setEnabled(true);
    }

    private void schedulePlantNeeds() {
        handler.postDelayed(() -> {
            if (!isActionNeeded) {
                String[] needs = {"water", "fertilizer", "pesticide", "sunlight"};
                do {
                    currentNeed = needs[random.nextInt(needs.length)];
                } while (currentNeed.equals(lastNeed));

                lastNeed = currentNeed;
                plantNeedsText.setText("Your plant needs " + currentNeed + "!");
                plantNeedsText.setVisibility(View.VISIBLE);
                updatePlantImage();
                enableOnlyNeededButton();
                isActionNeeded = true;
                sendPlantNeedNotification();
            }
        }, random.nextInt(10000) + 20000); // Random time between 20s and 30s
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Plant Care Channel";
            String description = "Channel for Plant Care notifications";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }
    private boolean checkNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.POST_NOTIFICATIONS},
                        NOTIFICATION_PERMISSION_REQUEST_CODE);
                return false;
            }
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == NOTIFICATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted, you can send notifications now
            } else {
                // Permission denied, handle accordingly (e.g., show a message to the user)
                Toast.makeText(this, "Notification permission denied. Some features may not work properly.", Toast.LENGTH_LONG).show();
            }
        }
    }
    private void sendPlantNeedNotification() {
        if (checkNotificationPermission()) {
            Intent intent = new Intent(this, PlantCareActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_IMMUTABLE);

            NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                    .setSmallIcon(R.drawable.baseline_circle_notifications_24)
                    .setContentTitle("Plant Care")
                    .setContentText("Your plant needs " + currentNeed + "!")
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                    .setContentIntent(pendingIntent)
                    .setAutoCancel(true);

            NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
            try {
                notificationManager.notify(PLANT_NEED_NOTIFICATION_ID, builder.build());
            } catch (SecurityException e) {
                e.printStackTrace();
                // Handle the exception (e.g., request permission again or inform the user)
            }
        }
    }

    private void sendDailyReminderNotification() {
        if (checkNotificationPermission()) {
            Intent intent = new Intent(this, LessonActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_IMMUTABLE);

            NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                    .setSmallIcon(R.drawable.baseline_circle_notifications_24)
                    .setContentTitle("Daily Reminder")
                    .setContentText("Complete your daily lessons and quizzes to earn bonus points and coins!")
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                    .setContentIntent(pendingIntent)
                    .setAutoCancel(true);

            NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
            try {
                notificationManager.notify(DAILY_REMINDER_NOTIFICATION_ID, builder.build());
            } catch (SecurityException e) {
                e.printStackTrace();
                // Handle the exception (e.g., request permission again or inform the user)
            }
        }
    }

    private void scheduleDailyReminder() {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                sendDailyReminderNotification();
                handler.postDelayed(this, 60000); // Run every 1 minute
            }
        }, 60000); // Start after 1 minute
    }



    private void updatePlantImage() {
        switch (currentNeed) {
            case "water":
                plantImage.setImageResource(R.drawable.plantdry);
                break;
            case "fertilizer":
                plantImage.setImageResource(R.drawable.normal);
                break;
            case "pesticide":
                plantImage.setImageResource(R.drawable.plantinfectedpests);
                break;
            case "sunlight":
                plantImage.setImageResource(R.drawable.plantdry);
                break;
        }
    }

    private void enableOnlyNeededButton() {
        disableAllButtons();
        switch (currentNeed) {
            case "water":
                waterButton.setEnabled(true);
                break;
            case "fertilizer":
                fertilizeButton.setEnabled(true);
                break;
            case "pesticide":
                pesticideButton.setEnabled(true);
                break;
            case "sunlight":
                sunlightButton.setEnabled(true);
                break;
        }
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
}