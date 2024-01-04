package com.book.store.repo;

import com.book.store.config.TestDataInitializer;
import com.book.store.model.Order;
import com.book.store.model.OrderItem;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.Set;

@SpringBootTest
@Transactional
public class OrderItemRepositoryTests {
    @Autowired
    private OrderItemRepository orderItemRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TestDataInitializer testDataInitializer;

    @BeforeEach
    public void setUp() {
        testDataInitializer.initializeTestData();
    }

    @Test
    @DisplayName("Should find OrderItems by Order ID")
    public void findAllByOrderId_PresentIdPassed_ReturnSet() {
        Order order = orderRepository.findAll().get(0);
        Set<OrderItem> orderItems = orderItemRepository.findAllByOrderId(order.getId());

        long expectedOrderId = 1L;
        long actualOrderId = order.getId();
        Assertions.assertEquals(expectedOrderId, actualOrderId);

        int expectedSize = 5;
        int actualSize = orderItems.size();
        Assertions.assertEquals(expectedSize, actualSize);
    }

    @Test
    @DisplayName("Should find OrderItem by Item ID and Order ID")
    public void findByIdAndOrderId_OrderItemPassed_ReturnOrderItem() {
        Order order = orderRepository.findAll().get(0);
        OrderItem orderItem = orderItemRepository.findAllByOrderId(order.getId()).stream().findFirst().orElse(null);

        Optional<OrderItem> foundOrderItem = orderItemRepository.findByIdAndOrderId(orderItem.getId(), order.getId());

        Assertions.assertTrue(foundOrderItem.isPresent());
    }

}
