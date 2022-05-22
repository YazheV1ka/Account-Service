package account.controller;

import account.exception.PasswordLengthIsLessThan12Exception;
import account.exception.UserAlreadyExistException;
import account.model.User;
import account.service.UserService;
import account.util.NewPasswordRequest;
import account.util.NewPasswordResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;
import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private UserService userService;

    @Autowired
    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/signup")
    public User signup(@RequestBody @Valid User userRequest) {
        String userEmail = userRequest.getEmail().toLowerCase();
        log.info("credentials received to register: {} {}", userEmail, userRequest.getPassword());

        Optional<User> userOptional = userService.findByEmail(userEmail);
        if (userOptional.isPresent()) {
            throw new UserAlreadyExistException();
        } else {
            User user = userService.save(userRequest);
            log.info("User saved: {}", user);
            return user;
        }
    }

    @PostMapping("/changepass")
    public NewPasswordResponse changePassword(
            Authentication auth, @RequestBody @Valid NewPasswordRequest request, BindingResult result) {
        String rawPassword = request.getNewPassword();
        String userEmail = auth.getPrincipal().toString();
        log.info("authenticated user changing password: {}", userEmail);
        User user = userService.findByEmail(userEmail).get();

        if(result.hasErrors()){
            throw new PasswordLengthIsLessThan12Exception();
        }
        userService.changePassword(user, rawPassword);

        NewPasswordResponse response = new NewPasswordResponse();
        response.setEmail(userEmail);
        response.setStatus("The password has been updated successfully");
        return response;
    }
}