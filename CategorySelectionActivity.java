package com.example.test7;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class CategorySelectionActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_selection);

        Button[] categoryButtons = new Button[]{
                findViewById(R.id.btnCategory1),
                findViewById(R.id.btnCategory2),
                findViewById(R.id.btnCategory3),
                findViewById(R.id.btnCategory4)
        };

        for (Button button : categoryButtons) {
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showDifficultyDialog(((Button) v).getText().toString());
                }
            });
        }
    }

    private void showDifficultyDialog(final String category) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Select Difficulty");
        final String[] difficulties = {"Easy", "Medium", "Hard"};
        builder.setItems(difficulties, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String difficulty = difficulties[which];
                startQuiz(category, difficulty);
            }
        });
        builder.show();
    }

    private void startQuiz(String category, String difficulty) {
        Intent intent = new Intent(CategorySelectionActivity.this, QuizActivity.class);
        intent.putExtra("category", category);
        intent.putExtra("difficulty", difficulty);
        startActivity(intent);
    }
}