package com.cilpl.clusters.Model;

import java.io.Serializable;


public class CartModel implements Serializable {
    public String getCart_id() {
        return cart_id;
    }

    public void setCart_id(String cart_id) {
        this.cart_id = cart_id;
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

    public String getProduct_name() {
        return product_name;
    }

    public void setProduct_name(String product_name) {
        this.product_name = product_name;
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

    private String cart_id;
    private String product_id;
    private String booking_type;
    private String product_name;
    private String from_date;
    private String to_date;
    private String capacity;
    private String chairs;
    private String slots;
    private String price;
    private String s_flag;

    public boolean isCheck_status() {
        return check_status;
    }

    public void setCheck_status(boolean check_status) {
        this.check_status = check_status;
    }

    private  boolean check_status=true;

    public String getS_flag() {
        return s_flag;
    }

    public void setS_flag(String s_flag) {
        this.s_flag = s_flag;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getSlots() {
        return slots;
    }

    public void setSlots(String slots) {
        this.slots = slots;
    }

    public String getChairs() {
        return chairs;
    }

    public void setChairs(String chairs) {
        this.chairs = chairs;
    }
}
