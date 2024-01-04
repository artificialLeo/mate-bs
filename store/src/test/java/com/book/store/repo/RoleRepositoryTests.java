package com.book.store.repo;

import com.book.store.config.TestDataInitializer;
import com.book.store.model.Role;
import com.book.store.model.RoleName;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
public class RoleRepositoryTests {

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private TestDataInitializer testDataInitializer;

    @BeforeEach
    public void setUp() {
        testDataInitializer.initializeTestData();
    }

    @Test
    @DisplayName("Should find Role by Name")
    public void findByName_ExistingRoleName_ReturnRole() {
        RoleName roleName = RoleName.ROLE_USER;
        Role role = roleRepository.findByName(roleName);

        Assertions.assertNotNull(role);
        Assertions.assertEquals(roleName, role.getName());
    }

    @Test
    @DisplayName("Should not find Role by Non-Existing Name")
    public void findByName_NonExistingRoleName_ReturnNull() {
        RoleName existingRoleName = RoleName.ROLE_ADMIN;
        Role role = roleRepository.findByName(existingRoleName);

        Assertions.assertNotNull(role);
    }
}
