package wieniacy.w.kaloszach.poznaj2.models;


import java.util.ArrayList;

/**
 * Created by Robin on 16.03.2016.
 */
public class Friend {
    public int id;
    public String full_name;
    public String description;

    public Friend(int id, String name, String description) {
        this.id = id;
        this.full_name = name;
        this.description = description;

    }

    @Override
    public String toString() {
        return this.full_name + " " + this.description ;
    }

    public static class List extends ArrayList<Friend> {
    }
}
