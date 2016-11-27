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
    private TextView tvLastTime;

    // 답
    private static int LEFT_ANSWER;
    private static int RIGHT_ANSWER;

    private static Set<Multiplication> set;

    private static int TOTAL;
    private static int CORR;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        // 타이머 표시와 설정
        tvLastTime = (TextView)findViewById(R.id.textView_sec);
        timer.schedule(new GameTimerTask(), 0, 1000);

        // 게임 시작
        startGame();
    }

    // 뒤로가기 누를 때 타이머 끄고 액티비티 끄기
    @Override
    public void onBackPressed() {
        timer.cancel();
        // 매번 값을 따로 초기화 하지 않고 하는 법
        TOTAL = 0;
        CORR = 0;
        finish();
    }

    private void startGame() {
        selectNumber();
        ((TextView)findViewById(R.id.textView_pre)).setText("" + LEFT_ANSWER);
        ((TextView)findViewById(R.id.textView_post)).setText("" + RIGHT_ANSWER);

        ((TextView)findViewById(R.id.textView_num)).setText("" + CORR);
        ((TextView)findViewById(R.id.textView_num3)).setText("" + TOTAL);

        int[] button_id = {R.id.button1, R.id.button2, R.id.button3, R.id.button4, R.id.button5, R.id.button6, R.id.button7, R.id.button8, R.id.button9};
        int index = 0;
        for (Multiplication mul : set) {
            TextView textButton = ((TextView)findViewById(button_id[index]));
            textButton.setText("" + (mul.left * mul.right));
            if ((textButton.getText()).equals(String.valueOf(LEFT_ANSWER * RIGHT_ANSWER))) {
                textButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        CORR++;
                        TOTAL++;
                        startGame();
                    }
                });
            } else {
                textButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        TOTAL++;
                        startGame();
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
    private static void selectNumber() {
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
        private int seconds = 0;
        @Override
        public void run() {
            seconds++;
            if (seconds >= LIMIT) {
                timer.cancel();
                Intent intent = new Intent(GameActivity.this, ResultActivity.class);
                intent.putExtra("total", TOTAL);
                intent.putExtra("corr", CORR);
                startActivity(intent);
                TOTAL = 0;
                CORR = 0;
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
