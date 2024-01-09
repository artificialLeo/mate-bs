package com.book.store.repo;

import com.book.store.model.ShoppingCart;
import com.book.store.model.User;
import java.util.Optional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
public class ShoppingCartRepositoryTests {
    @Autowired
    private ShoppingCartRepository shoppingCartRepository;

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    public void setUp() {
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
    }

    @Test
    @DisplayName("findByUser_Id -> User")
    @Transactional
    @Rollback
    public void findByUser_Id_ExistingUserId_ReturnUserShoppingCart() {
        Optional<User> userOptional = userRepository.findByEmail("test@example.com");
        Assertions.assertTrue(userOptional.isPresent());
        User actual = userOptional.get();

        Optional<ShoppingCart> shoppingCartOptional = shoppingCartRepository.findByUser_Id(actual.getId());
        Assertions.assertTrue(shoppingCartOptional.isPresent());
        User expected = shoppingCartOptional.get().getUser();

        Assertions.assertEquals(expected, actual);
    }

    @Test
    @DisplayName("findByUser_Id -> Empty True")
    @Transactional
    @Rollback
    public void findByUser_Id_NonExistingUserId_ReturnEmptyOptional() {
        Long nonExistingUserId = 100L;

        Optional<ShoppingCart> shoppingCartOptional = shoppingCartRepository.findByUser_Id(nonExistingUserId);

        Assertions.assertTrue(shoppingCartOptional.isEmpty());
    }
}
