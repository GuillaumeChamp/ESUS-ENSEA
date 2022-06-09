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
public class Job {
    @Id
    @GeneratedValue
    @Type(type = "int")
    private int id;

    @NotBlank
    private String job;
    @Formula("(select" +
            "(select count(s.job_id1) from Student s where s.job_id1 = id) " +
            "+ (select count(s.job_id2) from Student s where s.job_id2 = id))")
    private int studentCount;


    @OneToMany(mappedBy = "job1")
    @Nullable private List<Student> students = new LinkedList<>();

    //SETTER
    public void setJob(String job) {
        this.job = job;
    }
    public void setStudents(@Nullable List<Student> students) {
        this.students = students;
    }

    //GETTER
    public String getJob() {
        return job;
    }
    @Nullable public List<Student> getStudents() {
        return students;
    }
    public int getStudentCount(){
        return studentCount;
    }
}
