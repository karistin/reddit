package com.lucas.reddit.service;

import static java.util.Collections.singletonList;

import com.lucas.reddit.model.User;
import com.lucas.reddit.repository.UserRepository;
import java.util.Collection;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserDetailServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;
    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> userOptional = userRepository.findByUsername(username);
        User user = userOptional.orElseThrow(() -> new UsernameNotFoundException("Not User "
            + "Found with Username " + username));

        return org.springframework.security.core.userdetails.User.builder().username(user.getUsername())
            .password(user.getPassword())
            .accountExpired(true)
            .credentialsExpired(true)
            .accountLocked(true)
            .authorities(getAuthorities("USER"))
            .build();
    }

    private Collection<? extends GrantedAuthority> getAuthorities(String role) {
        return singletonList(new SimpleGrantedAuthority(role));
    }
}
