package com.microdrones.technical_test.Model;

public class ConfigurationModel {

    private double ascension;
    private double descent;
    private double numberOfBatteries;
    private double capacity;
    private double additionalLoad;

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

    public double getNumberOfBatteries() {
        return numberOfBatteries;
    }

    public void setNumberOfBatteries(double numberOfBatteries) {
        this.numberOfBatteries = numberOfBatteries;
    }

    public double getCapacity() {
        return capacity;
    }

    public void setCapacity(double capacity) {
        this.capacity = capacity;
    }

    public double getAdditionalLoad() {
        return additionalLoad;
    }

    public void setAdditionalLoad(double additionalLoad) {
        this.additionalLoad = additionalLoad;
    }
}
