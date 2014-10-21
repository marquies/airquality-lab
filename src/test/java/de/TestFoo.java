package de;

import org.testng.Assert;
import org.testng.annotations.Test;

public class TestFoo {


    @Test
    public void testFoo() throws Exception {
        byte[] bytes = {(byte)0xc3, (byte)0x03};


        Assert.assertEquals(unsignedToBytes(bytes[0]), 0xc3);

        int foo = unsignedToBytes(bytes[1]) << 8 | unsignedToBytes(bytes[0]);

        Assert.assertEquals(foo, 963);

    }

    public static int unsignedToBytes(byte b) {
        return b & 0xFF;
    }

}
