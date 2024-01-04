package com.book.store.config;

import com.book.store.model.Book;
import com.book.store.model.CartItem;
import com.book.store.model.Category;
import com.book.store.model.Order;
import com.book.store.model.OrderItem;
import com.book.store.model.Role;
import com.book.store.model.RoleName;
import com.book.store.model.ShoppingCart;
import com.book.store.model.Status;
import com.book.store.model.User;
import com.book.store.repo.BookRepository;
import com.book.store.repo.CartItemRepository;
import com.book.store.repo.CategoryRepository;
import com.book.store.repo.OrderItemRepository;
import com.book.store.repo.OrderRepository;
import com.book.store.repo.RoleRepository;
import com.book.store.repo.ShoppingCartRepository;
import com.book.store.repo.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
public class TestDataInitializer {
    private final BookRepository bookRepository;
    private final CartItemRepository cartItemRepository;
    private final CategoryRepository categoryRepository;
    private final OrderItemRepository orderItemRepository;
    private final OrderRepository orderRepository;
    private final RoleRepository roleRepository;
    private final ShoppingCartRepository shoppingCartRepository;
    private final UserRepository userRepository;
//    private static CustomMySqlContainer customMySqlContainer;


    public void initializeTestData() {
//        customMySqlContainer = CustomMySqlContainer.getInstance();
//        customMySqlContainer.start();

        createAndSaveTestBooks(5);
        createAndSaveTestUsers(5);
        User user = userRepository.findAll().get(0);
        createAndSaveTestOrders(user, 5);
        Book book = bookRepository.findAll().get(0);
        Order order = orderRepository.findAll().get(0);
        createAndSaveTestOrderItems(book, order, 5);
        createAndSaveTestCategories(3);
        createAndSaveTestRoles(3);
        createAndSaveTestShoppingCarts(3);
    }

    private User createRandomUser() {
        User user = new User();
        long randomSalt = System.currentTimeMillis();
        user.setEmail("random_user_" + randomSalt + "@example.com");
        user.setPassword("random_password_" + randomSalt);
        user.setFirstName("RandomFirstName" + randomSalt);
        user.setLastName("RandomLastName" + randomSalt);
        user.setShippingAddress("RandomAddress" + randomSalt);
        userRepository.save(user);
        return user;
    }

    private void createAndSaveTestBooks(int count) {
        for (int i = 0; i < count; i++) {
            Book book = new Book();
            book.setTitle("Test Book " + i);
            book.setAuthor("Test Author " + i);
            book.setIsbn("123456789012" + i);
            book.setPrice(BigDecimal.valueOf(29.99 + i));
            book.setDescription("Test Description " + i);
            book.setCoverImage("test_cover_image" + i + ".jpg");
            bookRepository.save(book);
        }
    }

    private void createAndSaveTestUsers(int count) {
        for (int i = 0; i < count; i++) {
            User user = new User();
            user.setEmail("example" + i + "@email.com");
            user.setPassword("password" + i);
            user.setFirstName("John" + i);
            user.setLastName("Doe" + i);
            user.setShippingAddress(i + " Main Street");
            userRepository.save(user);
        }
    }

    private void createAndSaveTestOrders(User user, int count) {
        for (int i = 0; i < count; i++) {
            Order order = new Order();
            order.setUser(user);
            order.setStatus(Status.PENDING);
            order.setTotal(BigDecimal.valueOf(11 + i));
            order.setOrderDate(LocalDateTime.now().minusDays(i));
            order.setShippingAddress("LA" + i);
            orderRepository.save(order);
        }
    }

    private void createAndSaveTestOrderItems(Book book, Order order, int count) {
        for (int i = 0; i < count; i++) {
            OrderItem orderItem = new OrderItem();
            orderItem.setBook(book);
            orderItem.setOrder(order);
            orderItem.setQuantity(2 + i);
            orderItem.setPrice(BigDecimal.valueOf(59.98 + i));
            orderItemRepository.save(orderItem);
        }
    }

    private void createAndSaveTestCartItems(User user, ShoppingCart shoppingCart, int count) {
        for (int i = 0; i < count; i++) {
            CartItem cartItem = new CartItem();
            cartItem.setShoppingCart(shoppingCart);

            Book book = bookRepository.findAll().get(i % bookRepository.findAll().size());
            cartItem.setBook(book);
            cartItem.setQuantity(1 + i);
            cartItemRepository.save(cartItem);
        }
    }

    private void createAndSaveTestCategories(int count) {
        List<Book> allBooks = bookRepository.findAll();

        for (int i = 0; i < count; i++) {
            Category category = new Category();
            category.setName("Test Category " + i);
            category.setDescription("Category Description " + i);
            categoryRepository.save(category);

            if (i == 0) {
                allBooks.get(0).getCategories().add(category);
            } else if (i == 1) {
                allBooks.get(1).getCategories().add(category);
            }
        }
    }

    private void createAndSaveTestRoles(int count) {
        Role user = new Role();
        user.setName(RoleName.ROLE_USER);
        roleRepository.save(user);
        Role admin = new Role();
        admin.setName(RoleName.ROLE_ADMIN);
        roleRepository.save(admin);
    }

    private void createAndSaveTestShoppingCarts(int count) {
        List<User> users = userRepository.findAll();
        for (int i = 0; i < count; i++) {
            ShoppingCart shoppingCart = new ShoppingCart();
            shoppingCart.setUser(users.get(i % users.size()));
            shoppingCartRepository.save(shoppingCart);
            createAndSaveTestCartItems(users.get(i % users.size()), shoppingCart, 3);
        }
    }

}
