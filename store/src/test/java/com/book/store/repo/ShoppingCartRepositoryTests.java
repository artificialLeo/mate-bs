package com.book.store.repo;

import com.book.store.config.TestDataInitializer;
import com.book.store.model.ShoppingCart;
import com.book.store.model.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@SpringBootTest
@Transactional
public class ShoppingCartRepositoryTests {

    @Autowired
    private ShoppingCartRepository shoppingCartRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TestDataInitializer testDataInitializer;

    @BeforeEach
    public void setUp() {
        testDataInitializer.initializeTestData();
    }

    @Test
    @DisplayName("Should find ShoppingCart by User ID")
    public void findByUser_Id_ExistingUserId_ReturnShoppingCart() {
        User newUser = new User();
        newUser.setEmail("test@example.com");
        newUser.setFirstName("fn");
        newUser.setLastName("ln");
        newUser.setShippingAddress("LA");
        newUser.setPassword("testPassword");
        userRepository.save(newUser);

        ShoppingCart newShoppingCart = new ShoppingCart();
        newShoppingCart.setUser(newUser);
        shoppingCartRepository.save(newShoppingCart);

        Optional<User> userOptional = userRepository.findById(newUser.getId());
        User user = userOptional.orElseThrow();

        Optional<ShoppingCart> shoppingCartOptional = shoppingCartRepository.findByUser_Id(user.getId());
        ShoppingCart retrievedShoppingCart = shoppingCartOptional.orElseThrow();

        Assertions.assertEquals(retrievedShoppingCart.getUser(), user);
    }

    @Test
    @DisplayName("Should not find ShoppingCart by Non-Existing User ID")
    public void findByUser_Id_NonExistingUserId_ReturnEmptyOptional() {
        Long nonExistingUserId = 100L;

        Optional<ShoppingCart> shoppingCartOptional = shoppingCartRepository.findByUser_Id(nonExistingUserId);

        Assertions.assertTrue(shoppingCartOptional.isEmpty());
    }
}
