package com.example.application.data.entity;

import org.hibernate.annotations.Formula;
import org.hibernate.annotations.Type;

import javax.annotation.Nullable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotBlank;
import java.util.LinkedList;
import java.util.List;

@Entity
public class Country{
    @Id
    @GeneratedValue
    @Type(type = "int")
    private int id;

    @OneToMany(mappedBy = "country")
    @Nullable
    private List<Student> students = new LinkedList<>();

    @NotBlank
    private String country_name;
    @Formula("(select count(s.country_id) from Student s where s.country_id = id)")
    private int studentCount;

    private int phone_code;
    private String country_code;

    //GETTER
    public String getCountry_name() {
        return country_name;
    }
    public int getId() {
        return id;
    }
    @Nullable
    public List<Student> getStudents() {
        return students;
    }
    public int getStudentCount(){
        return studentCount;
    }

    public int getPhone_code() {
        return phone_code;
    }

    public String getCountry_code() {
        return country_code;
    }

    //SETTER
    public void setCountry_name(String country_name) {
        this.country_name = country_name;
    }
    public void setStudents(@Nullable List<Student> students) {
        this.students = students;
    }
    public void setId(int id) {
        this.id = id;
    }

}
