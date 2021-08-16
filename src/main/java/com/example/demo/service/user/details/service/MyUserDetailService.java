package com.example.demo.service.user.details.service;

import com.example.demo.model.user.User;
import com.example.demo.model.user.principal.UserPrincipal;
import com.example.demo.repository.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Service
public class MyUserDetailService implements UserDetailsService {


    private UserRepository userRepository;

    @Autowired
    public MyUserDetailService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {

        Optional<User> byEmail = userRepository.findByEmail(userName);
        if (byEmail.isEmpty()) {
            Optional<User> byUserName = userRepository.findByUserName(userName);
            User user = byUserName.orElseThrow(() -> new UsernameNotFoundException("Can Not found:" + userName));
            return new UserPrincipal(user);
        } else {
            //User user = byEmail.orElseThrow(() -> new UsernameNotFoundException("Can Not found:" + userName));
            return new UserPrincipal(byEmail.get());
        }
    }
}
