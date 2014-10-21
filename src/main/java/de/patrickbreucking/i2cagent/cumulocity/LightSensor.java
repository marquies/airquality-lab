package de.patrickbreucking.i2cagent.cumulocity;

public class LightSensor {
    private UnitValue light;

    public void setLight(UnitValue light) {
        this.light = light;
    }

    public UnitValue getLight() {
        return light;
    }
}
