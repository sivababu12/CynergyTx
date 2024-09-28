package com.cilpl.clusters.Model;

import java.io.Serializable;

public class BookingModel implements Serializable {
    public String getBookid() {
        return bookid;
    }

    public void setBookid(String bookid) {
        this.bookid = bookid;
    }

    public String getBooktitile() {
        return booktitile;
    }

    public void setBooktitile(String booktitile) {
        this.booktitile = booktitile;
    }



    private String bookid;
    private String booktitile;

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return EndDate;
    }

    public void setEndDate(String endDate) {
        EndDate = endDate;
    }

    public String getNoOfseates() {
        return noOfseates;
    }

    public void setNoOfseates(String noOfseates) {
        this.noOfseates = noOfseates;
    }

    public BookingModel(String bookid, String booktitile, String startDate, String endDate, String noOfseates) {
        this.bookid = bookid;
        this.booktitile = booktitile;
        this.startDate = startDate;
        EndDate = endDate;
        this.noOfseates = noOfseates;
    }

    private String startDate;
    private String EndDate;
    private String noOfseates;


}
