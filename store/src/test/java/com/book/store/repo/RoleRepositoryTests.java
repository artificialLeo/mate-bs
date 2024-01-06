package com.book.store.repo;

import com.book.store.model.Role;
import com.book.store.model.RoleName;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
public class RoleRepositoryTests {
    @Autowired
    private RoleRepository roleRepository;

    private Role roleUser;

    @BeforeEach
    public void setUp() {
        roleUser = new Role();
        roleUser.setName(RoleName.ROLE_USER);
        roleRepository.save(roleUser);
    }

    @Test
    @DisplayName("findByName -> RoleName")
    @Transactional
    @Rollback
    public void findByName_ExistingRoleName_ReturnRole() {
        RoleName expected = RoleName.ROLE_USER;
        RoleName actual = roleRepository.findByName(expected).getName();

        Assertions.assertNotNull(actual);
        Assertions.assertEquals(expected, actual);
    }
}
