package com.tcs.finalproject.bankapp.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.tcs.finalproject.bankapp.entity.Users;
import com.tcs.finalproject.bankapp.repository.UsersRepository;

@Service
public class UsersService {
    private final UsersRepository usersRepo;

    public UsersService(UsersRepository usersRepo) {
        this.usersRepo = usersRepo;
    }

    public List<Users> getAllUsers() {
        return usersRepo.findAll();
    }

    public Users getUserById(Long id) {
        return usersRepo.findById(id).get();
    }

    public Users saveOrUpdateUser(Users user) {
        return usersRepo.save(user);
    }

    public void deleteUser(Long id) {
        usersRepo.deleteById(id);
    } 
}
