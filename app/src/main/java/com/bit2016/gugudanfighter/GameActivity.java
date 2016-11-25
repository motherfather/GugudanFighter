package com.bit2016.gugudanfighter;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class GameActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game2);
    }

    // 랜덤수 구하기
    private static int randomize(int from, int to) {
        return (int)(Math.random() * to) + from;
    }
}
