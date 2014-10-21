package de.patrickbreucking.i2cagent.cumulocity;

public class TemperatureSensor {


    private UnitValue temperature0;
    private UnitValue temperature1;

    public void setTemperature0(UnitValue temperature0) {
        this.temperature0 = temperature0;
    }

    public UnitValue getTemperature0() {
        return temperature0;
    }

    public void setTemperature1(UnitValue temperature1) {
        this.temperature1 = temperature1;
    }

    public UnitValue getTemperature1() {
        return temperature1;
    }
}
