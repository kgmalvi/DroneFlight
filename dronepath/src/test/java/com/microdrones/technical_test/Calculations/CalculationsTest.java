package com.microdrones.technical_test.Calculations;

import static org.junit.Assert.assertThrows;

import junit.framework.TestCase;

import org.junit.jupiter.api.Test;

public class CalculationsTest extends TestCase {

    double Precision = 0.001;

    /**
     * Testing haversine formula.
     * Calculated the distance between these points manually using online tools and providing it here to verify if calculations are correct.
     */
    public void testHaversine() {

        double lat1 = 53.478612, lon1 = 6.250578, lat2 = 50.752342, lon2 = 5.916981;

        double expectedDistance = 304.0010210; // meters

        double resultDistance = Calculations.haversine(lat1, lon1, lat2, lon2);

        assertEquals(expectedDistance, resultDistance, Precision);
        /*double distance = Calculations.haversine(53.478612, 6.250578, 50.752342, 5.916981);
        assertEquals(304.0010210, distance, Precision);*/
    }

    @Test
    void distanceNetherlandsNorthToSouthWithPrecision() {

        double lat1 = 53.478612, lon1 = 6.250578, lat2 = 50.752342, lon2 = 5.916981;

        double expectedDistance = 304001.021; // meters

        double resultDistance = Calculations.haversine(lat1, lon1, lat2, lon2);

        assertEquals(expectedDistance, resultDistance, Precision);
    }

    @Test
    void distanceAustraliaWestToEast() {
        double distance = Calculations.haversine(-23.939607, 113.585605, -28.293166, 153.718989);
        assertEquals(4018083.0398, distance, Precision);
    }

    @Test
    void distanceFromCapetownToJohannesburg() {
        double distance = Calculations.haversine(-33.926510, 18.364603, -26.208450, 28.040572);
        assertEquals(1265065.6094, distance, Precision);
    }

    @Test
    void distanceFromSantiagoToSaoPaolo() {
        double distance = Calculations.haversine(-33.464087, -70.660573, -23.553981, -46.630563);
        assertEquals(2585109.9646, distance, Precision);
    }

    @Test
    void distanceIsTheSameIfMeasuredInBothDirections() {
        // testing that distance is the same in whatever direction we measure
        double distanceDirection1 = Calculations.haversine(-33.926510, 18.364603, -26.208450, 28.040572);
        double distanceDirection2 = Calculations.haversine(-26.208450, 28.040572, -33.926510, 18.364603);
        assertEquals(1265065.6094, distanceDirection1, Precision);
        assertEquals(1265065.6094, distanceDirection2, Precision);
    }


    //
    // Corner cases
    //

    // Distance around 180th meridian - read more about this problem here:
    // https://smartpuffin.com/little-geo-stories-a-tale-of-180th-meridian
    @Test
    void distanceAround180thMeridianFiji() {
        double distance = Calculations.haversine(-17.947826, 177.221232, -16.603513, -179.779055);
        assertEquals(351826.7740, distance, Precision);
    }

    @Test
    void distanceFrom180ToMinus180LongitudeShouldBeZero() {
        // Because -180 and 180 longitude is essentially the same, the distance should be zero.
        double distance = Calculations.haversine(20, -180, 20, 180);
        assertEquals(0, distance, Precision);
    }

    @Test
    void distanceAround0thMeridianLondon() {
        double distance = Calculations.haversine(51.512722, -0.288552, 51.516100, 0.068025);
        assertEquals(24677.4562, distance, Precision);
    }

    // How about poles? Let's look at a couple of distances in the Arctic and Antarctica.
    @Test
    void distanceAround90LatitudeArctic() {
        double distance = Calculations.haversine(89.9, -179.9, 89.9, 179.9);
        assertEquals(38.8143, distance, Precision);
    }

