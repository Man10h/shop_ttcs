package com.web.shop_ttcs.service.impl;

import com.web.shop_ttcs.model.dto.UserLoginDTO;
import com.web.shop_ttcs.model.dto.UserRegisterDTO;
import com.web.shop_ttcs.model.entity.RoleEntity;
import com.web.shop_ttcs.model.entity.UserEntity;
import com.web.shop_ttcs.repository.RoleRepository;
import com.web.shop_ttcs.repository.UserRepository;
import com.web.shop_ttcs.service.AuthenticationService;
import com.web.shop_ttcs.service.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Optional;

@Service
public class AuthenticationServiceImpl implements AuthenticationService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private TokenService tokenService;

    /*
    * logic: verify user by sending email
    * */
    @Override
    public String register(UserRegisterDTO userRegisterDTO) {
        Optional<UserEntity> optionalUsername = userRepository.findByUsername(userRegisterDTO.getUsername());
        if (optionalUsername.isPresent()) {
            return "failed to register";
        }
        Optional<UserEntity> optionalEmail = userRepository.findByEmail(userRegisterDTO.getEmail());
        if (optionalEmail.isPresent()) {
            return "failed to register";
        }
        Optional<RoleEntity> optionalRole = roleRepository.findById(userRegisterDTO.getRoleId());
        if (optionalRole.isEmpty()) {
            return "failed to register";
        }
        RoleEntity role = optionalRole.get();
        UserEntity user = UserEntity.builder()
                .username(userRegisterDTO.getUsername())
                .firstName(userRegisterDTO.getFirstName())
                .lastName(userRegisterDTO.getLastName())
                .email(userRegisterDTO.getEmail())
                .password(passwordEncoder.encode(userRegisterDTO.getPassword()))
                .sex(userRegisterDTO.getSex())
                .age(userRegisterDTO.getAge())
                .coins(0L)
                .phoneNumber(userRegisterDTO.getPhoneNumber())
                .productEntities(new ArrayList<>())
                .imageEntities(new ArrayList<>())
                .shopEntities(new ArrayList<>())
                .refreshTokenEntities(new ArrayList<>())
                .role(role)
                .build();
        userRepository.save(user);
        return "registered successfully";
    }

    @Override
    public String login(UserLoginDTO userLoginDTO) {
        Optional<UserEntity> optionalUsername = userRepository.findByUsername(userLoginDTO.getUsername());
        if (optionalUsername.isEmpty()) {
            return "failed to login";
        }
        UserEntity userEntity = optionalUsername.get();
        if (!passwordEncoder.matches(userLoginDTO.getPassword(), userEntity.getPassword())) {
            return "failed to login";
        }
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken
                = new UsernamePasswordAuthenticationToken(userLoginDTO.getUsername(), userLoginDTO.getPassword(), userEntity.getAuthorities());
        authenticationManager.authenticate(usernamePasswordAuthenticationToken);

        return tokenService.generateToken(userEntity) ;
    }
}
