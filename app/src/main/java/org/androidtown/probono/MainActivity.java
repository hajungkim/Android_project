package org.androidtown.probono;

import android.Manifest;
import android.app.Application;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.skt.Tmap.TMapData;
import com.skt.Tmap.TMapGpsManager;
import com.skt.Tmap.TMapLabelInfo;
import com.skt.Tmap.TMapMarkerItem;
import com.skt.Tmap.TMapPOIItem;
import com.skt.Tmap.TMapPoint;
import com.skt.Tmap.TMapPolyLine;
import com.skt.Tmap.TMapView;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.MqttCallbackExtended;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.net.URL;
import java.util.Date;
import java.text.SimpleDateFormat;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,TMapGpsManager.onLocationChangedCallback {
    private static final String TAG = "MainActivity";
    private final String TMAP_API_KEY = "2061bf08-f7fe-4b20-97fc-807bb7eaaad7";
    TMapView tmap;
    boolean trackingmode = true;
    TMapPoint point;
    TMapGpsManager gps;
    private TMapData tmapdata = new TMapData();
    TextView txt1;
    boolean ea = false;
    private String now;

    private MqttAndroidClient client;
    public static PahoMqttClient pahoMqttClient;
    public static String myNumber = "01049230847";

    private String host="211.254.212.221";
    private String topic="user";

    double distance = 83;
    String dest = "tmp";

    private static final int TIME_INTERVAL = 2000; // # milliseconds, desired time passed between two back presses.
    private long mBackPressed;

    long mNow;
    Date mDate;
//    SimpleDateFormat mFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
    SimpleDateFormat mFormat = new SimpleDateFormat("yy/MM/dd \n a hh:mm");


    private  DBHelper dbHelper;

    private historyResource global;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        dbHelper = new DBHelper(
                this,
                "login",
                null,1
        );
        final Person UserData =  dbHelper.getPersonData();

        global=(historyResource)getApplicationContext();

        Intent refintent = this.getIntent();
        final String kind = refintent.getStringExtra("kind");

        pahoMqttClient = new PahoMqttClient(getApplicationContext(),host, topic,1,"gggpps",true,null);


//        pahoMqttClient.setCallback(new MqttCallbackExtended() {
//            @Override
//            public void connectComplete(boolean b, String s) {
//
//            }
//
//            @Override
//            public void connectionLost(Throwable throwable) {
//
//            }
//
//            @Override
//            public void messageArrived(String s, MqttMessage mqttMessage) throws Exception {
//                Log.e("MQTT","msg:"+new String(mqttMessage.getPayload())   );
//            }
//
//            @Override
//            public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {
//
//            }
//        });
//        client = pahoMqttClient.getMqttClient(getApplicationContext(), Constants.MQTT_BROKER_URL, Constants.CLIENT_ID);


        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1); //위치권한 탐색 허용 관련 내용
            }
            return;
        }

        Button btn1 = (Button) findViewById(R.id.button1);
        Button btn2 = (Button) findViewById(R.id.button2);
        Button btn3 = (Button) findViewById(R.id.button3);
        txt1 = (TextView) findViewById(R.id.Text1);



        LinearLayout linearLayoutTmap = (LinearLayout) findViewById(R.id.linearLayoutTmap);
        tmap = new TMapView(this);
        tmap.setSKTMapApiKey(TMAP_API_KEY);
        linearLayoutTmap.addView(tmap);

        tmap.setIconVisibility(true);//현재위치로 표시될 아이콘을 표시할지 여부를 설정합니다.

        gps = new TMapGpsManager(this);
        gps.setMinTime(1);
        gps.setMinDistance(1);
        gps.setProvider(gps.NETWORK_PROVIDER);
        gps.OpenGps();
        tmap.setTrackingMode(true);

        final Bitmap bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.icon);


        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("통합검색");

                final EditText input = new EditText(MainActivity.this);
                builder.setView(input);

                builder.setPositiveButton("검색", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        final String strData = input.getText().toString();
                        tmap.removeAllMarkerItem();
                        tmap.removeAllTMapPolyLine();
                        tmapdata.findAroundKeywordPOI(gps.getLocation(), strData, 100, 10, new TMapData.FindAroundKeywordPOIListenerCallback() {
                            @Override
                            public void onFindAroundKeywordPOI(ArrayList<TMapPOIItem> poiItem) {
                                if (poiItem.size() > 0) {
                                    ea = true;
                                    for (int i = 0; i < poiItem.size(); i++) {

                                        TMapPOIItem item = (TMapPOIItem) poiItem.get(i);
                                        double lat = Double.parseDouble(String.valueOf(item.noorLat));
                                        double lon = Double.parseDouble(String.valueOf(item.noorLon));

                                        tmap.setCenterPoint(lon,lat);

                                        TMapPoint tpoint = new TMapPoint(lat, lon);

                                        TMapMarkerItem tItem = new TMapMarkerItem();
                                        tItem.setCalloutRightButtonImage(bitmap);
                                        tItem.setTMapPoint(tpoint);
                                        tItem.setName(item.getPOIName());
                                        tItem.setVisible(TMapMarkerItem.VISIBLE);
                                        Bitmap src = null;
                                        try {
                                            String URL = "http://www.example.com/image.jpg";
                                            InputStream in = new URL(URL).openStream();
                                            src = BitmapFactory.decodeStream(in);
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                        tItem.setIcon(src);

                                        // 핀모양으로 된 마커를 사용할 경우 마커 중심을 하단 핀 끝으로 설정.
                                        tItem.setPosition(1, 1);         // 마커의 중심점을 하단, 중앙으로 설정
                                        tItem.setCanShowCallout(true);
                                        tItem.setCalloutTitle(item.getPOIName().toString());
//                                        tItem.setEnableClustering(true);
//                                tItem.setCalloutSubTitle(item.getPOIAddress().replace("null", ""));
//                                tItem.setAutoCalloutVisible(true);
                                        tmap.addMarkerItem("test" + i, tItem);

                                        Log.d("POI Name: ", item.getPOIName().toString() + ", " +
                                                "Address: " + item.getPOIAddress().replace("null", "") + ", " +
                                                "Point: " + item.getPOIPoint().toString() + " gps " + String.valueOf(item.frontLat) + " gps2 " + String.valueOf(item.noorLat));
                                    }
                                } else
                                    ea = false;
                            }


                        });

                    }
                }).setNegativeButton("취소", null);
                builder.show();
            }
        });

        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tmap.removeAllMarkerItem();
                tmap.removeAllTMapPolyLine();
                ea=false;
                txt1.setText(null);
                dest = "tmp";
                tmap.setCenterPoint(gps.getLocation().getLongitude(),gps.getLocation().getLatitude(),true);
            }
        });

        btn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tmapdata.convertGpsToAddress(gps.getLocation().getLatitude(), gps.getLocation().getLongitude(), new TMapData.ConvertGPSToAddressListenerCallback() {
                    @Override
                    public void onConvertToGPSToAddress(String s) {
                        String time = getTime();
                        now = s;
                        Log.d("HHelp", "now==onConvertToGPSToAddress: success //  s="+s+"now ="+now);
//                        distance=(int)(distance*0.001)*100;

//                        String msg = myNumber+",1,"+now+","+dest+","+UserData.getName()+","+kind+", "+(int)distance+"m"+","+time;
                        String msg = myNumber+",1,"+now+","+dest+","+UserData.getName()+","+kind+","+distance+"km"+","+time;
                        if (dest != "tmp") {
                            try {
                                pahoMqttClient.publishMessage(msg, 1, topic);
                            } catch (MqttException e) {
                                e.printStackTrace();
                            } catch (UnsupportedEncodingException e) {
                                e.printStackTrace();
                            }
                            global.setDistance(String.valueOf(distance));
                            global.setNow(now);
                            global.setTime(time);
                            global.setDest(dest);
                            Intent intent = new Intent(MainActivity.this,Request.class);
                            intent.setFlags( Intent.FLAG_ACTIVITY_NO_HISTORY);
                            startActivity(intent);
                        }
                        else {
                            new Thread() {
                                public void run(){

//                                    Toast.makeText(MainActivity.this,  "목적지가 없습니다", Toast.LENGTH_SHORT).show();
                                    Log.d("HHelp", "목적지가 없습니다");
                                }
                            }.start();
//                            Toast.makeText(MainActivity.this,  "목적지가 없습니다", Toast.LENGTH_SHORT).show();
//                            Log.d("HHelp", "목적지가 없습니다");
                        }
                    }
                });
