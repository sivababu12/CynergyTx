package com.cilpl.clusters.Model;

import java.io.Serializable;

public class NameDetails implements Serializable {
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

    public NameDetails( String booktitile) {

        this.booktitile = booktitile;

    }

    private String startDate;
    private String EndDate;
    private String noOfseates;


}
