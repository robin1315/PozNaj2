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
    public String day;
    public String month;
    public String hour;


    public Event(int id, String name, String day, String month, String hour, String place, String about) {
        this.id = id;
        this.name = name;
        this.day =  day;
        this.month = month;
        this.hour = hour;
        this.place = place;
        this.about = about;

    }

    @Override
    public String toString() {
        return this.name + " " + this.place + " " +this.day + " " + this.month + " " + this.hour + " " + this.about;
    }
    public String placetoString(){
        return this.about;
    }
    public String DatatoString(){
        return this.day + " " + this.month + " " + this.hour;
    }

    public static class List extends ArrayList<Event> {
    }}