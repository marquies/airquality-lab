package de.patrickbreucking.i2cagent;

import c8y.Hardware;
import c8y.lx.driver.*;
import com.cumulocity.model.ID;
import com.cumulocity.model.idtype.GId;
import com.cumulocity.rest.representation.inventory.ManagedObjectRepresentation;
import com.cumulocity.rest.representation.measurement.MeasurementRepresentation;
import com.cumulocity.sdk.client.Platform;
import de.patrickbreucking.i2cagent.cumulocity.*;
import de.patrickbreucking.i2cagent.sensors.AdafruitBMP180;
import de.patrickbreucking.i2cagent.sensors.PollutionSensor;
import de.patrickbreucking.i2cagent.sensors.SHT15;
import de.patrickbreucking.i2cagent.sensors.TSL45315;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import se.hirt.pi.adafruitlcd.ILCD;
import se.hirt.pi.adafruitlcd.impl.MyRealLCD;
import se.hirt.pi.adafruitlcd.impl.RealLCD;

import java.io.IOException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class PiI2CAgent extends PollingDriver implements Driver {


    private static final String XTIDTYPE = "c8y_I2C";
    private static Logger logger = LoggerFactory.getLogger(PiI2CAgent.class);
    private DeviceManagedObject dmo;
    private Platform platform;
    private ManagedObjectRepresentation lightMo;
    private MeasurementRepresentation barometer = new MeasurementRepresentation();
    public static final String MOTION_TYPE = "c8y_Baromeeter";
    private AdafruitBMP180 barometerSensor;
    private ManagedObjectRepresentation mo;
    private PollutionSensor pollutionSensor;
    private TSL45315 lightSensor;
    private SHT15 sht15;
    private MyRealLCD lcd;
    private final NumberFormat NF = new DecimalFormat("00");


    public PiI2CAgent() throws IOException {
        super("BMP180", "c8y.barometer", 30000);
        logger.info("Called super!");
        init();
    }

    @Override
    public void initialize() throws Exception {
    }

    private void init() throws IOException {
        logger.info("Init!!");
        barometerSensor = new AdafruitBMP180();

        lcd = new MyRealLCD();
        lcd.setText(0, "Initializing...");


        pollutionSensor = new PollutionSensor();
        lightSensor = new TSL45315();
        sht15 = new SHT15();
        sht15.reset();

        lcd.clear();
        lcd.setText(0, "Init Done.");


        //PressureSensorRepresentationConverter converter = new PressureSensorRepresentationConverter();
        //platform.getConversionService().register(converter);
        //platform.getValidationService().register(converter);

    }

    @Override
    public void initialize(Platform platform) throws Exception {
        logger.info("Init with plattform");
        this.platform = platform;
        dmo = new DeviceManagedObject(platform);
    }

    @Override
    public OperationExecutor[] getSupportedOperations() {
        return new OperationExecutor[0];  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void initializeInventory(ManagedObjectRepresentation managedObjectRepresentation) {
        mo = managedObjectRepresentation;
        mo.getAttrs().put("c8y_SupportedMeasurements", new String[]{"PressureSensorRepresentation"});
        barometer.setSource(managedObjectRepresentation);
        barometer.setType(MOTION_TYPE);
    }

    @Override
    public void discoverChildren(ManagedObjectRepresentation managedObjectRepresentation) {
        String piSerial = managedObjectRepresentation.get(Hardware.class).getSerialNumber();
        String idPrefix = "raspberrypi-" + piSerial + "-";
        String namePrefix = "RaspPi " + piSerial.substring(8) + " ";

        createSensors(managedObjectRepresentation.getId(), idPrefix, namePrefix);
    }

    private void createSensors(GId parent, String idPrefix, String namePrefix) {
        lightMo = new ManagedObjectRepresentation();
        lightMo.setType("c8y_Bmp180");
        lightMo.setName(namePrefix + "Sensor BMP180");

        String id = idPrefix + "sensor-bmp180";

        logger.trace("Creating Sensor BMP180 {}", idPrefix);

        createOrUpdate(lightMo, id, parent);


    }

    private boolean createOrUpdate(ManagedObjectRepresentation mo, String id, GId parent) {
        ID extId = new ID(id);
        extId.setType(XTIDTYPE);
        return dmo.createOrUpdate(mo, extId, parent);
    }


    @Override
    public void run() {
        Date now = new Date();
        try {

            lcd.setText(0, "Transmitting...");

            int value = pollutionSensor.readValue();
            double lux = lightSensor.readValue();
            float press = 0;
            float temp = 0;
            double alt = 0;

            alt = barometerSensor.readAltitude();
            temp = barometerSensor.readTemperature();

            double temp2 = sht15.readTemperature();
            double humidity = sht15.readHumidity();

            float pressure = barometerSensor.readPressure();

            logger.debug("Sending data from pressure sensor.");

            PressureSensorRepresentation pressureSensorRepresentation = new PressureSensorRepresentation();
            pressureSensorRepresentation.setPressure(new UnitValue("hPa", pressure / 100.0));

            barometer = new MeasurementRepresentation();
            barometer.setSource(mo);
            barometer.setType("PressureSensorRepresentation");
            barometer.setTime(now);
            barometer.set(pressureSensorRepresentation);

            platform.getMeasurementApi().create(barometer);

            logger.debug("Sending data from AirQuality sensor.");

            AirQualitySensor airQualitySensor = new AirQualitySensor();
            airQualitySensor.setQuality(new UnitValue("relative", value));

            MeasurementRepresentation mr = new MeasurementRepresentation();
            mr.setSource(mo);
            mr.setType("AirQuality");
            mr.setTime(now);
            mr.set(airQualitySensor);

            platform.getMeasurementApi().create(mr);

            LightSensor lightSensor1 = new LightSensor();
            lightSensor1.setLight(new UnitValue("LUX", lux));

            mr = new MeasurementRepresentation();
            mr.setSource(mo);
            mr.setType("LightSensor");
            mr.setTime(now);
            mr.set(lightSensor1);

            platform.getMeasurementApi().create(mr);


            HumiditySensor humiditySensor = new HumiditySensor();
            humiditySensor.setHumidity(new UnitValue("%", humidity));

            mr = new MeasurementRepresentation();
            mr.setSource(mo);
            mr.setType("HumiditySensor");
            mr.setTime(now);
            mr.set(humiditySensor);

            platform.getMeasurementApi().create(mr);

            TemperatureSensor temperatureSensor = new TemperatureSensor();
            temperatureSensor.setTemperature0(new UnitValue("°C", temp));
            temperatureSensor.setTemperature1(new UnitValue("°C", temp2));

            mr = new MeasurementRepresentation();
            mr.setSource(mo);
            mr.setType("TemperatureSensor");
            mr.setTime(now);
            mr.set(temperatureSensor);

            platform.getMeasurementApi().create(mr);


            lcd.setText(0, "Last: " + new SimpleDateFormat("HH:mm:ss").format(new Date()));
            lcd.setText(1, "AQ:"+NF.format(value) + " H:" + NF.format(humidity) + " T:" + NF.format(temp2));


        } catch (Exception e) {
            // TODO: Split up each sensor exception
            logger.warn("Cannot create measurement on platform", e);

            try {
                lcd.setText("Resetting network.");
                Process process = Runtime.getRuntime().exec("sudo ifdown wlan0 && sudo ifup wlan0");
                lcd.setText("Reset done");
            } catch (IOException e1) {
                try {
                    lcd.clear();
                    lcd.setText("Network failed.");
                } catch (IOException e2) {
                    e2.printStackTrace();
                }

            }
        }
    }

}
