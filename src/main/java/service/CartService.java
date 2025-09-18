package service;

import model.Book;
import model.Cart;
import model.User;
import org.springframework.stereotype.Service;
import repository.BookRepository;
import repository.CartRepository;

import java.util.Optional;

@Service
public class CartService {
    private final BookRepository bookRepository;
    private final CartRepository cartRepository;

    public CartService(BookRepository bookRepository, CartRepository cartRepository) {
        this.bookRepository = bookRepository;
        this.cartRepository = cartRepository;
    }

    public Cart addToCart(User user, long bookId, int quantity) {
        Optional<Book> optionalBook = bookRepository.findbyId(bookId);
        if (optionalBook.isEmpty()) {
            throw new IllegalArgumentException("Book not found with id = " + bookId);
        }
        Book book = optionalBook.get();

        int allowedQuantity = Math.min(quantity, book.getStockQuantity());
        if (allowedQuantity <= 0) {
            throw new IllegalArgumentException("Book is out of stock");
        }

        Cart cart = cartRepository.findByUser(user).orElse(new Cart(user));

        cart.addItem(book, allowedQuantity);

        cartRepository.save(cart);

        return cart;
    }
}
