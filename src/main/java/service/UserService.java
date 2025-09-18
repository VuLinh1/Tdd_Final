package service;

import lombok.RequiredArgsConstructor;
import model.User;
import org.springframework.stereotype.Service;
import repository.UserRepository;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public User login(String username, String password, String email) {
        // login bằng email
        Optional<User> userOpt = userRepository.findByEmail(email);

        User user = userOpt.orElseThrow(() -> new RuntimeException("User not found"));

        if (!user.getUsername().equals(username)) {
            throw new RuntimeException("Invalid username");
        }
        if (!user.getPassword().equals(password)) {
            throw new RuntimeException("Invalid password");
        }

        return user;
    }

}
