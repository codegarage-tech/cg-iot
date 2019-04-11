package io.armcha.ribble.presentation.navigationview;

public enum NavigationId {

    OWN_DEVICES("OWN DEVICES"),
    PRODUCTS("PRODUCTS"),
    LOGIN("LOGIN"),
    SETTINGS("SETTINGS"),
    LOGOUT("LOGOUT"),
    ADD_DEVICE("ADD DEVICE");

    private String value = "";

    NavigationId(String name) {
        value = name;
    }

    public String getValue() {
        return value;
    }
}