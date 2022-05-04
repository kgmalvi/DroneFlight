package com.microdrones.technical_test.DataDeclaration;

public class DataConstants {

    public class Configuration {
        public static final String verticalSpeeds = "verticalSpeeds";
        // the vertical speed of the drone during a descent in meter / seconds (m/s)
        public static final String ascension = "ascension";
        // the vertical speed of the drone during an ascension in meter / seconds (m/s)
        public static final String descent = "descent";

        public static final String energy = "energy";
        // is the number of batteries mounted on the drone
        public static final String numberOfBatteries = "numberOfBatteries";
        // is the capacity of the battery in milli Ampere Hour (mAh)
        public static final String capacity = "capacity";

        public static final String payload = "payload";
        // is the extra load in in Ampere x Seconds ² / Meters ² (A.s²/m² ) caused by the payload
        public static final String additionalLoad = "additionalLoad";
    }

    public class Drone {
        // currentLoadInFlight: in Ampere x Seconds ² / Meters ² (A.s²/m² )
        //  The current load in A per square unit of speed ((m/s)²) on the drone power system when it is flying
        public static final String currentLoadInFlight = "currentLoadInFlight";
        // currentLoadInFlight In vertical ascension
        public static final String ascension = "ascension";
        // currentLoadInFlight In vertical descent
        public static final String descent = "descent";
        // currentLoadInFlight \In horizontal translation
        public static final String translation = "translation";
        // in milli Ampere Hour (mAh)
        // When the available charge is the batteries is less or equal to [forcedLandingCharge], the drone will land
        public static final String forcedLandingCharge = "forcedLandingCharge";
    }

    public class Mission {
        public static final String name = "name";
        // the horizontal speed of the drone during the flight in meter / seconds (m/s)
        public static final String horizontalSpeed = "horizontalSpeed";
        // the flying altitude in meters (m)
        public static final String altitude = "altitude";
        // is a ordered array of geographical coordinates representing the flight path
        public static final String points = "points";
        public static final String latitude = "latitude";
        public static final String longitude = "longitude";
    }


}
