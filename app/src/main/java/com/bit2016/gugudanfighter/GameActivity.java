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
    private static final int LIMIT = 5; // 제한 시간 설정
    private Timer timer = new Timer(); // 타이머 선언
    private TextView tvLastTime; // 타이머 출력 선언

    // 답
    private static int LEFT_ANSWER; // 답의 왼쪽 피연산자
    private static int RIGHT_ANSWER; // 답의 오른쪽 피연산자

    private static Set<Multiplication> set; // 중복값을 막기 위한 hashset 선언

    private static int TOTAL; // 문제수
    private static int CORR; // 정답수

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        // 정답수/문제수 초기화
        TOTAL = 0;
        CORR = 0;
        // 타이머 표시와 설정
        tvLastTime = (TextView)findViewById(R.id.textView_sec);
        timer.schedule(new GameTimerTask(), 0, 1000); // 0초부터 1초마다 시간 감소를 나타내기 위해서

        // 게임 시작
        startGame(); // 처음 문제와 답을 출력
    }

    // 뒤로가기 누를 때 타이머 끄고 액티비티 끄기
    @Override
    public void onBackPressed() {
        timer.cancel();
        finish();
    }

    private void startGame() {
        // 숫자 결정
        selectNumber();
        // 문제 출력
        ((TextView)findViewById(R.id.textView_pre)).setText("" + LEFT_ANSWER);
        ((TextView)findViewById(R.id.textView_post)).setText("" + RIGHT_ANSWER);
        // 정답수/문제수 출력
        ((TextView)findViewById(R.id.textView_num)).setText("" + CORR);
        ((TextView)findViewById(R.id.textView_num3)).setText("" + TOTAL);

        // 버튼 배열화
        int[] button_id = {R.id.button1, R.id.button2, R.id.button3, R.id.button4, R.id.button5, R.id.button6, R.id.button7, R.id.button8, R.id.button9};

        int index = 0; // foreach문을 쓰게 되어서 button_id 배열에 필요한 i(인덱스)가 없어서 필요함...
        for (Multiplication mul : set) { // foreach문
            TextView textButton = (TextView)findViewById(button_id[index]); // R.id.button1 ~ button9 까지를 텍스트뷰로 만듬
            textButton.setText("" + (mul.left * mul.right)); // 텍스트뷰로 만들어서 버튼에 텍스트를 입력하는 데 Multiplication mul에서 두 개의 피연산자를 가져옴
            // 정답을 눌렀을 때...
            if ((textButton.getText()).equals(String.valueOf(LEFT_ANSWER * RIGHT_ANSWER))) {
                textButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        // 정답카운트 증가, 문제카운트 증가, 새로운 문제 생성!!
                        CORR++;
                        TOTAL++;
                        startGame();
                    }
                });
            // 오답을 눌렀을 때...
            } else {
                textButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        // 문제카운트 증가
                        TOTAL++;
                        // 새로운 문제 생성!!
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

        int indexRandom = randomize(0, 9); // hashset의 9개의 보기에서 문제를 선택하기 위해서...

        int index = 0;
        for (Multiplication mul : set) {
            // 문제 결정
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
        public boolean equals(Object o) { // 4*6, 6*4, 8*3, 3*8과 같은 같은 곱한 값이 중복되는 것을 막기 위해서...
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Multiplication that = (Multiplication) o;

            return right * left == that.right * that.left;
        }
        // 해시코드 생성
        @Override
        public int hashCode() {
            return 31 * left * right; // 3*9, 3*9 동일한 값이 여러번 들어오는 것을 막기 위해서...
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
            if (seconds >= LIMIT) { // 타이어가 끝나면...
                timer.cancel(); // 타이머 종료
                Intent intent = new Intent(GameActivity.this, ResultActivity.class);
                intent.putExtra("total", TOTAL); // 문제수와
                intent.putExtra("corr", CORR); // 정답수를 ResultActivity로 넘기기 위해서 intent에 값을 담는 과정
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
