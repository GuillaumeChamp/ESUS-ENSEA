package com.application.data.entity;

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
public class ExchangeType {
    @Id
    @GeneratedValue
    @Type(type = "int")
    private int id;

    @NotBlank
    private String name;

    @NotBlank
    private String name_ext;

    @OneToMany(mappedBy = "exchangeType")
    @Nullable
    private List<Student> students = new LinkedList<>();

    @Formula("(select count(s.exchange_type_id) from Student s where s.exchange_type_id = id)")
    private int studentCount;

    //GETTER
    public int getId() {
        return id;
    }
    public String getName_ext() {
        return name_ext;
    }
    public String getName() {
        return name;
    }
    @Nullable
    public List<Student> getStudents() {
        return students;
    }
    public int getStudentCount() {
        return studentCount;
    }

    //SETTER
    public void setId(int id) {
        this.id = id;
    }
    public void setStudents(@Nullable List<Student> students) {
        this.students = students;
    }
    public void setName(String name) {
        this.name = name;
    }
    public void setName_ext(String name_ext) {
        this.name_ext = name_ext;
    }
}
