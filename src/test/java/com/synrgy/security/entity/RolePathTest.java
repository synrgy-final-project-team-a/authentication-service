package com.synrgy.security.entity;

import org.junit.Before;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.Assert.*;

@SpringBootTest
public class RolePathTest {

    private RolePath rolePath;

    @Before
    public void setUp() {
        rolePath = new RolePath();
        rolePath.setName("Test Role Path");
        rolePath.setPattern("/test/path");
        rolePath.setMethod("GET");
    }

    @Test
    public void testGetName() {
        assertEquals("Test Role Path", rolePath.getName());
    }

    @Test
    public void testSetName() {
        rolePath.setName("Test Role Path Updated");
        assertEquals("Test Role Path Updated", rolePath.getName());
    }

    @Test
    public void testGetPattern() {
        assertEquals("/test/path", rolePath.getPattern());
    }

    @Test
    public void testSetPattern() {
        rolePath.setPattern("/updated/test/path");
        assertEquals("/updated/test/path", rolePath.getPattern());
    }

    @Test
    public void testGetMethod() {
        assertEquals("GET", rolePath.getMethod());
    }

    @Test
    public void testSetMethod() {
        rolePath.setMethod("POST");
        assertEquals("POST", rolePath.getMethod());
    }
}

