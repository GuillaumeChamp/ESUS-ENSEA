package com.example.application.data.entity;

import javax.persistence.Entity;



@Entity
public class Triggers extends AbstractEntity{

    public boolean register;
    public boolean french_account;
    public boolean flight_planning;

    public void setFlightPlanning(boolean flightPlanning) {
        this.flight_planning = flightPlanning;
    }
    public void setFrenchAccount(boolean frenchAccount) {
        this.french_account = frenchAccount;
    }
    public void setRegister(boolean register) {
        this.register = register;
    }

}
