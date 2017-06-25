package a_barbu.gps_agenda;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import java.io.Serializable;
import java.text.SimpleDateFormat;


public class MarkerObj implements Serializable {

    public double lat;
    public double lng;
    public String locality;
    public int model;
    public int  ID ;
    public String memo;
    public int size;
    public String title;
    public String added;
    public String last_visit;
    public String device;
    public int passed = 0;
    public int accuracy;
    public int radius;
    public int active;

    public MarkerObj(){

    }

    public MarkerObj (double lat, double lng, String loc, int r, int a, String memo, int model, int id,int pinsize,String title,String date,String device,int active){
        this.lat = lat;
        this.lng = lng;
        this.locality= loc;
        this.radius=r;
        this.accuracy=a;
        this.memo=memo;
        this.model=model;
        this.ID = id;
        this.last_visit="none";
        this.added="none";
        this.device="unknown";
        this.size=pinsize;
        this.title=title;
        this.added=date;
        this.device=device;
        this.active=active;
    }



    public int getPassed() {
        return passed;
    }

    public void setAdded(String s){this.added=s;}

    public void setPassed(int passed) {
        this.passed = passed;
    }

    public void set_visit(String v){this.last_visit = v;}


    public void setSize(int s){
        size=s;
    }

    public int getModel() {
        return model;
    }

    public void setModel(int model) {
        this.model = model;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public void setTitle(String t){
        title=t;
    }


    public void Passed(int ID){
        passed++;
    }

    public void MarkerUpdate (double lat, double lng, String loc){
        this.lat = lat;
        this.lng = lng;
        this.locality= loc;

    }
    public void ChangeMemo(String m){
        this.memo=m;
    }

    public MarkerObj ReturnModel(int mod){
        if (this.model == mod)
        return this;
        else return null;
    }


    @Override
    public String toString() {
        return this.locality + ". " + this.ID + " [$]";
    }
}

