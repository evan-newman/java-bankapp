package com.tcs.finalproject.bankapp.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.tcs.finalproject.bankapp.entity.LoginUser;
import com.tcs.finalproject.bankapp.entity.Users;
import com.tcs.finalproject.bankapp.repository.UsersRepository;


@RestController
public class ValidateController {

	@Autowired
	private UsersRepository repo;
	
	@Autowired
	private PasswordEncoder passwordEncoder;

	@PostMapping("/validate")
	public ResponseEntity<String>  validate(@RequestBody LoginUser user) {
		ResponseEntity<String> response = null;
		
		Optional<Users> found = repo.findByEmail(user.getEmail());
		if (found.isEmpty()) {
			response = ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
					"Username or password incorrect");
		} else {
			Users foundUser = found.get();
			System.out.println("*********************************");
			System.out.println(user.getPwd() + "   " + foundUser.getPassword());
			System.out.println("*********************************");
			boolean match = passwordEncoder.matches(user.getPwd(),
			     foundUser.getPassword());

			if (!match) {
				response = ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR
				     ).body("Username or password incorrect");
							} else {
								response = ResponseEntity.status(HttpStatus.OK).body("Login valid!");
							}

		}
			
		return response;
	}
}
