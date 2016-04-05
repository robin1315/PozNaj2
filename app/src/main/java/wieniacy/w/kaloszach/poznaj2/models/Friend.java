package wieniacy.w.kaloszach.poznaj2.models;


import java.util.ArrayList;

/**
 * Created by Robin on 16.03.2016.
 */
public class Friend {
    public int id;
    public String name;
    public String surname;

    public Friend(int id, String name, String surname) {
        this.id = id;
        this.name = name;
        this.surname = surname;

    }

    @Override
    public String toString() {
        return this.id + ". " + this.name + this.surname ;
    }

    public static class List extends ArrayList<Friend> {
    }
}
