package model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Order {
    private Long id;
    private Book book; // bạn để Book trực tiếp ở đây
    private int quantity;

    // để lưu nhiều item thì cần thêm list
    private List<CartItem> items = new ArrayList<>();

    public void addItem(Book book, int quantity) {
        items.add(new CartItem(null, book, quantity));
    }

    public Collection<CartItem> getItems() {
        return items;
    }
}
