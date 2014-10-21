package de.patrickbreucking.i2cagent;

import de.patrickbreucking.i2cagent.sensors.AdafruitBMP180;
import de.patrickbreucking.i2cagent.sensors.PollutionSensor;
import de.patrickbreucking.i2cagent.sensors.SHT15;
import de.patrickbreucking.i2cagent.sensors.TSL45315;
import se.hirt.pi.adafruitlcd.impl.RealLCD;

import java.io.IOException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.concurrent.TimeUnit;

public class GetAllValues {

    public static void main(String[] args) throws IOException, InterruptedException {

        RealLCD lcd = new RealLCD();

        lcd.clear();
        lcd.setText(0, "Hello World!");


        lcd.setAutoScrollEnabled(true);
        lcd.setText(1, "Very Long String Lorem Ipsum");

        System.out.println("I2C Connecting");


        PollutionSensor ps = new PollutionSensor();
        TSL45315 lightSensor = new TSL45315();
        AdafruitBMP180 sensor = new AdafruitBMP180();
        SHT15 sht15 = new SHT15();
        sht15.reset();

        float press = 0;
        float temp = 0;
        double alt = 0;

        final NumberFormat NF = new DecimalFormat("##00.00");

        for (int i = 0; i < 10; i++) {

            int value = ps.readValue();
            double lux = lightSensor.readValue();
            try {
                press = sensor.readPressure();
                sensor.setStandardSeaLevelPressure((int) press); // As we ARE at the sea level (in San Francisco).
                alt = sensor.readAltitude();
                temp = sensor.readTemperature();
            } catch (Exception ex) {
                System.err.println(ex.getMessage());
                ex.printStackTrace();
            }

            double temp2 = sht15.readTemperature();
            double humidity = sht15.readHumidity();

            System.out.println("Temperature 1:" + NF.format(temp2) + " C");
            System.out.println("Humidity:     " + NF.format(humidity) + "%");
            System.out.println("AirQuality:   " + NF.format(value) + " ADC");
            System.out.println("Light:        " + NF.format(lux) + " LUX");
            System.out.println("Temperature 2:" + NF.format(temp) + " C");
            System.out.println("Pressure   :  " + NF.format(press / 100) + " hPa");
            System.out.println("Altitude   :  " + NF.format(alt) + " m");

            TimeUnit.SECONDS.sleep(2);

        }


    }
}
