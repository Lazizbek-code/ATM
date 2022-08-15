package uz.pdp.appatm.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import uz.pdp.appatm.entity.Card;
import uz.pdp.appatm.entity.Role;
import uz.pdp.appatm.entity.User;
import uz.pdp.appatm.entity.enums.RoleName;
import uz.pdp.appatm.payload.ApiResponse;
import uz.pdp.appatm.payload.LoginDto;
import uz.pdp.appatm.payload.RegisterDto;
import uz.pdp.appatm.repositpry.CardRepository;
import uz.pdp.appatm.repositpry.RoleRepository;
import uz.pdp.appatm.repositpry.UserRepository;
import uz.pdp.appatm.security.JwtProvider;

import java.util.*;

@Service
public class AuthService implements UserDetailsService {
    @Autowired
    UserRepository userRepository;
    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    RoleRepository roleRepository;
    @Autowired
    JavaMailSender javaMailSender;
    @Autowired
    AuthenticationManager authenticationManager;
    @Autowired
    JwtProvider jwtProvider;
    @Autowired
    CardRepository cardRepository;


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
    public ApiResponse register(RegisterDto registerDto) {
        // tizimda shunday email yoqligini tekshiradi
        boolean existsByEmail = userRepository.existsByEmail(registerDto.getEmail());
        if (existsByEmail) {
            return new ApiResponse("This Email already exist", false);
        }

        User user = new User();
        user.setFirstName(registerDto.getFirstName());
        user.setLastName(registerDto.getLastName());
        user.setEmail(registerDto.getEmail());
        user.setPassword(passwordEncoder.encode(registerDto.getPassword()));
        user.setEmailCode(UUID.randomUUID().toString());

        // tizimdagi userni ushlab oldim
        User userInSystem = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        // agar roli director bo'lsa directorga o'zgaradi
        RoleName roleName = RoleName.MONEY_SUPPLIER;

        // rolini aniqladim
        for (Role role : userInSystem.getRole()) {
            if (role.getRoleName().equals(RoleName.DIRECTOR)) {
                roleName = role.getRoleName();
                break;
            }
        }

        // registerDto dagi rollarni oldim
        Set<Role> roles = new HashSet<>();
        for (Integer roleId : registerDto.getRoleId()) {
            Optional<Role> optionalRole = roleRepository.findById(roleId);
            optionalRole.ifPresent(roles::add);
        }

        // userga rollarni set qildim
        if (roleName.equals(RoleName.DIRECTOR)) {
            user.setRole(roles);
        } else {
            user.setRole(Collections.singleton(new Role(RoleName.MONEY_SUPPLIER)));
        }

        // userni saqladim
        User savedUser = userRepository.save(user);

        // send message to email
        sendEmail(savedUser.getEmail(), savedUser.getEmailCode());
        return new ApiResponse("Successfully registered, verify your account", true);
    }


    /**
     * VERIFY USER ACCOUNT
     *
     * @param emailCode   String
     * @param email       String
     * @param loginDto email(String),
     *                    password(String)
     * @return ApiResponse in ResponseEntity
     */
    public ApiResponse verifyAccount(String email, String emailCode, LoginDto loginDto) {
        Optional<User> optionalUser = userRepository.findByEmailAndEmailCode(email, emailCode);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            user.setEnabled(true);
            user.setEmailCode(null);
            user.setPassword(passwordEncoder.encode(loginDto.getPassword()));
            userRepository.save(user);

            return new ApiResponse("Account is verified", true);
        }
        return new ApiResponse("Account already verified", false);
    }


    /**
     * LOGIN TO SYSTEM
     *
     * @param loginDto email(String),
     *                 password(String)
     * @return ApiResponse in ResponseEntity
     */
    public ApiResponse login(LoginDto loginDto) {
        try {
            Authentication authenticate = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                    loginDto.getEmail(), loginDto.getPassword()));

            User user = (User) authenticate.getPrincipal();
            String token = jwtProvider.generateToken(user.getUsername(), user.getRole());

            return new ApiResponse("Token", true, token);
        } catch (BadCredentialsException e) {
            return new ApiResponse("Password or login is incorrect", false);
        }
    }


    // send message to user email
    public void sendEmail(String sendingEmail, String emailCode) {
        try {
            SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
            simpleMailMessage.setFrom("email.sender.hr@gmail.com");
            simpleMailMessage.setTo(sendingEmail);
            simpleMailMessage.setSubject("Verify account");
            simpleMailMessage.setText("http://localhost:8080/api/auth/verifyAccount?emailCode=" + emailCode + "&email=" + sendingEmail);
            javaMailSender.send(simpleMailMessage);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return userRepository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

    public UserDetails loadCardBySpecialCode(String specialCode) {
        return cardRepository.findBySpecialCode(specialCode).orElseThrow(() -> new UsernameNotFoundException("Card not found"));
    }
}
