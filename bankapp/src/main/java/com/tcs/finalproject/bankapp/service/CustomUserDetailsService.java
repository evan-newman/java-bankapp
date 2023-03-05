package com.tcs.finalproject.bankapp.service;

import com.tcs.finalproject.bankapp.entity.Users;
import com.tcs.finalproject.bankapp.entity.Roles;
import com.tcs.finalproject.bankapp.model.UserDetailsImpl;
import com.tcs.finalproject.bankapp.repository.RolesRepository;
import com.tcs.finalproject.bankapp.repository.UsersRepository;
import jakarta.transaction.Transactional;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UsersRepository userRepository;
    private final RolesRepository roleRepository;

    public CustomUserDetailsService(UsersRepository userRepository,
                                    RolesRepository roleRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        Users user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User Not Found with email: " + email));

        return UserDetailsImpl.build(user);
    }
}
