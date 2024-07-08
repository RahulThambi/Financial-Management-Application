package com.example.test7;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public abstract class BaseActivity extends AppCompatActivity {

    protected CurrencyManager currencyManager;
    protected TextView currencyTextView;
    protected TextView pointsTextView;
    private FloatingActionButton fabMain, fabOption1, fabOption2, fabOption3, fabOption4;
    private Animation fabOpen, fabClose, rotateForward, rotateBackward;
    private boolean isOpen = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutResourceId());

        fabMain = findViewById(R.id.fab_main);
        fabOption1 = findViewById(R.id.fab_option1);
        fabOption2 = findViewById(R.id.fab_option2);
        fabOption3 = findViewById(R.id.fab_option3);
        fabOption4 = findViewById(R.id.fab_option4);
        currencyManager = CurrencyManager.getInstance(this);

        initializeCurrencyViews();
        updateCurrencyDisplay();

        fabOpen = AnimationUtils.loadAnimation(this, R.anim.fab_open);
        fabClose = AnimationUtils.loadAnimation(this, R.anim.fab_close);
        rotateForward = AnimationUtils.loadAnimation(this, R.anim.rotate_forward);
        rotateBackward = AnimationUtils.loadAnimation(this, R.anim.rotate_backward);

        fabMain.setOnClickListener(view -> animateFab());

        fabOption1.setOnClickListener(view -> navigateToScreen(ExpenseSaverTracker.class));
        fabOption2.setOnClickListener(view -> navigateToScreen(ChoreManager.class));
        fabOption3.setOnClickListener(view -> navigateToScreen(StockMarket.class));
        fabOption4.setOnClickListener(view -> navigateToScreen(PlantCareActivity.class));
    }

    protected abstract int getLayoutResourceId();

    //currency part start
    private void initializeCurrencyViews() {
        currencyTextView = findViewById(R.id.currencyTextView);
        pointsTextView = findViewById(R.id.pointsTextView);
    }

    protected void updateCurrencyDisplay() {
        if (currencyTextView != null) {
            currencyTextView.setText(String.valueOf(currencyManager.getVirtualCurrency()));
        }
        if (pointsTextView != null) {
            pointsTextView.setText(currencyManager.getPoints() + " pts");
        }
    }

    //currency part over
        private void animateFab() {
        if (isOpen) {
            fabMain.startAnimation(rotateBackward);
            fabOption1.startAnimation(fabClose);
            fabOption2.startAnimation(fabClose);
            fabOption3.startAnimation(fabClose);
            fabOption4.startAnimation(fabClose);
            fabOption1.setVisibility(View.INVISIBLE);
            fabOption2.setVisibility(View.INVISIBLE);
            fabOption3.setVisibility(View.INVISIBLE);
            fabOption4.setVisibility(View.INVISIBLE);
            isOpen = false;
        } else {
            fabMain.startAnimation(rotateForward);
            fabOption1.startAnimation(fabOpen);
            fabOption2.startAnimation(fabOpen);
            fabOption3.startAnimation(fabOpen);
            fabOption4.startAnimation(fabOpen);
            fabOption1.setVisibility(View.VISIBLE);
            fabOption2.setVisibility(View.VISIBLE);
            fabOption3.setVisibility(View.VISIBLE);
            fabOption4.setVisibility(View.VISIBLE);
            isOpen = true;
        }
    }

    private void navigateToScreen(Class<?> destinationActivity) {
        if (this.getClass() != destinationActivity) {
            Intent intent = new Intent(this, destinationActivity);
            startActivity(intent);
            finish();
        } else {
            animateFab();
        }
    }
}