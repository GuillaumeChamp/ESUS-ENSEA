package com.example.application.data.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;


import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;


@Entity
public class Student extends AbstractEntity {

    @NotEmpty
    private String civility = "";

    @NotEmpty
    private String gender = "";
    @ManyToOne
    @JoinColumn(name = "nationality")
    @NotNull
    @JsonIgnoreProperties({"students"})
    private Country nationality;
    @NotEmpty
    private String firstName = "";
    @NotEmpty
    private String lastName = "";
    @NotNull
    private LocalDate born;
    @ManyToOne
    @JoinColumn(name = "step_list_id")
    @Cascade(CascadeType.ALL)
    private Triggers triggers;

    private String phoneNumber;
    @NotNull
    private String bornPlace;
    @NotNull
    private double endYear;
    @NotNull
    private double startSuperior;
    private String progress;

    @ManyToOne
    @JoinColumn(name = "school_id")
    @NotNull
    @JsonIgnoreProperties({"students"})
    private School school;

    @ManyToOne
    @JoinColumn(name = "country_id")
    @NotNull
    @JsonIgnoreProperties({"students"})
    private Country country;
    @ManyToOne
    @JoinColumn(name = "parkour_id")
    //@Cascade(CascadeType.ALL)
    private Parkour parkour;

    @ManyToOne
    @JoinColumn(name = "exchange_type_id")
    @NotNull
    @JsonIgnoreProperties({"students"})
    private ExchangeType exchangeType;

    @ManyToOne
    @JoinColumn(name = "job_id1")
    @NotNull
    @JsonIgnoreProperties({"students"})
    private Job job1;

    @ManyToOne
    @JoinColumn(name = "job_id2")
    @NotNull
    @JsonIgnoreProperties({"students"})
    private Job job2;

    @ManyToOne
    @JoinColumn(name = "flight_id")
    private Flight flight;

    @Email
    @NotEmpty
    private String email = "";

    //GETTER
    public String getFirstName() {
        return firstName;
    }
    public String getLastName() {
        return lastName;
    }
    public Country getNationality() {
        return nationality;
    }
    public School getSchool() {
        return school;
    }
    public ExchangeType getExchangeType(){
        return exchangeType;
    }
    public String getEmail() {
        return email;
    }
    public String getCivility() {
        return civility;
    }
    public Country getCountry() {
        return country;
    }

    public double getStartSuperior() {
        return startSuperior;
    }

    public Job getJob1() {
        return job1;
    }
    public Job getJob2() {
        return job2;
    }
    public Flight getFlight() {
        return flight;
    }
    public LocalDate getBorn() {
        if (born!=null)this.born.format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));
        return born;
    }
    public String getPhoneNumber() {
        return phoneNumber;
    }
    public String getBornPlace() {
        return bornPlace;
    }
    public double getEndYear() {
        return endYear;
    }
    public String getProgress() {
        return progress;
    }
    public String getGender() {
        return gender;
    }
    public Triggers getTriggers() {
        return triggers;
    }
    public Parkour getParkour() {
        return parkour;
    }

    //SETTER (needed by JPARep)
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setStartSuperior(double startSuperior) {
        this.startSuperior = startSuperior;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
    public void setSchool(School school) {
        this.school = school;
    }
    public void setExchangeType(ExchangeType exchangeType) {
        this.exchangeType = exchangeType;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public void setCivility(String civility) {
        this.civility = civility;
    }
    public void setCountry(Country country) {
        this.country = country;
    }
    public void setJob1(Job job1) {
        this.job1 = job1;
    }
    public void setJob2(Job job2) {
        this.job2= job2;
    }
    public void setFlight(Flight flight) {
        this.flight = flight;
    }
    public void setBorn(LocalDate born) {
        born.format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));
        this.born = born;
    }
    public void setNationality(Country nationality) {
        this.nationality = nationality;
    }
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
    public void setBornPlace(String bornPlace) {
        this.bornPlace = bornPlace;
    }
    public void setEndYear(double endYear) {
        this.endYear = endYear;
    }
    public void setProgress(String progress) {
        this.progress = progress;
    }
    public void setGender(String gender) {
        this.gender = gender;
    }
    public void setTriggers(Triggers triggers) {
        this.triggers = triggers;
    }
    public void setParkour(Parkour parkour) {
        this.parkour = parkour;
    }
}
