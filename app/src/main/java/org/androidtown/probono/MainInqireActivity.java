package org.androidtown.probono;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainInqireActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_inqire);

        //자주 묻는 질문 버튼
        Button button = (Button)findViewById(R.id.Inquire1);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(),Inquire.class);
                startActivity(intent);
            }
        });

        //1:1 문의 버튼
        Button button2 = (Button)findViewById(R.id.Inquire2);

        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent2 = new Intent(v.getContext(),OneToOne.class);
                startActivity(intent2);
            }
        });
    }
}
