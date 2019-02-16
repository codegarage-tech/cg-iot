package tech.codegarage.iot.model;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Md. Rashadul Alam
 * Email: rashed.droid@gmail.com
 */
public class Device {

    private String id = "";
    private String name = "";
    private String image = "";
    private List<String> connection_type = new ArrayList<>();
    private boolean isSelected = false;

    public Device() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
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

    public List<String> getConnection_type() {
        return connection_type;
    }

    public void setConnection_type(List<String> connection_type) {
        this.connection_type = connection_type;
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
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", image='" + image + '\'' +
                ", connection_type=" + connection_type +
                ", isSelected=" + isSelected +
                '}';
    }
}