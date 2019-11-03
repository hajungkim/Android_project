package org.androidtown.probono;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

import java.util.List;

public class History extends AppCompatActivity {
    private  DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        getSupportActionBar().setTitle("요청이력");
        dbHelper = new DBHelper(
                this,
                "history",
                null,1
        );
        ListView listview;
        ListViewAdapter adapter;
        List historyList =dbHelper.getAllHistory();

        adapter = new ListViewAdapter(historyList,this);

        // 리스트뷰 참조 및 Adapter달기
        listview = (ListView) findViewById(R.id.listview1);
        listview.setAdapter(adapter);

        // 첫 번째 아이템 추가.
//        adapter.addItem("11.2", "4:20", "오후", "동작구 상도동", "광진구 화양동");
//        adapter.addItem("7.0", "8:30", "오후", "서대문구 합동", "서초구 서초동");
//        adapter.addItem("17.1", "5:10", "오후", "인천시 부평동", "부천시 소사본동");

    }
}
