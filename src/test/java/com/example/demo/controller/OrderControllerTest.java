package com.example.demo.controller;

import com.example.demo.TestUtils;
import com.example.demo.controllers.OrderController;
import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.UserOrder;
import com.example.demo.model.persistence.repositories.OrderRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class OrderControllerTest {
    private UserRepository userRepository = mock(UserRepository.class);
    private OrderRepository orderRepository = mock(OrderRepository.class);
    private OrderController orderController;

    @Before
    public void setUp() {
        orderController = new OrderController();
        TestUtils.injectObjects(orderController, "userRepository", userRepository);
        TestUtils.injectObjects(orderController, "orderRepository", orderRepository);
    }

    @Test
    public void submitHappyPath() {
        User user = setUserItemCart();
        when(userRepository.findByUsername("test")).thenReturn(user);

        ResponseEntity<UserOrder> response = orderController.submit("test");
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        UserOrder returnedOrder = response.getBody();
        assertNotNull(returnedOrder);
        assertEquals(1, returnedOrder.getItems().size());
    }

    @Test
    public void submitUserNotFound() {
        User user = setUserItemCart();

        when(userRepository.findByUsername("notFound")).thenReturn(null);
        ResponseEntity<UserOrder> response = orderController.submit("notFound");
        assertNotNull(response);
        assertEquals(404, response.getStatusCodeValue());
    }

    @Test
    public void getOrdersForUserHappyPath() {
        User user = setUserItemCart();
        when(userRepository.findByUsername("test")).thenReturn(user);

        ResponseEntity<List<UserOrder>> ordersForUser = orderController.getOrdersForUser("test");
        assertNotNull(ordersForUser);
        assertEquals(200, ordersForUser.getStatusCodeValue());
        List<UserOrder> returnedOrders = ordersForUser.getBody();
        assertNotNull(returnedOrders);
    }

    @Test
    public void getOrdersForUserNotFound() {
        User user = setUserItemCart();

        when(userRepository.findByUsername("notFound")).thenReturn(null);
        ResponseEntity<List<UserOrder>> response = orderController.getOrdersForUser("notFound");
        assertNotNull(response);
        assertEquals(404, response.getStatusCodeValue());
    }

    private User setUserItemCart() {
        Item item = new Item();
        item.setId(0L);
        item.setName("Test");
        BigDecimal price = BigDecimal.valueOf(100);
        item.setPrice(price);
        item.setDescription("description");
        List<Item> items = new ArrayList<Item>();
        items.add(item);

        User user = new User();
        Cart cart = new Cart();
        user.setId(0L);
        user.setUsername("test");
        user.setPassword("testPassword");
        cart.setId(0L);
        cart.setUser(user);
        cart.setItems(items);
        BigDecimal total = BigDecimal.valueOf(100);
        cart.setTotal(total);
        user.setCart(cart);
        return user;
    }

}
