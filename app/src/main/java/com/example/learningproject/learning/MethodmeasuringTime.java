package com.example.learningproject.learning;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.SystemClock;
import android.widget.TextView;

import com.example.learningproject.R;

public class MethodmeasuringTime extends AppCompatActivity {

    TextView textView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_methodmeasuring_time);
        textView = findViewById(R.id.textView);
        String name = getCompanyName("Priya", "Dharshini");
        textView.setText(name);
    }
    @DebugLog
    public String getCompanyName(String first, String last) {
        SystemClock.sleep(1); // Some time taking process
        return first + " " + last;
    }
}