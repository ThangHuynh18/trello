package com.example.trello.controller;

import com.example.trello.dto.LoginRequest;
import com.example.trello.dto.LoginResponse;
import com.example.trello.security.jwt.JwtTokenProvider;
import com.example.trello.security.service.UserDetailsImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class UserController {

    final private AuthenticationManager authenticationManager;

    final private JwtTokenProvider jwtUtils;

    public UserController(AuthenticationManager authenticationManager, JwtTokenProvider jwtUtils) {
        this.authenticationManager = authenticationManager;
        this.jwtUtils = jwtUtils;
    }

    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {

        System.out.println(loginRequest.getPassword());
        //lấy username, pw từ request, authManager sẽ dùng nó để xác thực đăng nhập
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));


        // if go there, the user/password is correct
        SecurityContextHolder.getContext().setAuthentication(authentication);
        // generate jwt to return to client
        String jwt = jwtUtils.generateJwtToken(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        List<String> roles = userDetails.getAuthorities().stream()
                .map(item -> item.getAuthority())
                .collect(Collectors.toList());
        //String roles = userDetails.getAuthorities();

        return ResponseEntity.ok(new LoginResponse(jwt,
                userDetails.getId(),
                userDetails.getUsername(),
                userDetails.getEmail(),
                roles));
    }

}
