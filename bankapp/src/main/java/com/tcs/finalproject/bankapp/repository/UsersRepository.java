package com.tcs.finalproject.bankapp.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.tcs.finalproject.bankapp.entity.Users;

public interface UsersRepository extends JpaRepository<Users, Long> {
    
    Optional<Users> findByEmail(String email);
}
