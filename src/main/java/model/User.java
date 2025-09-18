package model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import utils.RoleEnum;

import java.util.Collections;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class User {
    private long id;
    private String username;
    private String password;
    private String email;
    private String fullname;
    private String address;
    private String phone;
    private RoleEnum role;

    private Cart cart; // mỗi user có 1 cart



    public void setCart(Cart cart) {
        this.cart = cart;
    }
    public List<CartItem> getCartItems() {
        return this.cart != null ? this.cart.getItems() : Collections.emptyList();
    }
    
}
