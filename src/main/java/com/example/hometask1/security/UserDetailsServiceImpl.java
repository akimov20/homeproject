package com.example.hometask1.security;

import com.example.hometask1.model.User;
import com.example.hometask1.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service("userDetailsServiceImpl")
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {
    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
        User user = userRepository.findByLogin(userName);
        if (user == null) {
            throw new UsernameNotFoundException("Unknown user: " + user.getLogin());
        }

        UserDetails userDetails = org.springframework.security.core.userdetails.User.builder()
                .username(user.getLogin())
                .password(user.getPassword())
                .authorities(new SimpleGrantedAuthority("user"))
                .build();
        return userDetails;
    }
}
