package com.book.store.repo;

import com.book.store.config.TestDataInitializer;
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
public class UserRepositoryTests {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TestDataInitializer testDataInitializer;

    User newUser;

    @BeforeEach
    public void setUp() {
        testDataInitializer.initializeTestData();

        newUser = new User();
        newUser.setEmail("test@example.com");
        newUser.setFirstName("fn");
        newUser.setLastName("ln");
        newUser.setShippingAddress("LA");
        newUser.setPassword("testPassword");
        userRepository.save(newUser);
    }

    @Test
    @DisplayName("Should check if user exists by email")
    public void existsByEmail_ExistingEmail_ReturnTrue() {
        boolean exists = userRepository.existsByEmail("test@example.com");

        Assertions.assertTrue(exists);
    }

    @Test
    @DisplayName("Should check if user does not exist by email")
    public void existsByEmail_NonExistingEmail_ReturnFalse() {
        boolean exists = userRepository.existsByEmail("nonexistent@example.com");

        Assertions.assertFalse(exists);
    }

    @Test
    @DisplayName("Should find user by email")
    public void findByEmail_ExistingEmail_ReturnUser() {
        Optional<User> foundUser = userRepository.findByEmail("test@example.com");

        Assertions.assertTrue(foundUser.isPresent());
        Assertions.assertEquals(newUser.getEmail(), foundUser.get().getEmail());
    }

    @Test
    @DisplayName("Should not find user by non-existing email")
    public void findByEmail_NonExistingEmail_ReturnEmptyOptional() {
        Optional<User> foundUser = userRepository.findByEmail("nonexistent@example.com");

        Assertions.assertTrue(foundUser.isEmpty());
    }
}
