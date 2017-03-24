package com.dream.dreamview;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        RelativeLayout vie = (RelativeLayout) findViewById(R.id.layout);
        LayoutInflater inflater = LayoutInflater.from(this);
        View view = inflater.inflate(R.layout.button_layout, null);
        vie.addView(view);
    }

}
