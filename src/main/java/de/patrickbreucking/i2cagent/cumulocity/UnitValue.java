package de.patrickbreucking.i2cagent.cumulocity;

public class UnitValue {
    public void setUnit(String unit) {
        this.unit = unit;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    private String unit;
    private Object value;

    public UnitValue() {

    }

    public UnitValue(String unit, Object value) {
        this.unit = unit;
        this.value = value;
    }

    public String getUnit() {
        return unit;
    }

    public Object getValue() {
        return value;
    }
}
