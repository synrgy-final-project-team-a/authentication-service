package com.synrgy.security.entity;

import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.Assert.assertEquals;

@SpringBootTest
public class RoleTest {

    @Test
    public void testRoleCreation() {
        Role role = new Role();
        role.setName("ADMIN");
        role.setType("ADMIN_TYPE");
        assertEquals("ADMIN", role.getName());
        assertEquals("ADMIN_TYPE", role.getType());
    }

    @Test
    public void testRoleAuthority() {
        Role role = new Role();
        role.setName("ADMIN");
        assertEquals("ADMIN", role.getAuthority());
    }

}
