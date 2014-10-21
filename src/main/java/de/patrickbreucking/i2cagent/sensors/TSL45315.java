package de.patrickbreucking.i2cagent.sensors;

import com.pi4j.io.i2c.I2CBus;
import com.pi4j.io.i2c.I2CDevice;
import com.pi4j.io.i2c.I2CFactory;
import org.apache.commons.codec.binary.Hex;

import java.io.IOException;
import java.nio.ByteBuffer;

public class TSL45315 {

    private static final byte REG_CONTROL = 0x00;
    private static final byte REG_CONFIG = 0x01;
    private static final byte REG_DATALOW = 0x04;
    private static final byte REG_DATAHIGH = 0x05;
    private static final byte REG_ID = 0x0A;

    private I2CDevice light;

    public TSL45315() {


        I2CBus bus = null;
        try {
            bus = I2CFactory.getInstance(I2CBus.BUS_1);
            light = bus.getDevice(0x29);

            //light.write((byte) (0x80|REG_ID));
            //Thread.sleep(8);


            //int tmp1 = light.read();


            // Wake up
            light.write((byte) (0x80 | REG_CONTROL));
            light.write((byte) 0x03);

            // Set Speed
            light.write((byte) (0x80 | REG_CONFIG));
            light.write((byte) 0x00);
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }


    }

    public double readValue() throws IOException {
        //uint16_t l, h;
        int lux;

        //Wire.beginTransmission(I2C_ADDR);
        //Wire.write(0x80|REG_DATALOW);
        //Wire.endTransmission();
        //Wire.requestFrom(I2C_ADDR, 2); //request 2 bytes

        //light.write((byte) (0x80|REG_DATALOW));

        byte[] luxValue = new byte[2];
        light.read(0x80 | REG_DATALOW, luxValue, 0, 2);



        lux = unsignedToBytes(luxValue[1]) << 8 | unsignedToBytes(luxValue[0]);
        lux *= 1; //M=1
        // lux *= 2; //M=2
        // lux *= 4; //M=4


        return lux;
    }

    private static int unsignedToBytes(byte b) {
        return b & 0xFF;
    }


}
