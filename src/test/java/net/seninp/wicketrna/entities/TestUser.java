package net.seninp.wicketrna.entities;

import static org.junit.Assert.*;
import org.junit.Test;

public class TestUser {

  private static final String UNAME1 = "uname1";
  private static final String SALT1 = "salt1";
  private static final String EMAIL1 = "email1";
  private static final String UFOLDER1 = "/Users/u1";
  private static final String KV1 = "k1:v1;k2:v2";

  private static final String UNAME2 = "uname2";
  private static final String SALT2 = "salt2";
  private static final String EMAIL2 = "email2";
  private static final String UFOLDER2 = "/Users/u2";
  private static final String KV2 = "k7:v7;k3:v3";

  @Test
  public void testUser() {
    User u1 = new User(0, UNAME1, SALT1, EMAIL1, UFOLDER1, KV1);

    User u1d1 = new User(0, UNAME1, SALT1, EMAIL1, UFOLDER1, KV1);

    User u2 = new User(1, UNAME2, SALT2, EMAIL2, UFOLDER2, KV2);

    assertTrue(u1.equals(u1d1));
    assertEquals(u1.hashCode(), u1d1.hashCode());

    assertFalse(u1.equals(u2));
    assertNotEquals(u1.hashCode(), u2.hashCode());

    //
    //
    assertEquals(Integer.valueOf(1), u2.getId());
    u2.setId(0);
    assertEquals(Integer.valueOf(0), u2.getId());

    assertTrue(UNAME2.equalsIgnoreCase(u2.getUsername()));
    u2.setUsername(UNAME1);
    assertTrue(UNAME1.equalsIgnoreCase(u2.getUsername()));

    assertTrue(EMAIL2.equalsIgnoreCase(u2.getEmail()));
    u2.setEmail(EMAIL1);
    assertTrue(EMAIL1.equalsIgnoreCase(u2.getEmail()));

    assertTrue(SALT2.equalsIgnoreCase(u2.getSalt()));
    u2.setSalt(SALT1);
    assertTrue(SALT1.equalsIgnoreCase(u2.getSalt()));

    assertTrue(UFOLDER2.equalsIgnoreCase(u2.getUser_folder()));
    u2.setUser_folder(UFOLDER1);
    assertTrue(UFOLDER1.equalsIgnoreCase(u2.getUser_folder()));

    assertTrue(KV2.equalsIgnoreCase(u2.getKey_values()));
    u2.setKey_values(KV1);
    assertTrue(KV1.equalsIgnoreCase(u2.getKey_values()));

    assertTrue(u1.equals(u2));
    assertEquals(u1.hashCode(), u2.hashCode());

  }

}
