package com.book.store.repo;

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
public class UserRepositoryTests {
    @Autowired
    private UserRepository userRepository;

    User newUser;

    @BeforeEach
    public void setUp() {
        newUser = new User();
        newUser.setEmail("test@example.com");
        newUser.setFirstName("fn");
        newUser.setLastName("ln");
        newUser.setShippingAddress("LA");
        newUser.setPassword("testPassword");
        userRepository.save(newUser);
    }

    @Test
    @DisplayName("existsByEmail -> Exists true")
    @Transactional
    @Rollback
    public void existsByEmail_ExistingEmail_ReturnTrue() {
        boolean exists = userRepository.existsByEmail("test@example.com");

        Assertions.assertTrue(exists);
    }

    @Test
    @DisplayName("existsByEmail -> Exists false")
    @Transactional
    @Rollback
    public void existsByEmail_NonExistingEmail_ReturnFalse() {
        boolean exists = userRepository.existsByEmail("nonexistent@example.com");

        Assertions.assertFalse(exists);
    }

    @Test
    @DisplayName("findByEmail -> Email")
    @Transactional
    @Rollback
    public void findByEmail_ExistingEmail_ReturnUser() {
        Optional<User> foundUser = userRepository.findByEmail("test@example.com");
        Assertions.assertTrue(foundUser.isPresent());


        String expected = newUser.getEmail();
        String actual = foundUser.get().getEmail();
        Assertions.assertEquals(expected, actual);
    }

    @Test
    @Transactional
    @Rollback
    @DisplayName("findByEmail -> Empty true")
    public void findByEmail_NonExistingEmail_ReturnEmptyOptional() {
        Optional<User> foundUser = userRepository.findByEmail("nonexistent@example.com");

        Assertions.assertTrue(foundUser.isEmpty());
    }
}
