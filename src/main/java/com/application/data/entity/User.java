package com.application.data.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

@Entity
public class User{
    @Id
    @NotNull
    private String username;
    @NotNull
    private String password;

    private int active;
    private String Role;

    @ManyToOne
    @JoinColumn(name = "student_id")
    @JsonIgnoreProperties({"students"})
    @Cascade(CascadeType.ALL)
    private Student student;

    //SETTER
    public void setUsername(String username) {
        this.username = username;
    }
    public void setPassword(String password) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        this.password = passwordEncoder.encode(password);
    }
    public void setActive(int active) {
        this.active = active;
    }
    public void setRole(String role) {
        this.Role = role;
    }
    public void setStudent(Student student) {
        this.student = student;
    }

    //GETTER
    public String getUsername() {
        return username;
    }
    public String getPassword() {
        return password;
    }
    public int getActive() {
        return active;
    }
    public String getRole() {
        return Role;
    }
    public Student getStudent() {
        return student;
    }
}
