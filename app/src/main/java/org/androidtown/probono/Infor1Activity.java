package org.androidtown.probono;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class Infor1Activity extends AppCompatActivity {

    private  DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_infor1);

        Button button = (Button)findViewById(R.id.NextButton);
        dbHelper = new DBHelper(
                this,
                "login",
                null,1
        );

        final EditText etName = (EditText)findViewById(R.id.name);
        final EditText etAge = (EditText)findViewById(R.id.age);
        final EditText etPhone = (EditText)findViewById(R.id.phone);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = etName.getText().toString();
                String age = etAge.getText().toString();
                String phone = etPhone.getText().toString();
                Person person = new Person();
                person.setName(name);
                person.setAge(age);
                person.setPhone(phone);

                dbHelper.addPerson(person);
                Intent intent = new Intent(v.getContext(),Main2Activity.class);
                intent.setFlags( Intent.FLAG_ACTIVITY_NO_HISTORY);
//                intent.setFlags( Intent.FLAG_ACTIVITY_CLEAR_TOP );
//                intent.setFlags( Intent.FLAG_ACTIVITY_NO_HISTORY);
                startActivity(intent);
            }
        });
    }

}
