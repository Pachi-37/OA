package utils;

import org.junit.Test;

import static org.junit.Assert.*;

public class MD5UtilsTest {

    @Test
    public void md5Digest() {
        System.out.println(MD5Utils.md5Digest("test"));
    }

    @Test
    public void testMd5Digest() {

        for (int i = 171; i < 200; i++) {
            System.out.println(MD5Utils.md5Digest("test", i) + " " + i);
        }
    }
}