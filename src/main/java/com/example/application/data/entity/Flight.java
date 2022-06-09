package com.example.application.data.entity;

import org.hibernate.annotations.Type;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.time.LocalDateTime;

@Entity
public class Flight {
    @Id
    @GeneratedValue
    @Type(type = "int")
    private int id;

    private String place;
    private String phoneNumber;
    private LocalDateTime date;

    @Override
    public String toString() {
        return "Flight{" +
                "place='" + place + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", date=" + date +
                '}';
    }
    //GETTER
    public LocalDateTime getDate() {
        return date;
    }
    public String getPhoneNumber() {
        return phoneNumber;
    }
    public String getPlace() {
        return place;
    }

    //SETTER
    public void setPlace(String place) {
        this.place = place;
    }
    public void setDate(LocalDateTime date) {
        this.date = date;
    }
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

}
