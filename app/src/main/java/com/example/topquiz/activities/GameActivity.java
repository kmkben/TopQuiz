package com.example.topquiz.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.topquiz.R;
import com.example.topquiz.model.QuestionBank;
import com.example.topquiz.model.Question;

import java.util.Arrays;

public class GameActivity extends AppCompatActivity implements View.OnClickListener {

    public static final String RESULT_SCORE = "RESULT_SCORE";
    public static final String BUNDLE_EXTRA_SCORE = "BUNDLE EXTRA SCORE";

    private static final String BUNDLE_STATE_SCORE = "BUNDLE_STATE_SCORE";
    private static final String BUNDLE_STATE_QUESTION_COUNT = "BUNDLE_STATE_QUESTION_COUNT";
    private static final String BUNDLE_STATE_QUESTION_BANK = "BUNDLE_STATE_QUESTION_BANK";

    private static final int INITIAL_QUESTION_COUNT = 4;

    private TextView questionTextView;
    private Button answer1;
    private Button answer2;
    private Button answer3;
    private Button answer4;

    private int score;
    private int remainQuestionCount;
    private QuestionBank questions;

    private boolean enableTouchEvent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        questionTextView = (TextView) findViewById(R.id.question);

        answer1 = (Button) findViewById(R.id.game_activity_button1);
        answer2 = (Button) findViewById(R.id.game_activity_button2);
        answer3 = (Button) findViewById(R.id.game_activity_button3);
        answer4 = (Button) findViewById(R.id.game_activity_button4);

        answer1.setOnClickListener(this);
        answer2.setOnClickListener(this);
        answer3.setOnClickListener(this);
        answer4.setOnClickListener(this);

        enableTouchEvent = true;

        if (savedInstanceState != null) {
            score = savedInstanceState.getInt(BUNDLE_STATE_SCORE);
            remainQuestionCount = savedInstanceState.getInt(BUNDLE_STATE_QUESTION_COUNT);
            questions = (QuestionBank) savedInstanceState.getSerializable(BUNDLE_STATE_QUESTION_BANK);
        } else {
            score = 0;
            remainQuestionCount = INITIAL_QUESTION_COUNT;
            questions = generateQuestions();
        }

        displayQuestion(questions.getCurrentQuestion());

    }


    protected void onSavedInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putInt(BUNDLE_STATE_SCORE, score);
        outState.putInt(BUNDLE_STATE_QUESTION_COUNT, remainQuestionCount);
        outState.putSerializable(BUNDLE_STATE_QUESTION_BANK, questions);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        return enableTouchEvent && super.dispatchTouchEvent(ev);
    }

    @Override
    public void onClick(View view) {
        int index;

        if (view == answer1){
            index = 0;
        } else if (view == answer2) {
            index = 1;
        } else if (view == answer3) {
            index = 2;
        } else if (view == answer4) {
            index = 3;
        } else {
            throw new IllegalStateException("Unknown clicked button " + view);
        }

        if (index == questions.getCurrentQuestion().getAnswerIndex()) {
            Toast.makeText(this, "Correct", Toast.LENGTH_SHORT).show();
            score++;
        } else {
            Toast.makeText(this, "Wrong answer", Toast.LENGTH_SHORT).show();
        }

        enableTouchEvent = false;

        new Handler(getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                enableTouchEvent = true;

                remainQuestionCount--;

                if (remainQuestionCount <= 0) {
                    endGame();
                } else {
                    displayQuestion(questions.getNextQuestion());
                }
            }
        }, 2_000);
    }

    private void displayQuestion(final Question question) {
        questionTextView.setText(question.getQuestion());
        answer1.setText(question.getChoiceList().get(0));
        answer2.setText(question.getChoiceList().get(1));
        answer3.setText(question.getChoiceList().get(2));
        answer4.setText(question.getChoiceList().get(3));
    }
    private void endGame() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("Well done !!")
                .setMessage("Your Score is " + score)
                .setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent intent = new Intent();
                        intent.putExtra(RESULT_SCORE, score);
                        setResult(Activity.RESULT_OK, intent);
                        finish();
                    }
                })
                .create()
                .show();
    }

    private QuestionBank generateQuestions() {

        Question question1 = new Question("Who is the creator of Android?",
            Arrays.asList(
                 "Andy Rubin",
                 "Steve Wozniak",
                 "Jake Wharton",
                 "Paul Smith"
            ),
            0
        );

        Question question2 = new Question("When did the first man land on the moon?",
            Arrays.asList(
                "1958",
                "1962",
                "1967",
                "1969"
            ),
            3
        );

        Question question3 = new Question("What is the house number of The Simpsons?",
            Arrays.asList(
                "42",
                "101",
                "666",
                "742"
            ),
            3
        );

        Question question4 = new Question("Who did the Mona Lisa paint?",
            Arrays.asList(
                "Michelangelo",
                "Leonardo Da Vinci",
                "Raphael",
                "Carravagio"
            ),
            1
        );

        Question question5 = new Question(
            "What is the country top-level domain of Belgium?",
            Arrays.asList(
                ".bg",
                ".bm",
                ".bl",
                ".be"
            ),
            3
        );

        return new QuestionBank(Arrays.asList(question1, question2, question3, question4, question5));

    }
}