package com.example.test7;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.cardview.widget.CardView;

public class ChapterDetailActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chapter_detail);

        Intent intent = getIntent();
        String chapterTitle = intent.getStringExtra("CHAPTER_TITLE");
        String chapterContent = intent.getStringExtra("CHAPTER_CONTENT");

        TextView chapterTitleView = findViewById(R.id.chapterTitle);
        chapterTitleView.setText(chapterTitle);

        LinearLayout contentLayout = findViewById(R.id.contentLayout);

        // Split the chapter content into paragraphs
        String[] paragraphs = chapterContent.split("\n\n");

        // Create a CardView for each paragraph
        for (String paragraph : paragraphs) {
            CardView cardView = new CardView(this);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );
            layoutParams.setMargins(16, 16, 16, 16);
            cardView.setLayoutParams(layoutParams);

            TextView textView = new TextView(this);
            textView.setText(paragraph);
            textView.setTextSize(16);
            textView.setTextColor(getResources().getColor(R.color.bodyTextColor));
            textView.setPadding(16, 16, 16, 16);

            cardView.addView(textView);
            contentLayout.addView(cardView);
        }
    }
}
