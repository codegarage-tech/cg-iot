package tech.codegarage.iot.model;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * @author Md. Rashadul Alam
 * Email: rashed.droid@gmail.com
 */
public class Room extends RealmObject {

    @PrimaryKey
    private String name = "";
    private String image = "";
    private boolean isSelected = false;

    public Room() {
    }

    public Room(String name, String image) {
        this.name = name;
        this.image = image;
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
                ", name='" + name + '\'' +
                ", image='" + image + '\'' +
                ", isSelected=" + isSelected +
                '}';
    }
}