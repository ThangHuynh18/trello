package com.example.trello.security.service;

import com.example.trello.entity.User;
import com.example.trello.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

//dùng để custom lại cách load thông tin user
@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    private final UserRepository userRepository;

    public UserDetailsServiceImpl (UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username)
            throws UsernameNotFoundException {

        System.out.println("USER NAME: "+username);
        User user = userRepository.findByUserName(username)
                .orElseThrow(() ->
                        new UsernameNotFoundException("User Not Found with -> username or email : " + username)
                );


        return UserDetailsImpl.build(user);
    }
}
