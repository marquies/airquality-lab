package de.patrickbreucking.i2cagent.cumulocity;

public class AirQualitySensor  {

    private UnitValue quality;

    public void setQuality(UnitValue quality) {
        this.quality = quality;
    }

    public UnitValue getQuality() {
        return quality;
    }
}
