package service;

import model.Book;
import model.Cart;
import model.CartItem;
import model.Order;
import model.User;
import org.springframework.stereotype.Service;
import repository.BookRepository;
import repository.OrderRepository;
@Service
public class OrderService {
    private final BookRepository bookRepository;
    private final OrderRepository orderRepository;

    public OrderService(BookRepository bookRepository,
                        OrderRepository orderRepository) {
        this.bookRepository = bookRepository;
        this.orderRepository = orderRepository;
    }

    public Order checkout(User user) {
        Cart cart = user.getCart();
        if (cart == null ) {
            throw new IllegalStateException("Cart is empty");
        }

        Order order = new Order();
        for (CartItem item : cart.getItems()) {
            Book book = item.getBook();
            int qty = item.getQuantity();

            // Trừ tồn kho
            book.setStockQuantity(book.getStockQuantity() - qty);
            bookRepository.save(book);

            // Thêm vào order
            order.addItem(book, qty);
        }

        // Lưu order
        orderRepository.save(order);

        // Xóa cart sau khi checkout
        cart.getItems().clear();

        return order;
    }

    // 2. Cancel order
    public void cancel(Order order) {
        if (order == null || order.getItems().isEmpty()) {
            throw new IllegalArgumentException("Order is empty or invalid");
        }

        // Hoàn lại stock
        order.getItems().forEach(item -> {
            Book book = item.getBook();
            int qty = item.getQuantity();
            book.setStockQuantity(book.getStockQuantity() + qty);
            bookRepository.save(book);
        });

        // Xóa order
        orderRepository.delete(order);
    }
}
