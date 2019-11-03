package org.androidtown.probono;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;

public class Information extends AppCompatActivity {

    DBHelper dbHelper = new DBHelper(
            this,
            "login",
            null,1
    );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_information);
        getSupportActionBar().setTitle("내정보");


        TextView tvName = findViewById(R.id.inforName);
        TextView tvAge = findViewById(R.id.inforAge);
        TextView tvPhone = findViewById(R.id.inforPhone);

        Person UserData =  dbHelper.getPersonData();

        tvName.setText(UserData.getName());
        tvAge.setText(UserData.getAge());
        tvPhone.setText(UserData.getPhone());



    }
}
