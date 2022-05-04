package com.microdrones.technical_test.Model;

public class DroneModel {

    private double ascension;
    private double descent;
    private double translation;
    private double forcedLandingCharge;

    public double getAscension() {
        return ascension;
    }

    public void setAscension(double ascension) {
        this.ascension = ascension;
    }

    public double getDescent() {
        return descent;
    }

    public void setDescent(double descent) {
        this.descent = descent;
    }

    public double getTranslation() {
        return translation;
    }

    public void setTranslation(double translation) {
        this.translation = translation;
    }

    public double getForcedLandingCharge() {
        return forcedLandingCharge;
    }

    public void setForcedLandingCharge(double forcedLandingCharge) {
        this.forcedLandingCharge = forcedLandingCharge;
    }
}
