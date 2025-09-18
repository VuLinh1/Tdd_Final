package service;

import model.Book;
import model.Cart;
import model.CartItem;
import model.Order;
import model.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import repository.BookRepository;
import repository.OrderRepository;
import utils.RoleEnum;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class OrderServiceTest {

    @Mock
    private BookRepository bookRepository;

    @Mock
    private OrderRepository orderRepository;

    @InjectMocks
    private OrderService orderService;




    @Test
    void should_reduce_inventory_after_checkout() {
        User user =  User.builder()
                .id(1l)
                .phone("082384733")
                .role(RoleEnum.User)
                .email("linh@gmail.com")
                .address("ThaiBInh")
                .username("linhkk")
                .password("kkk")
                .build();        Book book = Book.builder()
                .id(1L)
                .title("Dirty Code")
                .author("NONAME")
                .publisher("CODE")
                .isbn("kkk")
                .price(20.0)
                .stockQuantity(10)
                .description("READ IT")
                .build();        Cart cart = new Cart(user);
        int expectedStockLeft = 6;
        cart.addItem(book, 4);
        user.setCart(cart);
        int expectedOrderItems  = 1;
        when(bookRepository.save(any())).thenAnswer(i -> i.getArguments()[0]);
        when(orderRepository.save(any())).thenAnswer(i -> i.getArguments()[0]);

        Order order = orderService.checkout(user);
        int actualStockLeft = book.getStockQuantity();
        int actualOrderItem = order.getItems().size();
        assertEquals(expectedStockLeft, actualStockLeft);
        assertTrue(user.getCartItems().isEmpty());
        assertEquals(expectedOrderItems ,actualOrderItem) ;
    }

    @Test
    void should_restore_inventory_after_cancel() {
        Book book = Book.builder()
                .id(1L)
                .title("Dirty Code")
                .author("NONAME")
                .publisher("CODE")
                .isbn("kkk")
                .price(20.0)
                .stockQuantity(20)
                .description("READ IT")
                .build();
        CartItem item = new CartItem(1l,book, 5);
        int expected = 25;
        Order order = new Order();
        order.addItem(book, 5);

        when(bookRepository.save(any())).thenAnswer(i -> i.getArguments()[0]);

        orderService.cancel(order);
        int actual = book.getStockQuantity();

        assertEquals(expected, actual);
        verify(orderRepository, times(1)).delete(order);
    }
    @Test
    void should_cancel_order_and_restore_stock() {
        // arrange
        int expectedStockRestock = 5;
        Book book = Book.builder()
                .id(3L)
                .title("Refactoring")
                .stockQuantity(2)
                .build();

        Order order = new Order();
        order.addItem(book, 3); // order  3 sach

        when(bookRepository.save(any(Book.class))).thenAnswer(i -> i.getArguments()[0]);

        // act
        orderService.cancel(order);
        int actual = book.getStockQuantity();
        // assert
        assertEquals(expectedStockRestock, actual);
        verify(orderRepository).delete(order);
    }

    @Test
    void should_throw_exception_when_cancel_empty_order() {
        Order order = new Order();

        assertThrows(IllegalArgumentException.class, () -> orderService.cancel(order));
    }
    @Test
    void should_throw_when_cart_is_null() {
        // arrange
        User user = User.builder()
                .id(1L)
                .username("Linh")
                .cart(null) // cart = null
                .build();

        // act + assert
        assertThrows(IllegalStateException.class, () -> orderService.checkout(user));
    }



}
