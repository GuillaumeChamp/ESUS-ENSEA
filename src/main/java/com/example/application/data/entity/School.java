package com.example.application.data.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Formula;

import java.util.LinkedList;
import java.util.List;
import javax.annotation.Nullable;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Entity
public class School extends AbstractEntity {
    @NotBlank
    private String name;
    @Formula("(select count(s.id) from Student s where s.school_id = id)")
    private int studentCount;
    private String city;
    @OneToMany(mappedBy = "school")
    @Nullable
    private List<Student> students = new LinkedList<>();
    @Email
    private String contact;
    @ManyToOne
    @JoinColumn(name = "country_id")
    @NotNull
    @JsonIgnoreProperties({"students"})
    private Country country;

    //GETTER (Needed by beanValidation)
    public String getName() {
        return name;
    }
    @Nullable public List<Student> getStudents() {
        return students;
    }
    public int getStudentCount(){
        return studentCount;
    }
    public Country getCountry() {
        return country;
    }
    public String getCity() {
        return city;
    }

    public String getContact() {
        return contact;
    }

    //SETTER
    public void setName(String name) {
        this.name = name;
    }
    public void setStudents(@Nullable List<Student> students) {
        this.students = students;
    }
    public void setCountry(Country country) {
        this.country = country;
    }
    public void setCity(String city) {
        this.city = city;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }
}
