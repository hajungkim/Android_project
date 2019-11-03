package org.androidtown.probono;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;

import com.skt.Tmap.TMapGpsManager;

public class Intro extends Activity {

    DBHelper dbHelper;
    SQLiteDatabase database ;

    String sql;
    Cursor cursor;

    Handler handler = new Handler();
    Runnable r = new Runnable() {
        @Override
        public void run() {
            dbHelper = new DBHelper(
                    Intro.this,
                    "login",
                    null,1
            );
            database = dbHelper.getWritableDatabase();
            sql = "SELECT * FROM TEST_TABLE ";
            cursor = database.rawQuery(sql, null);
            if(cursor.getCount() != 1) {
                Intent intent = new Intent(getApplicationContext(), Infor1Activity.class);
                intent.setFlags( Intent.FLAG_ACTIVITY_NO_HISTORY);
                startActivity(intent);
                finish();
            }
            else{
                Intent intent = new Intent(getApplicationContext(), Main2Activity.class);
//                intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                startActivity(intent);
                finish();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.intro);
    } // end of onCreate

    @Override
    protected void onResume() {
        super.onResume();
// 다시 화면에 들어어왔을 때 예약 걸어주기
        handler.postDelayed(r, 2000);
    }

    @Override
    protected void onPause() {
        super.onPause();
// 화면을 벗어나면, handler 에 예약해놓은 작업을 취소하자
        handler.removeCallbacks(r); // 예약 취소
    }
}
