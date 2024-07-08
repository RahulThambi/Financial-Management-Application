package com.example.test7;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class QuizActivity extends AppCompatActivity {
    private TextView tvQuestion;
    private Button[] optionButtons;
    private ProgressBar timerProgressBar;
    private CountDownTimer timer;
    private int currentQuestion = 0;
    private int score = 0;
    private List<Question> questions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        tvQuestion = findViewById(R.id.tvQuestion);
        timerProgressBar = findViewById(R.id.timerProgressBar);
        optionButtons = new Button[]{
                findViewById(R.id.btnOption1),
                findViewById(R.id.btnOption2),
                findViewById(R.id.btnOption3),
                findViewById(R.id.btnOption4)
        };

        String category = getIntent().getStringExtra("category");
        String difficulty = getIntent().getStringExtra("difficulty");

        questions = getQuestions(category, difficulty);
        displayQuestion();
    }

    private List<Question> getQuestions(String category, String difficulty) {
        // Implement this method to return a list of questions based on category and difficulty
        // For now, we'll return a dummy list
        List<Question> dummyQuestions = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            dummyQuestions.add(new Question("Question " + (i + 1), new String[]{"Option 1", "Option 2", "Option 3", "Option 4"}, 0));
        }
        return dummyQuestions;
    }

    private void displayQuestion() {
        if (currentQuestion < questions.size()) {
            Question question = questions.get(currentQuestion);
            tvQuestion.setText(question.getQuestion());
            for (int i = 0; i < 4; i++) {
                optionButtons[i].setText(question.getOptions()[i]);
                final int optionIndex = i;
                optionButtons[i].setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        checkAnswer(optionIndex);
                    }
                });
            }
            startTimer();
        } else {
            showResult();
        }
    }

    private void startTimer() {
        timerProgressBar.setProgress(100);
        timer = new CountDownTimer(30000, 100) {
            @Override
            public void onTick(long millisUntilFinished) {
                int progress = (int) (millisUntilFinished / 300);
                timerProgressBar.setProgress(progress);
            }

            @Override
            public void onFinish() {
                checkAnswer(-1);
            }
        }.start();
    }

    private void checkAnswer(int selectedOption) {
        timer.cancel();
        if (selectedOption == questions.get(currentQuestion).getCorrectAnswer()) {
            score++;
        }
        currentQuestion++;
        displayQuestion();
    }

    private void showResult() {
        Intent intent = new Intent(QuizActivity.this, ResultActivity.class);
        intent.putExtra("score", score);
        intent.putExtra("totalQuestions", questions.size());
        startActivity(intent);
        finish();
    }

    private static class Question {
        private String question;
        private String[] options;
        private int correctAnswer;

        public Question(String question, String[] options, int correctAnswer) {
            this.question = question;
            this.options = options;
            this.correctAnswer = correctAnswer;
        }

        public String getQuestion() {
            return question;
        }

        public String[] getOptions() {
            return options;
        }

        public int getCorrectAnswer() {
            return correctAnswer;
        }
    }
}