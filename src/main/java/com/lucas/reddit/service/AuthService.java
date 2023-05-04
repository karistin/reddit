package com.lucas.reddit.service;

import static com.lucas.reddit.util.Constants.ACTIVATION_EMAIL;

import com.lucas.reddit.dto.RegisterRequest;
import com.lucas.reddit.model.NotificationEmail;
import com.lucas.reddit.model.User;
import com.lucas.reddit.model.VerificationToken;
import com.lucas.reddit.repository.UserRepository;
import jakarta.transaction.Transactional;
import java.time.Instant;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final MailService mailService;

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
        return token;
    }
    private String encodePassword(String password) {
        return passwordEncoder.encode(password);
    }
}
