package account;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api")
public class Controller {

    private final UserService userService;

    @Autowired
    public Controller(UserService userService) {
        this.userService = userService;
    }

    @PostMapping(path = "auth/signup")
    public ResponseEntity<User> registerUser(@Valid @RequestBody User user) {
        return new ResponseEntity(
                userService.saveUser(user),
                HttpStatus.OK);
    }

    @GetMapping(path = "empl/payment")
    public ResponseEntity<User> getUser(@AuthenticationPrincipal UserDetails details) {
        return new ResponseEntity(
                userService.getUser(details.getUsername()),
                HttpStatus.OK);
    }
}
