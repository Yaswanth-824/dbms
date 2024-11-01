package com.example.Travel.And.Tourisum.service.userservice;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.security.Principal;
import com.example.Travel.And.Tourisum.DataAccessObject_DAO.userdao;
import com.example.Travel.And.Tourisum.models.user;

@Service
public class userservice {
    @Autowired
    userdao userdao;
    public String register(user user) {
        System.out.println("Step 2");
        return userdao.save(user);
    }
    public user profile(Principal principal){
        return userdao.profile(principal);
    }

}