//                String msg = "{1,"+now+","+dest+",공필상,100원택시}";
//                if (!msg.isEmpty()) {
//                    try {
//                        pahoMqttClient.publishMessage(msg, 1, topic);
//                    } catch (MqttException e) {
//                        e.printStackTrace();
//                    } catch (UnsupportedEncodingException e) {
//                        e.printStackTrace();
//                    }
//                }
//                Intent intent = new Intent(v.getContext(),Request.class);
//                startActivity(intent);
            }
        });


        tmap.setOnClickReverseLabelListener(new TMapView.OnClickReverseLabelListenerCallback() {
            @Override
            public void onClickReverseLabelEvent(TMapLabelInfo tMapLabelInfo) {
                if (!ea) {
                    // TODO Auto-generated method stub
                    if (tMapLabelInfo != null) {
                        double lat = Double.valueOf(tMapLabelInfo.labelLat);
                        double lon = Double.valueOf(tMapLabelInfo.labelLon);
                        //  String name = tMapLabelInfo.labelName;
                        TMapMarkerItem marker1 = new TMapMarkerItem();
                        marker1.setName(tMapLabelInfo.labelName);
                        marker1.setVisible(TMapMarkerItem.VISIBLE);
                        marker1.setCalloutRightButtonImage(bitmap);
                        marker1.setCanShowCallout(true);
                        marker1.setCalloutTitle(tMapLabelInfo.labelName.toString());
//                        marker1.setEnableClustering(true);
                        marker1.setID("reverseLabelID");
                        marker1.setTMapPoint(new TMapPoint(lat, lon));
                        tmap.addMarkerItem("reverseLabelID", marker1);
                    }
                }
            }
        });

        tmap.setOnCalloutRightButtonClickListener(new TMapView.OnCalloutRightButtonClickCallback() {

            @Override
            public void onCalloutRightButton(final TMapMarkerItem tMapMarkerItem) {
                txt1.setText(tMapMarkerItem.getCalloutTitle());
                dest=(tMapMarkerItem.getCalloutTitle());
                    tmapdata.convertGpsToAddress(tMapMarkerItem.latitude, tMapMarkerItem.longitude, new TMapData.ConvertGPSToAddressListenerCallback() {
                        @Override
                        public void onConvertToGPSToAddress(String s) {
//                            dest = s;
                            Log.d("HHelp", "dest==onConvertToGPSToAddress: success///s = "+s+"  dest = "+ dest);
                            dest = s;
                            txt1.setText(dest);
                        }
                    });


                new Thread() {
                    public void run(){

                        TMapPoint tMapPointStart = new TMapPoint(gps.getLocation().getLatitude(), gps.getLocation().getLongitude()); // SKT타워(출발지)
                        TMapPoint tMapPointEnd = new TMapPoint(tMapMarkerItem.latitude, tMapMarkerItem.longitude); // N서울타워(목적지)

                        try {
                            TMapPolyLine tMapPolyLine = new TMapData().findPathData(tMapPointStart, tMapPointEnd);
                            tMapPolyLine.setLineColor(Color.BLUE);
                            tMapPolyLine.setLineWidth(2);
                            tmap.addTMapPolyLine("Line1", tMapPolyLine);
                            distance = tMapPolyLine.getDistance()/1000.0;
                            distance = Math.round(distance*100)/100.0 ;
                        }catch(Exception e) {
                            e.printStackTrace();
                        }
                    }
                }.start();

//                Toast.makeText(MainActivity.this, "주소 : "+address +" name : "+tMapMarkerItem.getName() + "distance : "+ distance, Toast.LENGTH_SHORT).show();
//                Toast.makeText(MainActivity.this,  " 까지거리: "+ (int)distance+"m", Toast.LENGTH_SHORT).show();
//                txt1.setText(dest);
            }
        });

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

      //  Intent intent = new Intent(MainActivity.this, MqttMessageService.class);
      //  startService(intent);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
