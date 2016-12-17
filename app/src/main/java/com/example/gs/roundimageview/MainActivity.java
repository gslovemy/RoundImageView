package com.example.gs.roundimageview;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.LinearLayout;

import com.example.library.RoundImageView;

public class MainActivity extends AppCompatActivity {

    private LinearLayout ll;
    private RoundImageView roundImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ll = (LinearLayout) findViewById(R.id.ll);

        roundImageView = new RoundImageView(this);
        roundImageView.setType(1);
        roundImageView.setmBorderRadius(50);
        roundImageView.setImageResource(R.drawable.a);
        ll.addView(roundImageView);
    }
}
