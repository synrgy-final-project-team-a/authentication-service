package com.synrgy.security.entity.enumeration;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class EnumRoleTest {

    @Test
    public void testEnumRoleValues() {
        EnumRole[] roles = EnumRole.values();
        assertEquals(6, roles.length);
        assertEquals(EnumRole.ROLE_TN, roles[0]);
        assertEquals(EnumRole.ROLE_TNT, roles[1]);
        assertEquals(EnumRole.ROLE_SK, roles[2]);
        assertEquals(EnumRole.ROLE_SUPERUSER, roles[3]);
        assertEquals(EnumRole.ROLE_WRITE, roles[4]);
        assertEquals(EnumRole.ROLE_READ, roles[5]);
    }
}
