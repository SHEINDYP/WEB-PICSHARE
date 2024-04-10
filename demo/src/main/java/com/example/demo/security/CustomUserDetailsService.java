package com.example.demo.security;

import com.example.demo.model.User;
import com.example.demo.service.UserRepository;
//import com.javatpoint.User;
//import com.javatpoint.UserRepository;
//import com.javatpoint.security.CustomUserDetails;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

//import javax.transaction.Transactional;

//********תפקיד המחלקה:
//לספק את הלוגיקה הפשוטה על מנת לטעון את פרטי המשתמש
@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    //********תפקיד הפונקציה:
    //מה הפונקציה מקבלת?
    //מה הפונקציה מחזירה?
    //מקבלת שם משתמש מהמשתמש ובודקת במסד הנתונים האם קיים משתמש כזה אם כן מחזירה את האובייקט של משתמש זה אם לא זורקת שגיאה
    @Override
    @Transactional
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        User user=userRepository.findByEmail(s);
        if(user==null)
            throw new UsernameNotFoundException("User not found");
        return new CustomUserDetails(user);
    }
}
