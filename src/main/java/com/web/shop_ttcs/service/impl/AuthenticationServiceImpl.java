package com.web.shop_ttcs.service.impl;

import com.web.shop_ttcs.converter.UserConvertTo;
import com.web.shop_ttcs.exception.ex.RefreshTokenNotFoundException;
import com.web.shop_ttcs.exception.ex.UserNotFoundException;
import com.web.shop_ttcs.model.dto.ChangePasswordDTO;
import com.web.shop_ttcs.model.dto.UserLoginDTO;
import com.web.shop_ttcs.model.dto.UserRegisterDTO;
import com.web.shop_ttcs.model.entity.RefreshTokenEntity;
import com.web.shop_ttcs.model.entity.RoleEntity;
import com.web.shop_ttcs.model.entity.UserEntity;
import com.web.shop_ttcs.model.response.UserResponse;
import com.web.shop_ttcs.repository.RefreshTokenRepository;
import com.web.shop_ttcs.repository.RoleRepository;
import com.web.shop_ttcs.repository.UserRepository;
import com.web.shop_ttcs.service.AuthenticationService;
import com.web.shop_ttcs.service.MailService;
import com.web.shop_ttcs.service.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

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

    @Autowired
    private MailService mailService;

    @Autowired
    private RefreshTokenRepository refreshTokenRepository;

    @Autowired
    private UserConvertTo userConvertTo;

    /*
    * logic: verify user by sending email
    * */
    @Override
    public String register(UserRegisterDTO userRegisterDTO) {
        Optional<UserEntity> optionalUsername = userRepository.findByUsername(userRegisterDTO.getUsername());
        if (optionalUsername.isPresent()) {
            return "failed to register";
        }
        Optional<UserEntity> optionalEmail = userRepository.findByEmail(userRegisterDTO.getEmail().toLowerCase());
        if (optionalEmail.isPresent()) {
            return "failed to register";
        }
        Optional<RoleEntity> optionalRole = roleRepository.findById(userRegisterDTO.getRoleId());
        if (optionalRole.isEmpty()) {
            return "failed to register";
        }
        RoleEntity role = optionalRole.get();
        String verificationCode = generateCode();
        UserEntity user = UserEntity.builder()
                .username(userRegisterDTO.getUsername())
                .firstName(userRegisterDTO.getFirstName())
                .lastName(userRegisterDTO.getLastName())
                .email(userRegisterDTO.getEmail().toLowerCase())
                .password(passwordEncoder.encode(userRegisterDTO.getPassword()))
                .sex(userRegisterDTO.getSex())
                .age(userRegisterDTO.getAge())
                .coins(0L)
                .phoneNumber(userRegisterDTO.getPhoneNumber())
                .cartItemEntities(new ArrayList<>())
                .imageEntities(new ArrayList<>())
                .shopEntities(new ArrayList<>())
                .refreshTokenEntities(new ArrayList<>())
                .verificationCode(verificationCode)
                .verificationCodeExpiration(new Date(new Date().getTime() + 1000 * 60 * 15))
                .role(role)
                .email(userRegisterDTO.getEmail())
                .enabled(false)
                .build();
        userRepository.save(user);

        sendCode(userRegisterDTO.getEmail(), "VerificationCode", verificationCode);
        return "registered successfully";
    }

    @Override
    public String verify(String email, String verificationCode) {
        Optional<UserEntity> optionalUser = userRepository.findByEmail(email.toLowerCase());
        if (optionalUser.isEmpty()) {
            throw new UserNotFoundException("User not found");
        }
        UserEntity userEntity = optionalUser.get();
        if(userEntity.getEnabled()){
            return "user already verified";
        }
        if (!verificationCode.equals(userEntity.getVerificationCode())
            || !new Date(new Date().getTime()).before(userEntity.getVerificationCodeExpiration())) {
            return "failed to verify";
        }
        userEntity.setVerificationCode(null);
        userEntity.setVerificationCodeExpiration(null);
        userEntity.setEnabled(true);
        userRepository.save(userEntity);

        return "verified successfully";
    }

    @Override
    public String resendVerificationCode(String email) {
        Optional<UserEntity> optionalEmail = userRepository.findByEmail(email.toLowerCase());
        if (optionalEmail.isEmpty()) {
            throw new UserNotFoundException("User not found");
        }
        UserEntity userEntity = optionalEmail.get();
        if(userEntity.getEnabled()){
            return "User already verified";
        }
        String newCode = generateCode();

        userEntity.setVerificationCode(newCode);
        userEntity.setVerificationCodeExpiration(new Date(new Date().getTime() + 1000 * 60 * 15));
        userRepository.save(userEntity);

        sendCode(email, "VerificationCode", newCode);
        return "checked your email";
    }

    @Override
    public String forgotPassword(String email) {
        Optional<UserEntity> optional = userRepository.findByEmail(email.toLowerCase());
        if(optional.isEmpty()) {
            throw new UserNotFoundException("User not found");
        }
        UserEntity userEntity = optional.get();
        String newPassword = generateCode();
        userEntity.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(userEntity);

        sendCode(email, "Password", newPassword);
        return "checked your email";
    }

    @Override
    public String changePassword(ChangePasswordDTO changePasswordDTO) {
        if(changePasswordDTO.getUserId() == null ||
        !changePasswordDTO.getNewPassword().equals(changePasswordDTO.getConfirmPassword())) {
            return "failed to change password";
        }
        Optional<UserEntity> optional = userRepository.findById(changePasswordDTO.getUserId());
        if (optional.isEmpty()) {
            throw new UserNotFoundException("User not found");
        }
        UserEntity userEntity = optional.get();
        if(!passwordEncoder.matches(changePasswordDTO.getOldPassword(), userEntity.getPassword())){
            return "failed to change password";
        }
        userEntity.setPassword(passwordEncoder.encode(changePasswordDTO.getNewPassword()));
        userRepository.save(userEntity);
        return "changed password successfully";
    }

    @Override
    public UserResponse infoToken(String token) {
        if(!tokenService.validateToken(token)){
            return null;
        }
        UserEntity userEntity = tokenService.getUserEntity(token);
        if(userEntity == null){
            throw new UserNotFoundException("User not found");
        }
        return userConvertTo.convertTo(userEntity);
    }

    @Transactional(readOnly = true)
    public String refreshToken(String token) {
        if(!tokenService.validateRefreshToken(token)){
            return "refreshed token invalid";
        }
        Optional<RefreshTokenEntity> optional = refreshTokenRepository.findByRefreshToken(token);
        if (optional.isEmpty()) {
            throw new RefreshTokenNotFoundException("Refresh token not found");
        }
        RefreshTokenEntity refreshTokenEntity = optional.get();
        UserEntity userEntity = refreshTokenEntity.getUserEntity();
        return tokenService.generateToken(userEntity);
    }

    /*
    * logic: user login => delete all RefreshToken + create new RefreshToken
    * */
    @Transactional(isolation = Isolation.SERIALIZABLE, rollbackFor = {Exception.class})
    public String login(UserLoginDTO userLoginDTO) {
        Optional<UserEntity> optionalUsername = userRepository.findByUsername(userLoginDTO.getUsername());
        if (optionalUsername.isEmpty()) {
            return "failed to login";
        }
        UserEntity userEntity = optionalUsername.get();
        if (!passwordEncoder.matches(userLoginDTO.getPassword(), userEntity.getPassword())) {
            return "failed to login";
        }
        if(!userEntity.getEnabled()){
            return "failed to login";
        }
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken
                = new UsernamePasswordAuthenticationToken(userLoginDTO.getUsername(), userLoginDTO.getPassword(), userEntity.getAuthorities());
        authenticationManager.authenticate(usernamePasswordAuthenticationToken);

        // delete old RefreshToken
        refreshTokenRepository.deleteByUserEntity(userEntity);
        // create new RefreshToken
        String refreshToken = tokenService.generateRefreshToken(userEntity);
        RefreshTokenEntity refreshTokenEntity = RefreshTokenEntity.builder()
                .refreshToken(refreshToken)
                .userEntity(userEntity)
                .build();
        refreshTokenRepository.save(refreshTokenEntity);

        return tokenService.generateToken(userEntity) ;
    }


    public String generateCode(){
        return new Random().nextInt(100000) + "";
    }

    public void sendCode(String to, String subject, String content) {
        String html = "<html>" + subject + ": " + content + "</html>";
        mailService.send(to, subject, html);
    }
}
