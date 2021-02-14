package com.example.demo.controller;

import com.example.demo.TestUtils;
import com.example.demo.controllers.ItemController;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.ItemRepository;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ItemControllerTest {
    private ItemController itemController;
    private ItemRepository itemRepository = mock(ItemRepository.class);

    @Before
    public void setUp() {
        itemController = new ItemController();
        TestUtils.injectObjects(itemController, "itemRepository", itemRepository);
    }

    @Test
    public void getItems() {
        Item expectedItem = setItem();
        when(itemRepository.findAll()).thenReturn(Collections.singletonList(expectedItem));

        ResponseEntity<List<Item>> response = itemController.getItems();
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        List<Item> items = response.getBody();
        assertNotNull(items);
        assertEquals(1, items.size());
    }

    @Test
    public void getItemByIdHappyPath() {
        Item expectedItem = setItem();
        when(itemRepository.findById(0L)).thenReturn(java.util.Optional.of(expectedItem));

        ResponseEntity<Item> response = itemController.getItemById(0L);
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        Item returnedItem = response.getBody();
        assertNotNull(returnedItem);
        assertEquals("test", returnedItem.getName());
        assertEquals("description", returnedItem.getDescription());
        assertEquals(BigDecimal.valueOf(100), returnedItem.getPrice());
    }

    @Test
    public void getItemByIdNotFound() {
        ResponseEntity<Item> response = itemController.getItemById(1L);
        assertNotNull(response);
        assertEquals(404, response.getStatusCodeValue());
    }

    @Test
    public void getItemsByNameHappyPath() {
        Item expectedItem = setItem();
        when(itemRepository.findByName("test")).thenReturn(Collections.singletonList(expectedItem));

        ResponseEntity<List<Item>> response = itemController.getItemsByName("test");
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        List<Item> items = response.getBody();
        assertNotNull(items);
        assertEquals(1, items.size());
    }

    @Test
    public void getItemsByNameNotFound() {
        ResponseEntity<List<Item>> response = itemController.getItemsByName("test");
        assertNotNull(response);
        assertEquals(404, response.getStatusCodeValue());
    }

    private Item setItem () {
        long id = 0L;
        Item item = new Item();
        item.setName("test");
        item.setDescription("description");
        item.setPrice(BigDecimal.valueOf(100));
        return item;
    }
}
