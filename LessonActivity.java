package com.example.test7;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.cardview.widget.CardView;

public class LessonActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lesson);

        // Chapter 1
        CardView chapter1Card = findViewById(R.id.chapter1Card);
        chapter1Card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LessonActivity.this, ChapterDetailActivity.class);
                intent.putExtra("CHAPTER_TITLE", "Chapter 1: Introduction to Finance");
                intent.putExtra("CHAPTER_CONTENT", "Introduction to Finance covers the basics of financial concepts, including the importance of finance, financial planning, and understanding financial terms.");
                startActivity(intent);
            }
        });

        // Chapter 2
        CardView chapter2Card = findViewById(R.id.chapter2Card);
        chapter2Card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LessonActivity.this, ChapterDetailActivity.class);
                intent.putExtra("CHAPTER_TITLE", "Chapter 2: Budgeting Basics");
                intent.putExtra("CHAPTER_CONTENT", "Budgeting is the process of creating a plan to spend your money. This spending plan is called a budget. Creating this spending plan allows you to determine in advance whether you will have enough money to do the things you need to do or would like to do.");
                startActivity(intent);
            }
        });

        // Chapter 3
        CardView chapter3Card = findViewById(R.id.chapter3Card);
        chapter3Card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LessonActivity.this, ChapterDetailActivity.class);
                intent.putExtra("CHAPTER_TITLE", "Chapter 3: Saving Strategies");
                intent.putExtra("CHAPTER_CONTENT", "Saving Strategies include methods to save money effectively, understanding different types of savings accounts, and setting savings goals.");
                startActivity(intent);
            }
        });

        // Chapter 4
        CardView chapter4Card = findViewById(R.id.chapter4Card);
        chapter4Card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LessonActivity.this, ChapterDetailActivity.class);
                intent.putExtra("CHAPTER_TITLE", "Chapter 4: Investment Fundamentals");
                intent.putExtra("CHAPTER_CONTENT", "Investment Fundamentals cover the basics of investing, understanding different types of investments, and the importance of diversifying your portfolio.");
                startActivity(intent);
            }
        });

        // Video Lecture Button
        Button btnVideoLecture = findViewById(R.id.btnVideoLecture);
        btnVideoLecture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LessonActivity.this, VideoLectureActivity.class);
                startActivity(intent);
            }
        });

        // Add more chapter card click listeners as needed
    }
}
