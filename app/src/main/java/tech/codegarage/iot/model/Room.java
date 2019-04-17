package tech.codegarage.iot.model;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * @author Md. Rashadul Alam
 * Email: rashed.droid@gmail.com
 */
public class Room extends RealmObject {

    @PrimaryKey
    private int id = -1;
    private String name = "";
    private String image = "";
    private boolean isSelected = false;

    public Room() {
    }

    public Room(int id, String name, String image) {
        this.id = id;
        this.name = name;
        this.image = image;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    @Override
    public String toString() {
        return "{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", image='" + image + '\'' +
                ", isSelected=" + isSelected +
                '}';
    }
}