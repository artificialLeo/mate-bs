package com.book.store.repo;

import com.book.store.model.Book;
import com.book.store.model.Order;
import com.book.store.model.OrderItem;
import com.book.store.model.Status;
import com.book.store.model.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
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

    private Order order;
    private OrderItem orderItem;

    @BeforeEach
    public void setUp() {
        User user = new User();
        user.setEmail("user@example.com");
        user.setPassword("password");
        user.setFirstName("John");
        user.setLastName("Doe");
        user.setShippingAddress("123 Main St");
        userRepository.save(user);

        Book book = new Book();
        book.setTitle("Example Book");
        book.setAuthor("Author");
        book.setIsbn("1234567890123");
        book.setPrice(BigDecimal.valueOf(29.99));
        book.setDescription("Book Description");
        book.setCoverImage("book_cover.jpg");
        bookRepository.save(book);

        order = new Order();
        order.setUser(user);
        order.setStatus(Status.PENDING);
        order.setTotal(BigDecimal.valueOf(59.98));
        order.setOrderDate(LocalDateTime.now());
        order.setShippingAddress("Shipping Address");
        orderRepository.save(order);

        orderItem = new OrderItem();
        orderItem.setOrder(order);
        orderItem.setBook(book);
        orderItem.setQuantity(2);
        orderItem.setPrice(BigDecimal.valueOf(29.99));
        orderItemRepository.save(orderItem);
    }

    @Test
    @DisplayName("findAllByOrderId -> Set size")
    public void findAllByOrderId_PresentIdPassed_ReturnSetSize() {
        Set<OrderItem> orderItems = orderItemRepository.findAllByOrderId(order.getId());

        int expected = 1;
        int actual = orderItems.size();
        Assertions.assertEquals(expected, actual);
    }

    @Test
    @DisplayName("findAllByOrderId -> OrderItem Id")
    public void findAllByOrderId_PresentIdPassed_ReturnId() {
        Set<OrderItem> orderItems = orderItemRepository.findAllByOrderId(order.getId());

        long expected = 2L;
        long actual = orderItems.stream().findFirst().orElseThrow().getId();
        Assertions.assertEquals(expected, actual);
    }

    @Test
    @DisplayName("findByIdAndOrderId -> OrderItem Id")
    public void findByIdAndOrderId_OrderItemPassed_ReturnOrderItem() {
        Optional<OrderItem> foundOrderItem = orderItemRepository.findByIdAndOrderId(orderItem.getId(), order.getId());
        Assertions.assertTrue(foundOrderItem.isPresent());

        OrderItem actualOrderItem = foundOrderItem.get();

        long expected = orderItem.getId();
        long actual = actualOrderItem.getId();
        Assertions.assertEquals(expected, actual);
    }

    @Test
    @DisplayName("findByIdAndOrderId -> Order Id")
    public void findByIdAndOrderId_OrderItemPassed_ReturnOrder() {
        Optional<OrderItem> foundOrderItem = orderItemRepository.findByIdAndOrderId(orderItem.getId(), order.getId());
        Assertions.assertTrue(foundOrderItem.isPresent());

        OrderItem actualOrderItem = foundOrderItem.get();

        long expected = order.getId();
        long actual = actualOrderItem.getOrder().getId();
        Assertions.assertEquals(expected, actual);
    }

}
