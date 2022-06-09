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
    /*

Are you using any social network ENSEA’s students could use to join you (WhatsApp, Facebook, Telegram…)?
Arriving by train/plane/other
Airport or train station name
Airport Terminal (if applicable)
City of departure
Date of arrival
Hour of arrival
Flight or train number
     */

    private String place;
    private String phoneNumber;
    private LocalDateTime date;
    private String meansOfTransport;
    private String airportTerminal;
    private String network;
    private String departure;
    private String transportID;


    //GETTER
    public LocalDateTime getDate() {
        return date;
    }

    public String getMeansOfTransport() {
        return meansOfTransport;
    }

    public String getNetwork() {
        return network;
    }

    public String getTransportID() {
        return transportID;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }
    public String getPlace() {
        return place;
    }

    public String getDeparture() {
        return departure;
    }

    public String getAirportTerminal() {
        return airportTerminal;
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

    public void setAirportTerminal(String airportTerminal) {
        this.airportTerminal = airportTerminal;
    }

    public void setMeansOfTransport(String meansOfTransport) {
        this.meansOfTransport = meansOfTransport;
    }

    public void setNetwork(String network) {
        this.network = network;
    }

    public void setTransportID(String transportID) {
        this.transportID = transportID;
    }

    public void setDeparture(String departure) {
        this.departure = departure;
    }
}
