package org.androidtown.probono;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class DBHelper extends SQLiteOpenHelper {
    private Context context;
    private String  name;


    public DBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        this.context = context;
        this.name=name;
    }

    //* Database가 존재하지 않을 때, 딱 한번 실행된다. * DB를 만드는 역할을 한다.

    @Override
    public void onCreate(SQLiteDatabase db) {

        if(name == "login") {
            // String 보다 StringBuffer가 Query 만들기 편하다.

            StringBuffer sb = new StringBuffer();
            sb.append(" CREATE TABLE TEST_TABLE ( ");
            sb.append(" _ID INTEGER PRIMARY KEY AUTOINCREMENT, ");
            sb.append(" NAME TEXT, ");
            sb.append(" AGE TEXT, ");
            sb.append(" PHONE TEXT ) ");

            // SQLite Database로 쿼리 실행

            db.execSQL(sb.toString());
            Toast.makeText(context, "login Table 생성완료", Toast.LENGTH_SHORT).show();
        }
        else{

            StringBuffer sb = new StringBuffer();
            sb.append(" CREATE TABLE TEST_TABLE ( ");
            sb.append(" _ID INTEGER PRIMARY KEY AUTOINCREMENT, ");
            sb.append(" DEST TEXT, ");
            sb.append(" NOW TEXT, ");;
            sb.append(" TIME TEXT, ");
            sb.append(" DISTANCE TEXT ) ");

            // SQLite Database로 쿼리 실행

            db.execSQL(sb.toString());
            Toast.makeText(context, "history Table 생성완료", Toast.LENGTH_SHORT).show();
        }

    }

//    Application의 버전이 올라가서 * Table 구조가 변경되었을 때 실행된다.
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Toast.makeText(context, "버전이 올라갔습니다.", Toast.LENGTH_SHORT).show();

    }

    public void testDB() {
        SQLiteDatabase db = getReadableDatabase();
    }

    public void addPerson(Person person){
        // 1. 쓸 수 있는 DB 객체를 가져온다.
        SQLiteDatabase db = getWritableDatabase();
        // 2. Person Data를 Insert한다.
        // _id는 자동으로 증가하기 때문에 넣지 않습니다.
        StringBuffer sb = new StringBuffer();
        sb.append(" INSERT INTO TEST_TABLE ( ");
        sb.append(" NAME, AGE, PHONE ) ");
        sb.append(" VALUES ( ?, ?, ? ) ");
        db.execSQL(sb.toString(),
                new Object[]{
                        person.getName(),
                        person.getAge(),
                        person.getPhone()});
        Toast.makeText(context, "Insert 완료", Toast.LENGTH_SHORT).show();

                }

    public Person getPersonData() {
        StringBuffer sb = new StringBuffer();
        sb.append(" SELECT _ID, NAME, AGE, PHONE FROM TEST_TABLE ");
        // 읽기 전용 DB 객체를 만든다.
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(sb.toString(), null);
//        List people = new ArrayList();
        Person person = null;
        // moveToNext 다음에 데이터가 있으면 true 없으면 false
         while( cursor.moveToNext() ) {
             person = new Person();
             person.set_id(cursor.getInt(0));
             person.setName(cursor.getString(1));
             person.setAge(cursor.getString(2));
             person.setPhone(cursor.getString(3));
         }
             return person;
    }

    public void addHistory(historyResource historyRs){
        // 1. 쓸 수 있는 DB 객체를 가져온다.
        SQLiteDatabase db = getWritableDatabase();
        // 2. Person Data를 Insert한다.
        // _id는 자동으로 증가하기 때문에 넣지 않습니다.
        StringBuffer sb = new StringBuffer();
        sb.append(" INSERT INTO TEST_TABLE ( ");
        sb.append(" DEST, NOW, TIME, DISTANCE ) ");
        sb.append(" VALUES ( ?, ?, ?, ? ) ");
        db.execSQL(sb.toString(),
                new Object[]{
                        historyRs.getDest(),
                        historyRs.getNow(),
                        historyRs.getTime(),
                        historyRs.getDistance()});
        Toast.makeText(context, "Insert 완료", Toast.LENGTH_SHORT).show();

    }



    public List getAllHistory() {
        StringBuffer sb = new StringBuffer();
        sb.append(" SELECT _ID, DEST, NOW, TIME, DISTANCE FROM TEST_TABLE ");
        // 읽기 전용 DB 객체를 만든다.
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(sb.toString(), null);
        List HistoryArray = new ArrayList();
        historyResource history = null;
        // moveToNext 다음에 데이터가 있으면 true 없으면 false
        while( cursor.moveToNext() ) {
            history = new historyResource();
            history.set_id(cursor.getInt(0));
            history.setDest(cursor.getString(1));
            history.setNow(cursor.getString(2));
            history.setTime(cursor.getString(3));
            history.setDistance(cursor.getString(4));
            HistoryArray.add(history);
        }
        return HistoryArray;
    }


 }




