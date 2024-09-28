package com.cilpl.clusters.Model;

import java.io.Serializable;


public class ChairSlotModel implements Serializable {


    public String getChair() {
        return chair;
    }

    public void setChair(String chair) {
        this.chair = chair;
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

    private String chair;
    private String status;
    private String id;






}
