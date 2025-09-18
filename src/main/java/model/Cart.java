package model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Cart {
    private Long id;
    private User user;
    private List<CartItem> items;

    public Cart(User user) {
    }


    public void addItem(Book book, int quantity) {
        if (items == null) {
            items = new ArrayList<>();
        }
        for (CartItem item : items) {
            if (item.getBook().getId().equals(book.getId())) {
                item.setQuantity(item.getQuantity() + quantity);
                return;
            }
        }
        // nếu chưa có thì tạo mới
        CartItem newItem = new CartItem(book, quantity);
        items.add(newItem);
    }



}
