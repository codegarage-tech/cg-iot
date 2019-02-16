package tech.codegarage.iot.util;

import java.util.ArrayList;
import java.util.List;

import tech.codegarage.iot.model.Product;

public class ResponseOfflineDevice {

    private String status = "";
    private String message = "";
    private List<Product> data = new ArrayList<>();

    public ResponseOfflineDevice() {
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<Product> getData() {
        return data;
    }

    public void setData(List<Product> data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "{" +
                "status='" + status + '\'' +
                ", message='" + message + '\'' +
                ", data=" + data +
                '}';
    }
}