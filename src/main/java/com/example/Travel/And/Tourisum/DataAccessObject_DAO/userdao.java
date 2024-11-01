package com.example.Travel.And.Tourisum.DataAccessObject_DAO;

import java.security.Principal;
import org.springframework.stereotype.Repository;

import com.example.Travel.And.Tourisum.models.user;
@Repository
public interface userdao {
    String save(user user);
    user findByUsername(String username);
    user findByEmail(String email);
    user profile(Principal principal);
}
