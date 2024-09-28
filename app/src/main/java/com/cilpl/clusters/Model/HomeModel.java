package com.cilpl.clusters.Model;

import java.io.Serializable;

public class HomeModel implements Serializable {
    private String id;
    private String name;
    private String images;
    private String room_features;
    private String seats_capacity;
    private String availability;
    private String bookingtype;
    private String price;

    public String getWork_stations() {
        return work_stations;
    }

    public void setWork_stations(String work_stations) {
        this.work_stations = work_stations;
    }

    public String getConf_room() {
        return conf_room;
    }

    public void setConf_room(String conf_room) {
        this.conf_room = conf_room;
    }

    public String getManager_cabins() {
        return manager_cabins;
    }

    public void setManager_cabins(String manager_cabins) {
        this.manager_cabins = manager_cabins;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    private String work_stations;
    private String conf_room;
    private String manager_cabins;
    private String size;
    private String aminities;

    public String getAminities() {
        return aminities;
    }

    public void setAminities(String aminities) {
        this.aminities = aminities;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getBookingtype() {
        return bookingtype;
    }

    public void setBookingtype(String bookingtype) {
        this.bookingtype = bookingtype;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImages() {
        return images;
    }

    public void setImages(String images) {
        this.images = images;
    }

    public String getRoom_features() {
        return room_features;
    }

    public void setRoom_features(String room_features) {
        this.room_features = room_features;
    }

    public String getSeats_capacity() {
        return seats_capacity;
    }

    public void setSeats_capacity(String seats_capacity) {
        this.seats_capacity = seats_capacity;
    }

    public String getAvailability() {
        return availability;
    }

    public void setAvailability(String availability) {
        this.availability = availability;
    }
}
