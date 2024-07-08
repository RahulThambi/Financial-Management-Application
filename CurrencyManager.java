package com.example.test7;

import android.content.Context;
import android.content.SharedPreferences;

public class CurrencyManager {
    private static CurrencyManager instance;
    private SharedPreferences sharedPreferences;
    private static final String PREF_NAME = "CurrencyPrefs";
    private static final String CURRENCY_KEY = "virtualCurrency";
    private static final String POINTS_KEY = "points";

    private CurrencyManager(Context context)
    {
        sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }

    public static synchronized CurrencyManager getInstance(Context context) {
        if (instance == null) {
            instance = new CurrencyManager(context.getApplicationContext());
        }
        return instance;
    }

    public int getVirtualCurrency() {
        return sharedPreferences.getInt(CURRENCY_KEY, 0);
    }

    public int getPoints() {
        return sharedPreferences.getInt(POINTS_KEY, 0);
    }

    //when currency/points is needed to be added
    public void addCurrency(int amount) {
        int currentCurrency = getVirtualCurrency();
        sharedPreferences.edit().putInt(CURRENCY_KEY, currentCurrency + amount).apply();
    }

    public void addPoints(int amount) {
        int currentPoints = getPoints();
        sharedPreferences.edit().putInt(POINTS_KEY, currentPoints + amount).apply();
    }

    //when currency/points is needed to be deducted
    public void subCurrency(int amount) {
        int currentCurrency = getVirtualCurrency();
        sharedPreferences.edit().putInt(CURRENCY_KEY, currentCurrency - amount).apply();
    }

    public void subPoints(int amount) {
        int currentPoints = getPoints();
        sharedPreferences.edit().putInt(POINTS_KEY, currentPoints - amount).apply();
    }

    public boolean spendCurrency(int amount) {
        int currentCurrency = getVirtualCurrency();
        if (currentCurrency >= amount) {
            sharedPreferences.edit().putInt(CURRENCY_KEY, currentCurrency - amount).apply();
            return true;
        }
        return false;
    }

    public void spendPoints(int amount) {
        int currentPoints = getPoints();
        sharedPreferences.edit().putInt(POINTS_KEY, Math.max(0, currentPoints - amount)).apply();
    }
}