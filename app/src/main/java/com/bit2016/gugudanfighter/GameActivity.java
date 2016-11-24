package com.bit2016.gugudanfighter;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import java.util.HashSet;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

public class GameActivity extends AppCompatActivity {
    private static final int LIMIT = 3;
    private Timer timer = new Timer();
    private static int SECONDS = 0;
    private TextView tvLastTime;
    private static int CORR = 0;
    private static int TOTAL = 0;
    private static Set<Integer> hash = new HashSet<Integer>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        tvLastTime = (TextView)findViewById(R.id.textView_sec);
        timer.schedule(new GameTimerTask(), 0, 1000);

        question();

        // 정답수/문제수
        TextView text = (TextView)findViewById(R.id.textView_num);
        text.setText(String.valueOf(CORR));
        TextView text2 = (TextView)findViewById(R.id.textView_num3);
        text2.setText(String.valueOf(TOTAL));
    }

    private void updateLastTime(int seconds) {
        tvLastTime.setText("" + (LIMIT - seconds));
    }

    private void example() {
        for (int i = 0; i < 8; i++) { // 보기에 쓸 8개의 숫자
            int pre = (int) (Math.random() * 8) + 2; // 앞 피연산자
            int post = (int) (Math.random() * 9) + 1; // 뒤 피연산자
            int answer = pre * post; // 답
            if (hash.contains(answer)) { // 똑같은 숫자가 존재할 경우 추가 하지 않음
                i--;
            } else { // 똑같은 숫자가 존재 하지 않을 경우 그 숫자 추가!!
                hash.add(answer);
            }
        }
    }
    // 구구단문제
    private void question() {
        example();

        int pre = 0; // 진정한 답의 앞 피연산자
        int post = 0; // 진정한 답의 뒤 피연산자
        int answer = 0; // 진정한 답

        // 문제생성,답 hashset에 추가
        while (true) {
            pre = (int) (Math.random() * 8) + 2;
            post = (int) (Math.random() * 9) + 1;
            answer = pre * post;

            if (!hash.contains(answer)) {
                hash.add(answer);
                break;
            }
        }

        // 문제 올리기
        TextView textView = (TextView) findViewById(R.id.textView_pre);
        textView.setText(String.valueOf(pre));
        TextView textView2 = (TextView) findViewById(R.id.textView_post);
        textView2.setText(String.valueOf(post));

        // 버튼에 랜덤으로 넣기 위한 방법...1
        int[] fin = new int[9];
        while (hash.iterator().hasNext()) {
            int num = (int) (Math.random() * 9); // 배열 순서 정해주는 인덱스
            if (fin[num] == 0) {
                fin[num] = hash.iterator().next();
                hash.remove(hash.iterator().next());
            }
        }

        // 버튼에 랜덤으로 넣기 위한 방법...2
        int[] button_ids = {R.id.button1, R.id.button2, R.id.button3, R.id.button4, R.id.button5, R.id.button6, R.id.button7, R.id.button8, R.id.button9};

        // 버튼에 랜덤값 넣어주기
        for (int i = 0; i < 9; i++) {
            TextView textView3 = (TextView) findViewById(button_ids[i]);
            textView3.setText(String.valueOf(fin[i]));

            // 정답 누를 때
            if (textView3.getText() == String.valueOf(answer)) {
                textView3.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        CORR++;
                        TOTAL++;
                    }
                });
                // 오답 누를 때
            } else {
                textView3.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        TOTAL++;
                    }
                });
            }
        }
    }


    private class GameTimerTask extends TimerTask {
        @Override
        public void run() {
            SECONDS++;
            if (SECONDS >= LIMIT) {
                timer.cancel();
                Intent intent = new Intent(GameActivity.this, ResultActivity.class);
                startActivity(intent);
                finish();
            }
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    updateLastTime(SECONDS);
                }
            });
        }
    }
}
