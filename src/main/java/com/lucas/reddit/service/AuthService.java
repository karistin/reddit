package com.lucas.reddit.service;

import static com.lucas.reddit.util.Constants.ACTIVATION_EMAIL;

import com.lucas.reddit.config.SecurityConfig;
import com.lucas.reddit.dto.AuthenticationResponse;
import com.lucas.reddit.dto.LoginRequest;
import com.lucas.reddit.dto.RegisterRequest;
import com.lucas.reddit.exception.SpringRedditException;
import com.lucas.reddit.model.NotificationEmail;
import com.lucas.reddit.model.User;
import com.lucas.reddit.model.VerificationToken;
import com.lucas.reddit.repository.UserRepository;
import com.lucas.reddit.repository.VerificationTokenRepository;
import com.lucas.reddit.security.JwtProvider;
import jakarta.transaction.Transactional;
import java.time.Instant;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;
    private final MailService mailService;
    private final VerificationTokenRepository verificationTokenRepository;
    private final AuthenticationManager authenticationManager;
//    AuthenticationProvider : 실제 인증 로직이 있는 객체

    @Transactional
    public void signup(RegisterRequest registerRequest) {
        User user = new User();
        user.setUsername(registerRequest.getUsername());
        user.setEmail(registerRequest.getEmail());
//        BCryptPasswordEncoder
        user.setPassword(encodePassword(registerRequest.getPassword()));
        user.setCreated(Instant.now());
        user.setEnabled(false);
//      DB save
        userRepository.save(user);

        String token = generateVerificationToken(user);
        mailService.sendMail(new NotificationEmail("Please Activate your Account", user.getEmail() ,
            "Thank you for signing up to Spring Reddit, "
                + "please click on the below url to activate your account : "
            + ACTIVATION_EMAIL + "/" + token));

    }

    private String generateVerificationToken(User user) {
        String token = UUID.randomUUID().toString();
        VerificationToken verificationToken = new VerificationToken();
        verificationToken.setToken(token);
        verificationToken.setUser(user);
        verificationTokenRepository.save(verificationToken);
        return token;
    }
    private String encodePassword(String password) {
        return passwordEncoder.encode(password);
    }

    public void verifyAccount(String token) {
        Optional<VerificationToken> verificationToken = verificationTokenRepository.findByToken(token);
        verificationToken.orElseThrow(() -> new SpringRedditException("Invalid Token"));
        fetchUserAndEnable(verificationToken.get());
    }

    @Transactional
    private void fetchUserAndEnable(VerificationToken verificationToken) {
        String username = verificationToken.getUser().getUsername();
//        long userId = verificationToken.getUser().getUserId();

        User user = userRepository.findByUsername(username).orElseThrow(() ->
            new SpringRedditException("User not found with name - " + username));

        userRepository.save(user);
    }

    public AuthenticationResponse login(LoginRequest loginRequest) {

        System.out.println("authenticationManager : " + authenticationManager);
        Authentication authenticate = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(
                loginRequest.getUsername(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authenticate);
        String token = jwtProvider.generateToken(authenticate);
        log.info("Token :" + token);
        return new AuthenticationResponse(token, loginRequest.getUsername());
    }
}
