package com.tcs.finalproject.bankapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.tcs.finalproject.bankapp.entity.Roles;

public interface RolesRepository extends JpaRepository<Roles, Long> {
    
}
