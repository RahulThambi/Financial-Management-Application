package com.example.test7;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class NurseryActivity extends BaseActivity {

    protected int getLayoutResourceId() {
        return R.layout.activity_nursery;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Button buyButton1 = findViewById(R.id.buy_button_1);
        buyButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(NurseryActivity.this, "Plant 1 bought!", Toast.LENGTH_SHORT).show();
            }
        });

        // Repeat for other buy buttons
    }
}
