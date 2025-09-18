package service;

import model.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import repository.UserRepository;
import utils.RoleEnum;

import javax.management.relation.Role;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks

    private UserService userService;

    @Test
    void should_login_success_with_valid_credentials() {
        // arrange
        User user = User.builder()
                .id(1l)
                .phone("082384733")
                .role(RoleEnum.User)
                .email("linh@gmail.com")
                .address("ThaiBInh")
                .username("linhkk")
                .password("kkk")
                .build();

        when(userRepository.findByEmail("linh@gmail.com"))
                .thenReturn(Optional.of(user));
        String expected = "linh@gmail.com";
        // act
        User loggedInUser = userService.login("linhkk", "kkk", "linh@gmail.com");
        String actual = loggedInUser.getEmail();
        // assert
        assertNotNull(loggedInUser);
        assertEquals(expected, actual);
    }

    @Test
    void should_throw_exception_when_login_with_wrong_password() {
        User user = User.builder()
                .id(1l)
                .phone("082384733")
                .role(RoleEnum.User)
                .email("linh@gmail.com")
                .address("ThaiBInh")
                .username("linhkk")
                .password("kkk")
                .build();

        when(userRepository.findByEmail("linh@gmail.com"))
                .thenReturn(Optional.of(user));

        assertThrows(RuntimeException.class,
                () -> userService.login("linh", "wrongpass", "linh@gmail.com"));
    }

    @Test
    void should_throw_exception_when_user_not_found() {
        // arrange
        when(userRepository.findByEmail("notfound@gmail.com"))
                .thenReturn(Optional.empty());

        // act + assert
        assertThrows(RuntimeException.class,
                () -> userService.login("someone", "12345", "notfound@gmail.com"));
    }

    @Test
    void should_throw_exception_when_password_is_null() {
        User user = User.builder()
                .id(2L)
                .username("linh")
                .password(null) // chưa set mật khẩu
                .email("kk@gmail.com")
                .role(RoleEnum.User)
                .build();

        when(userRepository.findByEmail("kk@gmail.com"))
                .thenReturn(Optional.of(user));

        assertThrows(RuntimeException.class,
                () -> userService.login("linh", "whatever", "kk@gmail.com"));
    }

    @Test
    void should_login_success_when_username_and_password_match() {
        String expectedEmail = "linhs@gmail.com";
        String expectedUsername = "linh";
        String expectedPassword = "linhs";
        User user = User.builder()
                .id(3L)
                .username("linh")
                .password("linhs")
                .email("linh@gmail.com")
                .role(RoleEnum.User)
                .build();

        when(userRepository.findByEmail("linh@gmail.com"))
                .thenReturn(Optional.of(user));

        User loggedInUser = userService.login("linh", "linhs", "linh@gmail.com");
        String actualUsername = loggedInUser.getUsername();
        String actualPassword = loggedInUser.getPassword();
        assertNotNull(loggedInUser);
        assertEquals(expectedUsername, actualUsername);
        assertEquals(expectedPassword, actualPassword);
    }
    @Test
    void should_throw_exception_when_password_is_empty_string() {
        User user = User.builder()
                .id(5L)
                .username("linh")
                .password("")
                .email("empty@gmail.com")
                .role(RoleEnum.User)
                .build();

        when(userRepository.findByEmail("empty@gmail.com"))
                .thenReturn(Optional.of(user));

        assertThrows(RuntimeException.class,
                () -> userService.login("linh", "abc", "empty@gmail.com"));
    }

}
