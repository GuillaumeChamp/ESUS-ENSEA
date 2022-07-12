package com.application.data.entity;

import org.hibernate.annotations.Type;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
public class Request {
    @Id
    @GeneratedValue
    @Type(type = "int")
    private int id;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "student_id")
    private Student student;
    private String progress;

    //SETTER
    public void setStudent(Student student) {
        this.student = student;
    }
    public void setProgress(String progress) {
        this.progress = progress;
    }

    //GETTER
    public Student getStudent() {
        return student;
    }
    public String getProgress() {
        return progress;
    }
}
