package com.microdrones.technical_test.Model;

import java.util.ArrayList;

public class MissionModel {

    private final ArrayList<Points> pointsList = new ArrayList<>();
    int totalPoints;
    private String name;
    private double horizontalSpeed;
    private double altitude;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getHorizontalSpeed() {
        return horizontalSpeed;
    }

    public void setHorizontalSpeed(double horizontalSpeed) {
        this.horizontalSpeed = horizontalSpeed;
    }

    public double getAltitude() {
        return altitude;
    }

    public void setAltitude(double altitude) {
        this.altitude = altitude;
    }

    public int getTotalPoints() {
        return totalPoints;
    }

    public Points getPoints(int pointIndex) {
        return pointIndex < this.pointsList.size() ? this.pointsList.get(pointIndex) : null;
    }

    public void setPointsList(Points points) {
        this.pointsList.add(points);
        totalPoints = this.pointsList.size();
    }

    public static class Points {
        private double latitude;
        private double longitude;

        public double getLatitude() {
            return latitude;
        }

        public void setLatitude(double latitude) {
            this.latitude = latitude;
        }

        public double getLongitude() {
            return longitude;
        }

        public void setLongitude(double longitude) {
            this.longitude = longitude;
        }
    }
}
