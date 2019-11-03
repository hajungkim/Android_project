package org.androidtown.probono;


import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

public class Driverinfo extends AppCompatActivity{

    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driverinfo);

        TextView name = (TextView)findViewById(R.id.name);
        TextView call = (TextView)findViewById(R.id.call);
        TextView number = (TextView)findViewById(R.id.number);
        TextView kind = (TextView)findViewById(R.id.kind);
        TextView color = (TextView)findViewById(R.id.color);

        Intent refintent = this.getIntent();
        String tmp = refintent.getStringExtra("str");
        Toast.makeText(Driverinfo.this,tmp,Toast.LENGTH_SHORT).show();
        String[] splits =tmp.split(",");
        name.setText(splits[2]);
        call.setText(splits[3]);
        number.setText(splits[4]);
        kind.setText(splits[5]);
        color.setText(splits[6]);


    }
}
