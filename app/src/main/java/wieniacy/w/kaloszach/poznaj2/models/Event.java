package wieniacy.w.kaloszach.poznaj2.models;

import java.util.ArrayList;

/**
 * Created by Robin on 05.04.2016.
 */
public class Event {
    public int id;
    public String name;
    public String place;
    public String about;
    public String date;
    //public String month;
    public String hour;

    public Double getWid() {
        return wid;
    }

    public void setWid(Double wid) {
        this.wid = wid;
    }

    public Double getLen() {
        return len;
    }

    public void setLen(Double len) {
        this.len = len;
    }

    public Double len;
    public Double wid;
    //public String descryption;


    public Event(int id, String name,  String des, String startdate , String starttime){//String month, String hour, String place, String about) {
        this.id = id;
        this.name = name;
        //this.descryption =  des;
        //this.month = null;
        this.hour = starttime;
        this.date = startdate;
        this.place = null;
        this.about = des;
        this.len = Double.valueOf(0);
        this.wid = Double.valueOf(0);

    }

    @Override
    public String toString() {
        return "Tytuł: " + this.name + " "  + this.about + " Data: " + this.date + " Czas: " + this.hour;
    }
    public String placetoString(){
        return this.about;
    }
    public static class List extends ArrayList<Event> {
    }}