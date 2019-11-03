package org.androidtown.probono;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import org.eclipse.paho.client.mqttv3.MqttException;

import java.io.UnsupportedEncodingException;

public class Finish extends AppCompatActivity {
    public static PahoMqttClient pahoMqttClient;

    private String host="211.254.212.221";
    private String topic="user";

    private  DBHelper dbHelper;
    private historyResource global;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finish);

        global=(historyResource)getApplicationContext();

        dbHelper = new DBHelper(
                this,
                "history",
                null,1
        );

        pahoMqttClient = new PahoMqttClient(getApplicationContext(),host, topic,1,"gggpps",true,null);
//
//        Button btn4 = (Button) findViewById(R.id.button4);
//        btn4.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Finish.super.onBackPressed();
//            }
//        });

        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                this);

        // 제목셋팅
        alertDialogBuilder.setTitle("운행 완료");

        // AlertDialog 셋팅
        alertDialogBuilder
                .setMessage("목적지에 도착 하였습니까?")
                .setCancelable(false)
                .setPositiveButton("아니요",
                        new DialogInterface.OnClickListener() {
                            public void onClick(
                                    DialogInterface dialog, int id) {
                                Finish.super.onBackPressed();
                            }
                        })
                .setNegativeButton("네",
                        new DialogInterface.OnClickListener() {
                            public void onClick(
                                    DialogInterface dialog, int id) {

                                try {
                                    pahoMqttClient.publishMessage(MainActivity.myNumber+",4", 1, topic);
                                } catch (MqttException e) {
                                    e.printStackTrace();
                                } catch (UnsupportedEncodingException e) {
                                    e.printStackTrace();
                                }

                            dbHelper.addHistory(global);

                            }
                        });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }
}
