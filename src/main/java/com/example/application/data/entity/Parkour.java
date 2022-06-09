package com.example.application.data.entity;

import org.hibernate.annotations.Formula;
import org.hibernate.annotations.Type;


import javax.annotation.Nullable;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.LinkedList;
import java.util.List;

@Entity
public class Parkour{
    @Id
    @GeneratedValue
    @Type(type = "int")
    private int id;

    @NotNull
    private String semester;
    private String option_suivi;
    private String major;
    @OneToMany(mappedBy = "parkour")
    @Nullable
    private final List<Student> students = new LinkedList<>();
    @Formula("(select count(s.parkour_id) from Student s where s.parkour_id = id)")
    private int studentCount;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
    //getter


    public int getStudentCount() {
        return studentCount;
    }

    @Nullable
    public List<Student> getStudents() {
        return students;
    }

    public String getMajor() {
        return major;
    }

    public String getOption_suivi() {
        return option_suivi;
    }

    public String getSemester() {
        return semester;
    }

    public void setMajor(String major) {
        this.major = major;
    }

    public void setOption_suivi(String option_suivi) {
        this.option_suivi = option_suivi;
    }

    public void setSemester(String semester) {
        this.semester = semester;
    }
}
