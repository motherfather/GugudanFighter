package com.bit2016.gugudanfighter;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;

public class GameActivity extends AppCompatActivity {
    private static final int LIMIT = 5;
    private Timer timer = new Timer();
    private static int seconds = 0;
    private TextView tvLastTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        tvLastTime = (TextView)findViewById(R.id.textView_sec);
        timer.schedule(new GameTimerTask(), 0, 1000);
    }

    // 랜덤수 구하기
    private static int randomize(int from, int to) {
        return (int)(Math.random() * to) + from;
    }

    // 줄어드는 시간 보이기
    private void updateLastTime(int seconds) {
        tvLastTime.setText("" + (LIMIT - seconds));
    }

    // 타이머
    private class GameTimerTask extends TimerTask {
        @Override
        public void run() {
            seconds++;
            if (seconds >= LIMIT) {
                timer.cancel();
//                Intent intent = new Intent(GameActivity.this, ResultActivity.class);
//                startActivity(intent);
                finish();
            }
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    updateLastTime(seconds);
                }
            });
        }
    }
}
