package com.book.store.service;

import com.book.store.model.User;
import com.book.store.repo.UserRepository;
import com.book.store.service.impl.UserDetailsServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserDetailsServiceImplTests {
    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserDetailsServiceImpl userDetailsService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("loadUserByUsername -> User Found -> Return UserDetails")
    void loadUserByUsername_UserFound_ReturnUserDetails() {
        String userEmail = "test@example.com";
        User user = new User();
        user.setEmail("test@example.com");
        user.setFirstName("fn");
        user.setLastName("ln");
        user.setShippingAddress("LA");
        user.setPassword("testPassword");
        when(userRepository.findByEmail(userEmail)).thenReturn(Optional.of(user));

        UserDetails userDetails = userDetailsService.loadUserByUsername(userEmail);

        assertNotNull(userDetails);
        assertEquals(user.getUsername(), userDetails.getUsername());
        assertEquals(user.getPassword(), userDetails.getPassword());
        assertTrue(userDetails.isEnabled());
        assertTrue(userDetails.isAccountNonExpired());
        assertTrue(userDetails.isCredentialsNonExpired());
        assertTrue(userDetails.isAccountNonLocked());

        verify(userRepository, times(1)).findByEmail(userEmail);
    }

    @Test
    @DisplayName("loadUserByUsername -> User Not Found -> Throw UsernameNotFoundException")
    void loadUserByUsername_UserNotFound_ThrowUsernameNotFoundException() {
        String userEmail = "nonexistent@example.com";
        when(userRepository.findByEmail(userEmail)).thenReturn(Optional.empty());

        assertThrows(UsernameNotFoundException.class, () -> userDetailsService.loadUserByUsername(userEmail));

        verify(userRepository, times(1)).findByEmail(userEmail);
    }
}
