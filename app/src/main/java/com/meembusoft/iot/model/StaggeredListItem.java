package com.meembusoft.iot.model;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Md. Rashadul Alam
 * Email: rashed.droid@gmail.com
 */
public class StaggeredListItem {

    private List<StaggeredItem> items = new ArrayList<>();

    public StaggeredListItem(List<StaggeredItem> items) {
        this.items = items;
    }

    public List<StaggeredItem> getItems() {
        return items;
    }

    public void setItems(List<StaggeredItem> items) {
        this.items = items;
    }

    @Override
    public String toString() {
        return "{" +
                "items=" + items +
                '}';
    }
}