package org.androidtown.probono;

import android.app.Application;

public class historyResource extends Application {
    private int _id;
    private String dest;
    private String now;
    private String distance;
    private String time;



    @Override
    public void onCreate() {
        //전역 변수 초기화
        dest = "null";
        super.onCreate();
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
    }

    public void setDest(String dest){
        this.dest = dest;
    }

    public String getDest(){
        return dest;
    }

    public String getNow() {
        return now;
    }

    public void setNow(String now) {
        this.now = now;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
    public int get_id() { return _id; }
    public void set_id(int _id) { this._id = _id; }

}
