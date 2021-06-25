package com.widget.progress.button.demo;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.widget.progress.button.ProgressTextView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final ProgressTextView textView = findViewById(R.id.progress_textView);
        textView.setCurrentProgress(0.6f);
    }

}