package de.patrickbreucking.i2cagent;

import c8y.lx.driver.PollingDriver;
import org.slf4j.LoggerFactory;

import java.util.logging.Logger;

public class MySensorDriver extends PollingDriver {

    private static org.slf4j.Logger logger = LoggerFactory.getLogger(MySensorDriver.class);

    public MySensorDriver() {
        super("BMP180", "c8y.barometer", 500);
    }


    @Override
    public void initialize() throws Exception {

        logger.info("Initialize MySensorDriver");

    }

    @Override
    public void run() {
        //To change body of implemented methods use File | Settings | File Templates.
    }
}
