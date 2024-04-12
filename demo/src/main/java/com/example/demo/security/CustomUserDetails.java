package com.example.demo.security;


import com.example.demo.model.Role;
import com.example.demo.model.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.*;
import java.util.stream.Collectors;

//********תפקיד המחלקה:
//קביעת פרטי הכניסה של המשתמש, כמו שם משתמש וסיסמה.
//קביעת הרשאות המשתמש, המגדירות אילו פעולות הוא רשאי לבצע במערכת.
//קביעת סטטוס המשתמש, כמו חשבון פעיל או חסום.
public class CustomUserDetails implements UserDetails {
    private User user;

    public CustomUserDetails(User user) {
        super();
        this.user = user;
    }

    //********תפקיד הפונקציה:
    //מה הפונקציה מחזירה?
    //רשימה של הרשאות-עוברת על רשימת התפקידים
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> grantedAuthorities = new ArrayList<>();
        for (Role role : user.getRoles()) {
            grantedAuthorities.add(new SimpleGrantedAuthority(role.getName().name()));
        }
        return grantedAuthorities;

    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getEmail();
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
        return true;
    }


}
