package de.patrickbreucking.i2cagent.cumulocity;

public class HumiditySensor  {
    private UnitValue humidity;


    public void setHumidity(UnitValue humidity) {
        this.humidity = humidity;
    }

    public UnitValue getHumidity() {
        return humidity;
    }
}
