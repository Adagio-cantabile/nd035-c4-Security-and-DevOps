package com.example.demo.controller;

import com.example.demo.TestUtils;
import com.example.demo.controllers.CartController;
import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.UserOrder;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.ItemRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.ModifyCartRequest;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class CartControllerTest {
    private CartController cartController;
    private UserRepository userRepository = mock(UserRepository.class);
    private CartRepository cartRepository = mock(CartRepository.class);
    private ItemRepository itemRepository = mock(ItemRepository.class);

    @Before
    public void setUp() {
        cartController = new CartController();
        TestUtils.injectObjects(cartController, "userRepository", userRepository);
        TestUtils.injectObjects(cartController, "cartRepository", cartRepository);
        TestUtils.injectObjects(cartController, "itemRepository", itemRepository);
    }

    @Test
    public void addToCartHappyPath() {
        setCart();
        ModifyCartRequest r = new ModifyCartRequest();
        r.setItemId(0L);
        r.setQuantity(1);
        r.setUsername("test");
        ResponseEntity<Cart> response = cartController.addToCart(r);

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        Cart returnedCart = response.getBody();
        assertNotNull(returnedCart);
        assertEquals(BigDecimal.valueOf(100), returnedCart.getTotal());
    }

    @Test
    public void addToCartUserNotFound() {
        setCart();
        ModifyCartRequest r = new ModifyCartRequest();
        r.setItemId(0L);
        r.setQuantity(1);
        r.setUsername("notFound");
        ResponseEntity<Cart> response = cartController.addToCart(r);

        assertNotNull(response);
        assertEquals(404, response.getStatusCodeValue());
    }

    @Test
    public void addToCartItemNotFound() {
        ModifyCartRequest r = new ModifyCartRequest();
        r.setItemId(1L);
        r.setQuantity(1);
        r.setUsername("test");
        ResponseEntity<Cart> response = cartController.addToCart(r);

        assertNotNull(response);
        assertEquals(404, response.getStatusCodeValue());
    }

    @Test
    public void removeFromCartHappyPath() {
        setCart();

        ModifyCartRequest r = new ModifyCartRequest();
        r.setItemId(0L);
        r.setQuantity(1);
        r.setUsername("test");
        ResponseEntity<Cart> response = cartController.removeFromCart(r);

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        Cart returnedCart = response.getBody();

        assertNotNull(returnedCart);
        assertEquals(0, returnedCart.getItems().size());
    }

    @Test
    public void removeFromCartUserNotFound() {
        setCart();

        ModifyCartRequest r = new ModifyCartRequest();
        r.setItemId(0L);
        r.setQuantity(1);
        r.setUsername("notFound");
        ResponseEntity<Cart> response = cartController.removeFromCart(r);

        assertNotNull(response);
        assertEquals(404, response.getStatusCodeValue());
    }

    @Test
    public void removeFromCartItemNotFound() {
        ModifyCartRequest r = new ModifyCartRequest();
        r.setItemId(1L);
        r.setQuantity(1);
        r.setUsername("test");
        ResponseEntity<Cart> response = cartController.removeFromCart(r);

        assertNotNull(response);
        assertEquals(404, response.getStatusCodeValue());
    }

    private Cart setCart() {
        User user = new User();
        Cart cart = new Cart();
        user.setId(0L);
        user.setUsername("test");
        user.setPassword("testPassword");
        user.setCart(cart);
        when(userRepository.findByUsername("test")).thenReturn(user);
        when(itemRepository.findByName("notFound")).thenReturn(null);

        Item item = new Item();
        item.setId(0L);
        item.setName("test");
        BigDecimal price = BigDecimal.valueOf(100);
        item.setPrice(price);
        item.setDescription("description");
        when(itemRepository.findById(0L)).thenReturn(java.util.Optional.of(item));
        return cart;
    }
}
