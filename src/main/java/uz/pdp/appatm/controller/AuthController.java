package uz.pdp.appatm.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.pdp.appatm.payload.ApiResponse;
import uz.pdp.appatm.payload.LoginDto;
import uz.pdp.appatm.payload.RegisterDto;
import uz.pdp.appatm.service.AuthService;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    @Autowired
    AuthService authService;


    /**
     * REGISTER USERS IN THE SYSTEM
     *
     * @param registerDto firstName(String),
     *                    lastName(String),
     *                    email(String),
     *                    password(String),
     *                    roleId(Integer)
     * @return ApiResponse in ResponseEntity
     */
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterDto registerDto) {
        ApiResponse apiResponse = authService.register(registerDto);
        return ResponseEntity.status(apiResponse.isSuccess() ? 201 : 409).body(apiResponse);
    }


    /**
     * LOGIN TO SYSTEM
     *
     * @param loginDto email(String),
     *                 password(String)
     * @return ApiResponse in ResponseEntity
     */
    @PostMapping("/login")
    public ResponseEntity<?> signIn(@RequestBody LoginDto loginDto) {
        ApiResponse apiResponse = authService.login(loginDto);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }


    /**
     * VERIFY USER ACCOUNT
     *
     * @param emailCode   String
     * @param email       String
     * @param loginDto    email(String),
     *                    password(String)
     * @return ApiResponse in ResponseEntity
     */
    @PostMapping("/verifyAccount")
    public ResponseEntity<?> verify(@RequestParam String emailCode, @RequestParam String email, @RequestBody LoginDto loginDto) {
        ApiResponse apiResponse = authService.verifyAccount(email, emailCode, loginDto);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }
}
