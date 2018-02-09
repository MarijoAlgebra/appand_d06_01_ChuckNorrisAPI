package org.bubulescu.rest;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private Button btnGetJokes;
    private static TextView tvResult;

    private static final String JOKES_ENDPOINT = "http://api.icndb.com/jokes/random/3";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initWidgets();
        setupListeners();

    }

    public static void setJokeToTextView(String joke) {
        tvResult.setText(joke);
    }

    private void initWidgets() {
        btnGetJokes = findViewById(R.id.btnGetJokes);
        tvResult = findViewById(R.id.tvjokes);
    }

    private void setupListeners() {
        btnGetJokes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FetchData fetchData = new FetchData();
                fetchData.execute(JOKES_ENDPOINT);
            }
        });
    }
}
