package se.hirt.pi.adafruitlcd.impl;

import com.sun.corba.se.spi.activation.IIOP_CLEAR_TEXT;

import java.io.IOException;

public class MyRealLCD extends RealLCD {

    private static final String CLEAR_TEXT = "                ";

    public MyRealLCD() throws IOException {
        super();
    }

    public MyRealLCD(int bus, int address) throws IOException {
        super(bus, address);
    }

    @Override
    public void setText(int row, String string) throws IOException {
        clear(row);
        setCursorPosition(row, 0);
        internalWrite(string);
    }

    public void clear(int row) throws IOException {
        setCursorPosition(row, 0);
        internalWrite(CLEAR_TEXT);
    }
}