    @Test
    void distance90LatitudeNorthPole() {
        // This is the North Pole. When latitude = 90 or -90,
        // longitude doesn't matter - all meridians meet in this point.
        // So with both latitudes = 90, the distance is effectively 0.
        double distance = Calculations.haversine(90, -179.9, 90, 179.9);
        assertEquals(0, distance, Precision);
    }

    @Test
    void distanceAroundMinus90LatitudeAntarctica() {
        // This is the South Pole. When latitude = 90 or -90,
        // Longitude doesn't matter - all meridians meet in this point.
        // So with both latitudes = -90, the distance is effectively 0.
        double distance = Calculations.haversine(-89.9, -179.9, -89.9, 179.9);
        assertEquals(38.8143, distance, Precision);
    }

    // How about from one pole to another? This is max distance on the planet
    @Test
    void distanceFromNorthPoleToSouthPole() {
        double distance = Calculations.haversine(90, 10, -90, 10);
        assertEquals(20015086.7960, distance, Precision);
    }

    // Max distance on the planet along the equator
    @Test
    void maxDistanceAlongTheEquator() {
        double distance = Calculations.haversine(0, 0, 0, 180);
        assertEquals(20015086.7960, distance, Precision);
    }

    @Test
    void maxDistanceAlongTheEquator2() {
        double distance = Calculations.haversine(0, 10, 0, -170);
        assertEquals(20015086.7960, distance, Precision);
    }

    @Test
    void maxDistanceAlongTheEquator3() {
        double distance = Calculations.haversine(0, 175, 0, -5);
        assertEquals(20015086.7960, distance, Precision);
    }

    // How about a really small distance?
    @Test
    void verySmallDistance() {
        double distance = Calculations.haversine(0, 0, 0.00000001, 0);
        assertEquals(0.001, distance, Precision);
    }

    // If both points are absolutely the same, are we getting 0 m distance?
    @Test
    void distanceBetweenTheSamePointsIs0() {
        double distance = Calculations.haversine(42, 99, 42, 99);
        assertEquals(0, distance);
    }

    @Test
    void distanceBetweenTheSamePointsIs0_2() {
        double distance = Calculations.haversine(-42, -99, -42, -99);
        assertEquals(0, distance);
    }

    @Test
    void distanceBetweenTheSamePoints_0_0_Is0() {
        // Equator meets Greenwich
        double distance = Calculations.haversine(0, 0, 0, 0);
        assertEquals(0, distance);
    }

    //
    // Negative cases
    //

    @Test
    void invalidLatitude1TooMuch() {
        assertThrows(IllegalArgumentException.class, () -> {
            Calculations.haversine(666, 0, 0, 0);
        });
    }

    @Test
    void invalidLatitude1TooLittle() {
        assertThrows(IllegalArgumentException.class, () -> {
            Calculations.haversine(-666, 0, 0, 0);
        });
    }

    @Test
    void invalidLatitude2TooMuch() {
        assertThrows(IllegalArgumentException.class, () -> {
            Calculations.haversine(0, 0, 666, 0);
        });
    }

    @Test
    void invalidLatitude2TooLittle() {
        assertThrows(IllegalArgumentException.class, () -> {
            Calculations.haversine(0, 0, -666, 0);
        });
    }

    @Test
    void invalidLongitude1TooMuch() {
        assertThrows(IllegalArgumentException.class, () -> {
            Calculations.haversine(0, 666, 0, 0);
        });
    }

    @Test
    void invalidLongitude1TooLittle() {
        assertThrows(IllegalArgumentException.class, () -> {
            Calculations.haversine(0, -666, 0, 0);
        });
    }

    @Test
    void invalidLongitude2TooMuch() {
        assertThrows(IllegalArgumentException.class, () -> {
            Calculations.haversine(0, 0, 0, 666);
        });
    }

    @Test
    void invalidLongitude2TooLittle() {
        assertThrows(IllegalArgumentException.class, () -> {
            Calculations.haversine(0, 0, 0, -666);
        });
    }
}