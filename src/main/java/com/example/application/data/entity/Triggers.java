package com.example.application.data.entity;

import javax.annotation.Nullable;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import java.util.LinkedList;
import java.util.List;


@Entity
public class Triggers extends AbstractEntity{

    @OneToOne(mappedBy = "parkour")
    @Nullable
    private Student student;

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

    public void setStudent(@Nullable Student student) {
        this.student = student;
    }

    @Nullable
    public Student getStudent() {
        return student;
    }
}
