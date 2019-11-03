package org.androidtown.probono;

import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

public class Inquire extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inquire);
        getSupportActionBar().setTitle("자주 묻는 질문");

        ListView listview;
        ListViewAdapter2 adapter;

        adapter = new ListViewAdapter2();

        // 리스트뷰 참조 및 Adapter달기
        listview = (ListView) findViewById(R.id.listview2);
        listview.setAdapter(adapter);

        // 첫 번째 아이템 추가.
        adapter.addItem("자주 묻는 질문 입니다.", "2018.08.01");
        adapter.addItem("자주 묻는 질문 입니다.", "2018.08.01");
        adapter.addItem("자주 묻는 질문 입니다.", "2018.08.01");
        adapter.addItem("자주 묻는 질문 입니다.", "2018.08.01");
        adapter.addItem("자주 묻는 질문 입니다.", "2018.08.01");
        adapter.addItem("자주 묻는 질문 입니다.", "2018.08.01");
        adapter.addItem("자주 묻는 질문 입니다.", "2018.08.01");
        adapter.addItem("자주 묻는 질문 입니다.", "2018.08.01");
        adapter.addItem("자주 묻는 질문 입니다.", "2018.08.01");
        adapter.addItem("자주 묻는 질문 입니다.", "2018.08.01");
    }
}
