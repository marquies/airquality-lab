package de.patricksteinert.airquality;

import com.pi4j.io.i2c.I2CBus;
import com.pi4j.io.i2c.I2CDevice;
import com.pi4j.io.i2c.I2CFactory;

import java.io.IOException;

/**
 * Created by Patrick Steinert on 04.01.17.
 */
public class Tsl45315 {
    private static final byte REG_CONTROL = 0x00;
    private static final byte REG_CONFIG = 0x01;
    private static final byte REG_DATALOW = 0x04;
    private static final byte REG_DATAHIGH = 0x05;
    private static final byte REG_ID = 0x0A;

    private I2CDevice light;

    public static void main(String[] args) throws IOException {
        Tsl45315 lightSensor = new Tsl45315();
        double lux = lightSensor.readValue();
        System.out.println("Light: " + lux + " LUX");
    }

    public Tsl45315() {

        I2CBus bus = null;
        try {
            bus = I2CFactory.getInstance(I2CBus.BUS_1);
            light = bus.getDevice(0x29);

            // Wake up
            light.write((byte) (0x80 | REG_CONTROL));
            light.write((byte) 0x03);

            // Set Speed
            light.write((byte) (0x80 | REG_CONFIG));
            light.write((byte) 0x00);
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    public double readValue() throws IOException {
        int lux;

        byte[] luxValue = new byte[2];
        light.read(0x80 | REG_DATALOW, luxValue, 0, 2);

        lux = unsignedToBytes(luxValue[1]) << 8 | unsignedToBytes(luxValue[0]);
        lux *= 1; //M=1

        return lux;
    }

    private static int unsignedToBytes(byte b) {
        return b & 0xFF;
    }
}
