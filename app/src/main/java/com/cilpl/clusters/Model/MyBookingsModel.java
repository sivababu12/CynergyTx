package com.cilpl.clusters.Model;

import java.io.Serializable;


public class MyBookingsModel implements Serializable {


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProduct_id() {
        return product_id;
    }

    public void setProduct_id(String product_id) {
        this.product_id = product_id;
    }

    public String getBooking_type() {
        return booking_type;
    }

    public void setBooking_type(String booking_type) {
        this.booking_type = booking_type;
    }

    public String getFull_name() {
        return full_name;
    }

    public void setFull_name(String full_name) {
        this.full_name = full_name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFrom_date() {
        return from_date;
    }

    public void setFrom_date(String from_date) {
        this.from_date = from_date;
    }

    public String getTo_date() {
        return to_date;
    }

    public void setTo_date(String to_date) {
        this.to_date = to_date;
    }

    public String getCapacity() {
        return capacity;
    }

    public void setCapacity(String capacity) {
        this.capacity = capacity;
    }

    private String name;
    private String product_id;
    private String booking_type;
    private String full_name;
    private String phone;
    private String email;
    private String from_date;
    private String to_date;
    private String capacity;

    public String getChairs() {
        return chairs;
    }

    public void setChairs(String chairs) {
        this.chairs = chairs;
    }

    public String getTimeslots() {
        return timeslots;
    }

    public void setTimeslots(String timeslots) {
        this.timeslots = timeslots;
    }

    public String getBooking_id() {
        return booking_id;
    }

    public void setBooking_id(String booking_id) {
        this.booking_id = booking_id;
    }

    private String chairs;
    private String timeslots;
    private String booking_id;





}
