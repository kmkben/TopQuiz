package com.example.topquiz.activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.topquiz.R;
import com.example.topquiz.model.User;


public class MainActivity extends AppCompatActivity {

    private TextView nameText;
    private TextView welcomeText;
    private EditText name;
    private Button play;

    private User user;

    private static final int GAME_ACTIVITY_REQUEST_CODE = 7;

    private static final String SHARED_PREF_USER_INFO = "SHARED_PREF_USER_INFO";
    private static final String SHARED_PREF_USER_INFO_NAME = "SHARED_PREF_USER_INFO_NAME";
    private static final String SHARED_PREF_USER_INFO_SCORE = "SHARED_PREF_USER_INFO_SCORE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        welcomeText = (TextView) findViewById(R.id.welcomeText);
        nameText = (TextView) findViewById(R.id.nameEditTex);
        name = (EditText) findViewById(R.id.nameEditText);
        play = (Button) findViewById(R.id.btnPlay);

        play.setEnabled(false);

        user = new User();



        name.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                play.setEnabled(!editable.toString().isEmpty());

            }
        });

        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                user.setName(name.getText().toString());
                getSharedPreferences(SHARED_PREF_USER_INFO, MODE_PRIVATE)
                        .edit()
                        .putString(SHARED_PREF_USER_INFO_NAME, user.getName())
                        .apply();


                Intent intent = new Intent(MainActivity.this, GameActivity.class);
                //startActivity(intent);
                startActivityForResult(intent, GAME_ACTIVITY_REQUEST_CODE);
            }
        });

        //greetUser();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (GAME_ACTIVITY_REQUEST_CODE == requestCode && RESULT_OK == resultCode) {
            int score = data.getIntExtra(GameActivity.RESULT_SCORE, 0);

            getSharedPreferences(SHARED_PREF_USER_INFO, MODE_PRIVATE)
                    .edit()
                    .putInt(SHARED_PREF_USER_INFO_SCORE, score)
                    .apply();

            //greetUser();
        }
    }

    private void greetUser() {
        String userName = getSharedPreferences(SHARED_PREF_USER_INFO, MODE_PRIVATE).getString(SHARED_PREF_USER_INFO_NAME, null);
        int score = getSharedPreferences(SHARED_PREF_USER_INFO, MODE_PRIVATE).getInt(SHARED_PREF_USER_INFO_SCORE, -1);

        if (userName != null) {

            if (score != -1) {
                welcomeText.setText(getString(R.string.hello_user, userName));
                nameText.setText(getString(R.string.welcome_back_with_score, userName, score));
            } else {
                welcomeText.setText(getString(R.string.hello_user, userName));
                nameText.setText(getString(R.string.welcome_back, userName));
            }

            name.setText(userName);
        }
    }
}