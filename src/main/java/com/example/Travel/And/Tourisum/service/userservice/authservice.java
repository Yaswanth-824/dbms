package com.example.Travel.And.Tourisum.service.userservice;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.example.Travel.And.Tourisum.DataAccessObject_DAO.impl.authimpl;
import com.example.Travel.And.Tourisum.DataAccessObject_DAO.impl.userimpl;
import com.example.Travel.And.Tourisum.models.user;


public class authservice implements UserDetailsService{

    @Autowired
    private userimpl userimpl;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        user user  = userimpl.findByUsername(username);
        if(user == null){
            throw new UsernameNotFoundException("User Not Found in DataBase");
        }
        return new authimpl(user);
    }

}