//            finish();
//            moveTaskToBack(true);
            super.onBackPressed();
//            if (mBackPressed + TIME_INTERVAL > System.currentTimeMillis())
//            {
//                super.onBackPressed();
//                return;
//            }
//            else { Toast.makeText(getBaseContext(), "한번 더 누르면 종료됩니다.", Toast.LENGTH_SHORT).show(); }
//
//            mBackPressed = System.currentTimeMillis();
        }
    }

    public void setActionBarTitle(String title){
        getSupportActionBar().setTitle(title);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

//        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        FragmentManager manager = getFragmentManager();
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_request) {

        } else if (id == R.id.nav_history) {
            Intent intent = new Intent(MainActivity.this,History.class);
            startActivity(intent);
        } else if (id == R.id.nav_information) {
            Intent intent = new Intent(MainActivity.this,Information.class);
            startActivity(intent);
        } else if (id == R.id.nav_inquire) {
            Intent intent = new Intent(MainActivity.this,MainInqireActivity.class);
            startActivity(intent);
        }
        else if (id == R.id.nav_logout) {
            Intent intent = new Intent(MainActivity.this,Logout.class);
            startActivity(intent);
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    private void requestMyLocation() {
        LocationManager manager =
                (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        try {
            long minTime = 10000;
            float minDistance = 0;
            manager.requestLocationUpdates(
                    LocationManager.GPS_PROVIDER,
                    minTime,
                    minDistance,
                    new LocationListener() {
                        @Override
                        public void onLocationChanged(Location location) {
//                            showCurrentLocation(location);
                        }

                        @Override
                        public void onStatusChanged(String provider, int status, Bundle extras) {

                        }

                        @Override
                        public void onProviderEnabled(String provider) {

                        }

                        @Override
                        public void onProviderDisabled(String provider) {

                        }
                    }
            );

//            Location lastLocation = manager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
//            if (lastLocation != null) {
//                showCurrentLocation(lastLocation);
//            }



        } catch (SecurityException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onLocationChange(Location location) {
        if (trackingmode) {
            tmap.setLocationPoint(location.getLongitude(), location.getLatitude());
        }
    }
//
//    private void showCurrentLocation(Location location) {
//        LatLng curPoint = new LatLng(location.getLatitude(), location.getLongitude());
//
//        map.animateCamera(CameraUpdateFactory.newLatLngZoom(curPoint, 16));
//        //      showMyLocationMarker(location);
//    }


//    private void showMyLocationMarker(Location location) {
//        if (myLocationMarker == null) {
//            myLocationMarker = new MarkerOptions();
//            myLocationMarker.position(new LatLng(location.getLatitude(), location.getLongitude()));
//            myLocationMarker.title("● 내 위치\n");
//            myLocationMarker.snippet("● GPS로 확인한 위치");
//            myLocationMarker.icon(BitmapDescriptorFactory.fromResource(R.drawable.mylocation));
//            map.addMarker(myLocationMarker);
//        } else {
//            myLocationMarker.position(new LatLng(location.getLatitude(), location.getLongitude()));
//        }
//    }

    @Override
    protected void onDestroy() {
        try {
            pahoMqttClient.disconnect();
            Log.d(TAG, "onDestroysuccess: ");
        } catch (MqttException e) {
            e.printStackTrace();
        }
        super.onDestroy();

    }

    private String getTime(){
        mNow = System.currentTimeMillis();
        mDate = new Date(mNow);
        return mFormat.format(mDate);
    }

}

