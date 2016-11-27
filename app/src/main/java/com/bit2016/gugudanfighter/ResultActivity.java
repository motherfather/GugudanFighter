package com.bit2016.gugudanfighter;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class ResultActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        // 점수 받기
        Intent intent = getIntent();
        int total = intent.getIntExtra("total", 0);
        int corr = intent.getIntExtra("corr", 0);

        // 점수 표시
        ((TextView)findViewById(R.id.textView_score)).setText(corr + "/" + total);

        Button button_no = (Button)findViewById(R.id.button_no);
        Button button_yes = (Button)findViewById(R.id.button_yes);
        button_no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        button_yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ResultActivity.this, GameActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}
