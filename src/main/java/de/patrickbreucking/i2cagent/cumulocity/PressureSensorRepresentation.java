package de.patrickbreucking.i2cagent.cumulocity;

public class PressureSensorRepresentation {


    private UnitValue pressure;
    public void setPressure(UnitValue pressure) {
        this.pressure = pressure;
    }

    public UnitValue getPressure() {
        return pressure;
    }
}
