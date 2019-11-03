package org.androidtown.probono;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class ListViewItem2 {

    private String titleStr ;
    private String dayStr ;

    public void setTitleStr(String titleStr) {
        this.titleStr = titleStr;
    }
    public void setDayStr(String dayStr) {
        this.dayStr = dayStr;
    }

    public String getTitleStr() {
        return this.titleStr;
    }

    public String getDayStr() {
        return this.dayStr;
    }

}