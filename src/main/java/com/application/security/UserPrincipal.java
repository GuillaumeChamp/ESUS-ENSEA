package com.application.security;

import com.application.data.entity.Student;
import com.application.data.entity.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class UserPrincipal implements UserDetails {
    private final User user;

    public UserPrincipal(User user){
        this.user = user;
    }

    public User getUser() {
        return user;
    }
    public boolean isAdmin(){
        return getAuthorities().stream().anyMatch(a->a.getAuthority().equals("ROLE_ADMIN"));
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(this.user.getRole()));
        return authorities;
    }

    @Override
    public String getPassword() {
        return this.user.getPassword();
    }
    public Student getStudent(){
        return this.user.getStudent();
    }
    public void setStudent(Student student){
        this.user.setStudent(student);
    }

    @Override
    public String getUsername() {
        return user.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return user.getActive()==1;
    }
}
