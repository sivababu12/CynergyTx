package com.cilpl.clusters.Model;

import java.io.Serializable;


public class TimeSlotsModel implements Serializable {

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    private String time;
    private String status;
    private String id;






}
