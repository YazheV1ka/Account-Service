package account.service;

import account.exception.NotValidPasswordException;
import account.exception.SamePasswordException;
import account.model.User;
import account.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class UserService {

    private UserRepository userRepository;

    private PasswordEncoder passwordEncoder;

    private List<String> hackerDatabase = List.of(
            "PasswordForJanuary", "PasswordForFebruary", "PasswordForMarch",
            "PasswordForApril", "PasswordForMay", "PasswordForJune",
            "PasswordForJuly", "PasswordForAugust", "PasswordForSeptember",
            "PasswordForOctober", "PasswordForNovember", "PasswordForDecember");

    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email.toLowerCase());
    }
    public User save(User user) {

        String rawPassword = user.getPassword();

        if(hackerDatabase.contains(rawPassword)) {
            throw new NotValidPasswordException();
        }
        user.setEmail(user.getEmail().toLowerCase());
        user.setPassword(passwordEncoder.encode(rawPassword));
        return userRepository.save(user);
    }

    public User changePassword(User user, String newRawPassword) {
        if(passwordEncoder.matches(newRawPassword, user.getPassword())){
            throw new SamePasswordException();
        }
        user.setPassword(newRawPassword);
        return save(user);
    }
}