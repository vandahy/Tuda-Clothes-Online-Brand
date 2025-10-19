package chubby.teu.tuda.feature.auth.controller;

import chubby.teu.tuda.feature.auth.Component.JwtTokenProvider;
import chubby.teu.tuda.feature.auth.dto.login.LoginRequest;
import chubby.teu.tuda.feature.auth.dto.login.LoginResponse;
import chubby.teu.tuda.feature.auth.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api")
public class LoginController {
    @Autowired
    private LoginService userService;
    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody @Valid LoginRequest loginRequest) {
        boolean success = userService.checkLogin(loginRequest.getUsername(), loginRequest.getPassword());

        if (success) {
            String token = jwtTokenProvider.generateToken(loginRequest.getUsername());
            return ResponseEntity.ok(new LoginResponse(true, "Login successful",token));
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new LoginResponse(false, "Invalid credentials"));
        }
    }

}