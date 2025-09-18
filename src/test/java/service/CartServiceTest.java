package service;

import model.Book;
import model.Cart;
import model.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import repository.BookRepository;
import repository.CartRepository;
import utils.RoleEnum;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CartServiceTest {

    @Mock
    private BookRepository bookRepository;

    @Mock
    private CartRepository cartRepository;

    @InjectMocks
    private CartService cartService;

    @Test
    void should_add_full_quantity_when_stock_is_enough() {
        User user =  User.builder()
                .id(1l)
                .phone("082384733")
                .role(RoleEnum.User)
                .email("linh@gmail.com")
                .address("ThaiBInh")
                .username("linhkk")
                .password("kkk")
                .build();
        Book book = Book.builder()
                .id(1L)
                .title("Dirty Code")
                .author("NONAME")
                .publisher("CODE")
                .isbn("kkk")
                .price(20.0)
                .stockQuantity(20)
                .description("READ IT")
                .build();        int expected = 5;
        when(bookRepository.findbyId(1L)).thenReturn(Optional.of(book));
        when(cartRepository.findByUser(user)).thenReturn(Optional.empty());
        when(cartRepository.save(any())).thenAnswer(i -> i.getArguments()[0]);

        Cart cart = cartService.addToCart(user, 1L, 5);
        int actual = cart.getItems().get(0).getQuantity();

        assertEquals(expected,actual );
    }

    @Test
    void should_add_max_stock_when_request_exceeds_stock() {
        User user =  User.builder()
                .id(1l)
                .phone("082384733")
                .role(RoleEnum.User)
                .email("linh@gmail.com")
                .address("ThaiBInh")
                .username("linhkk")
                .password("kkk")
                .build();
        Book book = Book.builder()
                .id(1L)
                .title("Dirty Code")
                .author("NONAME")
                .publisher("CODE")
                .isbn("kkk")
                .price(20.0)
                .stockQuantity(3)
                .description("READ IT")
                .build();
        int expected = 3;
        when(bookRepository.findbyId(2L)).thenReturn(Optional.of(book));
        when(cartRepository.findByUser(user)).thenReturn(Optional.empty());
        when(cartRepository.save(any())).thenAnswer(i -> i.getArguments()[0]);

        Cart cart = cartService.addToCart(user, 2L, 10);
        int actual = cart.getItems().get(0).getQuantity();

        assertEquals(expected, actual); // chỉ cho mua tối đa stock
    }
    @Test
    void should_throw_exception_when_book_not_found() {
        User user = User.builder()
                .id(1L)
                .username("testUser")
                .role(RoleEnum.User)
                .build();

        when(bookRepository.findbyId(99L)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> {
            cartService.addToCart(user, 99L, 2);
        });
    }
    @Test
    void should_throw_exception_when_quantity_is_zero_or_negative() {
        User user = User.builder()
                .id(1L)
                .username("linhll")
                .role(RoleEnum.User)
                .build();

        Book book = Book.builder()
                .id(1L)
                .title("Nothing")
                .stockQuantity(10)
                .price(50.0)
                .build();

        when(bookRepository.findbyId(1L)).thenReturn(Optional.of(book));

        // quantity = 0
        assertThrows(IllegalArgumentException.class,
                () -> cartService.addToCart(user, 1L, 0));

        // quantity = -5
        assertThrows(IllegalArgumentException.class,
                () -> cartService.addToCart(user, 1L, -5));
    }

    @Test
    void should_throw_when_book_not_found() {
        // arrange
        User user = User.builder()
                .id(1L)
                .username("linh")
                .build();

        when(bookRepository.findbyId(99L)).thenReturn(Optional.empty());

        // act + assert
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> cartService.addToCart(user, 99L, 2));

        assertTrue(ex.getMessage().contains("Book not found with id = 99"));
    }
    @Test
    void should_update_quantity_when_book_already_in_cart() {
        // arrange
        Book book = Book.builder().id(1L).title("Clean Code").build();
        Cart cart = new Cart();
        cart.addItem(book, 2);

        // act
        cart.addItem(book, 3); // thêm cùng book

        // assert
        assertEquals(1, cart.getItems().size());
        assertEquals(5, cart.getItems().get(0).getQuantity()); // số lượng cộng dồn
    }

    @Test
    void should_add_new_item_when_book_not_in_cart() {
        // arrange
        Book book1 = Book.builder().id(1L).title("Clean Code").build();
        Book book2 = Book.builder().id(2L).title("Refactoring").build();
        Cart cart = new Cart();
        cart.addItem(book1, 2);

        // act
        cart.addItem(book2, 3); // book khác

        // assert
        assertEquals(2, cart.getItems().size()); // có 2 items
        assertEquals(2, cart.getItems().get(0).getQuantity());
        assertEquals(3, cart.getItems().get(1).getQuantity());
    }


}