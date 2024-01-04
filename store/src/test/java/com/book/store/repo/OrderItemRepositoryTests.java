package com.book.store.repo;

import com.book.store.config.CustomMySqlContainer;
import com.book.store.config.TestDataInitializer;
import com.book.store.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
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
    public void shouldFindOrderItemsByOrderId() {
        // Given
        Order order = orderRepository.findAll().get(0);

        // When
        Set<OrderItem> orderItems = orderItemRepository.findAllByOrderId(order.getId());

        // Then
        Assertions.assertFalse(orderItems.isEmpty());
    }

    @Test
    @DisplayName("Should find OrderItem by Item ID and Order ID")
    public void shouldFindOrderItemByIdAndOrderId() {
        Order order = orderRepository.findAll().get(0);
        OrderItem orderItem = orderItemRepository.findAllByOrderId(order.getId()).stream().findFirst().orElse(null);

        Optional<OrderItem> foundOrderItem = orderItemRepository.findByIdAndOrderId(orderItem.getId(), order.getId());

        Assertions.assertTrue(foundOrderItem.isPresent());
    }

}
