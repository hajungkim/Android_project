package org.androidtown.probono;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallbackExtended;
import org.eclipse.paho.client.mqttv3.MqttMessage;

public class Request extends AppCompatActivity {
    private MqttAndroidClient client;
    private PahoMqttClient pahoMqttClient;

    private String host="211.254.212.221";
    private String topic="user";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request);




        pahoMqttClient = MainActivity.pahoMqttClient;
//        pahoMqttClient = new PahoMqttClient(getApplicationContext(),host, topic,1,"subClient",true,null);
        pahoMqttClient.setCallback(new MqttCallbackExtended() {
            @Override
            public void connectComplete(boolean b, String s) {

            }

            @Override
            public void connectionLost(Throwable throwable) {

            }

            @Override
            public void messageArrived(String s, MqttMessage mqttMessage) {
                String tmp = new String(mqttMessage.getPayload());
                Log.e("MQTT","MSG2:"+tmp+"  s : "+s  );
//                Toast.makeText(Request.this,tmp,Toast.LENGTH_SHORT).show();
                String[] splits =tmp.split(",");
                if(splits[0].equals(MainActivity.myNumber)) {

                    if (splits[1].equals("2")) {
                        Intent intent = new Intent(getApplicationContext(), Driverinfo.class);
                        intent.putExtra("str", tmp);
//                        intent.setFlags( Intent.FLAG_ACTIVITY_NO_HISTORY);
                        startActivity(intent);
                    }
                    if (splits[1].equals("3")) {

                        Intent intent = new Intent(getApplicationContext(), Finish.class);
                        startActivity(intent);

                    }
                }
            }

            @Override
            public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {

            }
        });
    }

}


