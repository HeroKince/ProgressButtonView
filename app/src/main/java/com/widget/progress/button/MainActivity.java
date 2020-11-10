package com.widget.progress.button;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

/**
 * https://github.com/livesun/GradientTextView
 * https://github.com/beiliubei/ProgressTextView
 * https://medium.com/@kanekishiba/text-blending-on-android-bb21ab1cf8aa
 */
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final ProgressTextView textView = findViewById(R.id.progress_textView);
        textView.setCurrentProgress(0.6f);
    }
}