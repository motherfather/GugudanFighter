package com.bit2016.gugudanfighter;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.HashSet;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

public class GameActivity extends AppCompatActivity {
    private static final int LIMIT = 5;
    private Timer timer = new Timer();
    private static int seconds = 0;
    private TextView tvLastTime;

    // 답
    private static int LEFT_ANSWER;
    private static int RIGHT_ANSWER;

    private static Set<Multiplication> set;

    private static int TOTAL = 0;
    private static int CORR = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        tvLastTime = (TextView)findViewById(R.id.textView_sec); // 타이머 나타내는 것 선언
        timer.schedule(new GameTimerTask(), 0, 1000); // 타이머 발동
        createQ();
    }

    private void createQ() {
        SelectNumber();
        ((TextView)findViewById(R.id.textView_pre)).setText("" + LEFT_ANSWER);
        ((TextView)findViewById(R.id.textView_post)).setText("" + RIGHT_ANSWER);

        ((TextView)findViewById(R.id.textView_num)).setText("" + CORR);
        ((TextView)findViewById(R.id.textView_num3)).setText("" + TOTAL);

        int[] button_id = {R.id.button1, R.id.button2, R.id.button3, R.id.button4, R.id.button5, R.id.button6, R.id.button7, R.id.button8, R.id.button9};
        int index = 0;
        for (Multiplication mul : set) {
            ((TextView)findViewById(button_id[index])).setText("" + (mul.left * mul.right));
            if (((TextView)findViewById(button_id[index])).getText() == String.valueOf(LEFT_ANSWER * RIGHT_ANSWER)) {
                ((TextView)findViewById(button_id[index])).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        CORR++;
                        TOTAL++;
                    }
                });
            } else {
                ((TextView)findViewById(button_id[index])).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        TOTAL++;
                    }
                });
            }
            index++;
        }
    }

    // 랜덤수 생성에 사용
    private static int randomize(int from, int to) {
        return (int)(Math.random() * to) + from;
    }

    // 8개의 오답과 1개의 정답 생성
    private static void SelectNumber() {
        // 보기를 중복없이 넣기 위해서 hashset 사용
        set = new HashSet<Multiplication>();
        while (set.size() != 9) { // 보기 9개 선택
            int left = randomize(2, 8); // 앞쪽 피연산자
            int right = randomize(1, 9); // 뒷쪽 피연산자

            set.add(new Multiplication(left, right));
        }

        int indexRandom = randomize(0, 9); // hashset의 9개의 보기에서 문제 선택

        int index = 0;
        for (Multiplication mul : set) {
            if (index == indexRandom) {
                LEFT_ANSWER = mul.left;
                RIGHT_ANSWER = mul.right;
            }
            index++;
        }

    }
    // 중복값이 들어가지 않게 하기 위해서, 숫자 생성용
    private static class Multiplication {
        private  int left;
        private  int right;

        public Multiplication(int left, int right) {
            this.left = left;
            this.right = right;
        }

        @Override
        public String toString() {
            return "Multiplication{" +
                    "left=" + left +
                    ", right=" + right +
                    ", product=" + right*left +
                    '}';
        }
        // 결과값이 같을 때 중복이 되지 않도록 하기 위해서
        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Multiplication that = (Multiplication) o;

            return right * left == that.right * that.left;
        }
        // 해시코드 생성
        @Override
        public int hashCode() {
            return 31 * (left * right);
        }
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
                Intent intent = new Intent(GameActivity.this, MainActivity.class);
                startActivity(intent);
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
