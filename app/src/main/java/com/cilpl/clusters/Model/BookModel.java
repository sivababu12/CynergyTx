package com.cilpl.clusters.Model;

import java.io.Serializable;

public class BookModel implements Serializable {


    public String getBookdate() {
        return bookdate;
    }

    public void setBookdate(String bookdate) {
        this.bookdate = bookdate;
    }

    public String getTimeslots() {
        return timeslots;
    }

    public void setTimeslots(String timeslots) {
        this.timeslots = timeslots;
    }

    public String getChairslots() {
        return chairslots;
    }

    public void setChairslots(String chairslots) {
        this.chairslots = chairslots;
    }

    public String getNo_ofslots() {
        return no_ofslots;
    }

    public void setNo_ofslots(String no_ofslots) {
        this.no_ofslots = no_ofslots;
    }

    public String getNo_ofchairs() {
        return no_ofchairs;
    }

    public void setNo_ofchairs(String no_ofchairs) {
        this.no_ofchairs = no_ofchairs;
    }

    private String bookdate;
private String timeslots;
private String chairslots;
private String no_ofslots;
private String no_ofchairs;

}
