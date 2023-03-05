package com.tcs.finalproject.bankapp.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.tcs.finalproject.bankapp.entity.Accounts;

public interface AccountsRepository extends JpaRepository<Accounts, Long> {

    List<Accounts> findByUserIdAndOpen(Long id, boolean isOpen);

    Accounts findByIdAndOpen(Long id, boolean isOpen);

    Accounts findByIdAndUserIdAndOpen(Long id, Long userId, boolean isOpen);
}
