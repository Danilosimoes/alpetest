package com.alpetest.controller;

import com.alpetest.domain.User;
import com.alpetest.dto.RequestLogin;
import com.alpetest.dto.RequestRegister;
import com.alpetest.dto.ResponseLogin;
import com.alpetest.infra.security.TokenService;
import com.alpetest.repository.UserRepository;
import com.alpetest.pattern.CpfFormatter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Optional;

@Controller
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenService tokenService;
    private final CpfFormatter cpfFormatter;

    @PostMapping("/login")
    public ResponseEntity login(@RequestBody RequestLogin body){

        User user = this.userRepository.findByCpf(body.cpf()).orElseThrow(() -> new RuntimeException("CPF not found"));
        if (passwordEncoder.matches(body.password(), user.getPassword())){
            String token = this.tokenService.generateToken(user);
            return ResponseEntity.ok( new ResponseLogin(user.getName(), token));
        }
        return ResponseEntity.badRequest().build();
    }

    @PostMapping("/register")
    public ResponseEntity register(@RequestBody RequestRegister body){
        Optional<User> user = this.userRepository.findByCpf(cpfFormatter.CpfFormater(body.cpf()));

        if (user.isEmpty()){
            User newUser = new User();
            newUser.setName(body.nome());
            newUser.setEmail(body.email());
            newUser.setCpf(cpfFormatter.CpfFormater(body.cpf()));
            newUser.setPassword(passwordEncoder.encode(body.password()));
            this.userRepository.save(newUser);

            String token = this.tokenService.generateToken(newUser);
            return ResponseEntity.ok(new ResponseLogin(newUser.getName(), token));

        }
        return ResponseEntity.badRequest().build();
    }
}
