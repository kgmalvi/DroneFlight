package com.microdrones.technical_test.Calculations;

import com.microdrones.technical_test.ClassFunctions.PrintLog;
import com.microdrones.technical_test.Model.CompleteFlyingDataModel;

public class Calculations {
    private static final String TAG = "Calculations";

    /**
     * This API check the drone flight on given path with given parameters is possible or not.
     *
     * @return true if mission can be flown on given route.
     * false: if mission can not be flown on given route.
     */
    public static boolean checkIfFlightPossible() {

        // Step 1: Calculate the energy required for horizontal flight

        // i: Calculate horizontal flying distance.
        double flyingDistance = 0;
        double flyingTime = 0;
        double currentLoadInFlight = 0;
        double horizontalLoadInFlight = 0;
        for (int i = 0; i < CompleteFlyingDataModel.getInstance().getMissionModel().getTotalPoints() - 1; i++) {
            flyingDistance += haversine(CompleteFlyingDataModel.getInstance().getMissionModel().getPoints(i).getLatitude(),
                    CompleteFlyingDataModel.getInstance().getMissionModel().getPoints(i).getLongitude(),
                    CompleteFlyingDataModel.getInstance().getMissionModel().getPoints(i + 1).getLatitude(),
                    CompleteFlyingDataModel.getInstance().getMissionModel().getPoints(i + 1).getLongitude());
        }
        PrintLog.Log(TAG, "Flying Distance: " + flyingDistance + " meters.");

        // ii: FlightTime = flyingDistance * Speed
        flyingTime = flyingDistance *
                CompleteFlyingDataModel.getInstance().getMissionModel().getHorizontalSpeed();
        PrintLog.Log(TAG, "Horizontal Flying Time: " + flyingTime);

        // iii. currentLoadInFlight = currentLoadInFlight + additionalLoad
        currentLoadInFlight = CompleteFlyingDataModel.getInstance().getConfigurationModel().getAdditionalLoad()
                + CompleteFlyingDataModel.getInstance().getDroneModel().getTranslation();
        horizontalLoadInFlight = currentLoadInFlight;

        // iv: horizontalEnergy = flyingTime * currentLoadInFlight * speed²

        double horizontalEnergy = flyingTime * currentLoadInFlight *
                Math.pow(CompleteFlyingDataModel.getInstance().getMissionModel().getHorizontalSpeed(), 2);
        PrintLog.Log("Horizontal Energy: " + horizontalEnergy);


        // Step 2: Calculate the energy required for Ascend flight
        // i: FlightTime = flyingDistance * Speed
        flyingTime = CompleteFlyingDataModel.getInstance().getMissionModel().getAltitude() *
                CompleteFlyingDataModel.getInstance().getConfigurationModel().getAscension();

        // ii. currentLoadInFlight = currentLoadInFlight + additionalLoad
        currentLoadInFlight = CompleteFlyingDataModel.getInstance().getConfigurationModel().getAdditionalLoad()
                + CompleteFlyingDataModel.getInstance().getDroneModel().getAscension();

        // iii: ascendEnergy = flyingTime * currentLoadInFlight * speed²
        double ascendEnergy = flyingTime * currentLoadInFlight *
                Math.pow(CompleteFlyingDataModel.getInstance().getConfigurationModel().getAscension(), 2);
        PrintLog.Log("Ascending Energy: " + ascendEnergy);


        // Step 3: Calculate the energy required for Descend flight
        // i: FlightTime = flyingDistance * Speed
        flyingTime = CompleteFlyingDataModel.getInstance().getMissionModel().getAltitude() *
                CompleteFlyingDataModel.getInstance().getConfigurationModel().getDescent();

        // ii. currentLoadInFlight = currentLoadInFlight + additionalLoad
        currentLoadInFlight = CompleteFlyingDataModel.getInstance().getConfigurationModel().getAdditionalLoad()
                + CompleteFlyingDataModel.getInstance().getDroneModel().getDescent();

        // iii: ascendEnergy = flyingTime * currentLoadInFlight * speed²
        double descendEnergy = flyingTime * currentLoadInFlight *
                Math.pow(CompleteFlyingDataModel.getInstance().getConfigurationModel().getDescent(), 2);
        PrintLog.Log("Descending Energy: " + descendEnergy);

        // Step 4: Calculate the total energy required for the flight.
        double totalEnergyRequired = horizontalEnergy + ascendEnergy + descendEnergy;
        PrintLog.Log("Total Energy Required: " + totalEnergyRequired);

        // Step 6: Check if total energy required for the flight is less that threshold.
        double totalEnergyAvailable = CompleteFlyingDataModel.getInstance().getConfigurationModel().getCapacity()
                * CompleteFlyingDataModel.getInstance().getConfigurationModel().getNumberOfBatteries();
        totalEnergyAvailable = totalEnergyAvailable
                - CompleteFlyingDataModel.getInstance().getDroneModel().getForcedLandingCharge();

        PrintLog.Log("Total Energy Available: " + totalEnergyAvailable);


        // Optional : To find possible landing site
        // TODO: Becuase of time constraint this could not be completed successfully. This is just raw incomplete implementation.
        /*if (totalEnergyRequired > totalEnergyAvailable) {
            double excessRequiredEnergy = totalEnergyRequired - totalEnergyAvailable;
            PrintLog.Log(TAG, "excessRequiredEnergy: " + excessRequiredEnergy);
            double maxAllowedEnergyForHorizontal = excessRequiredEnergy - horizontalEnergy;
            PrintLog.Log(TAG, "maxAllowedEnergyForHorizontal: " + maxAllowedEnergyForHorizontal);

            double maxHorizontalDist = (maxAllowedEnergyForHorizontal / (horizontalLoadInFlight / Math.pow(CompleteFlyingDataModel.getInstance().getMissionModel().getHorizontalSpeed(), 3))) / 1000;
            PrintLog.Log(TAG, "maxHorizontalDist: " + maxHorizontalDist);

            int totalWayPoints = CompleteFlyingDataModel.getInstance().getMissionModel().getTotalPoints();
            CompleteFlyingDataModel.getInstance().setLandingPoint(totalWayPoints - 1);

            flyingDistance = 0;
            for (int i = 0; i < totalWayPoints - 1; i++) {
                flyingDistance += haversine(CompleteFlyingDataModel.getInstance().getMissionModel().getPoints(i).getLatitude(),
                        CompleteFlyingDataModel.getInstance().getMissionModel().getPoints(i).getLongitude(),
                        CompleteFlyingDataModel.getInstance().getMissionModel().getPoints(i + 1).getLatitude(),
                        CompleteFlyingDataModel.getInstance().getMissionModel().getPoints(i + 1).getLongitude());
                Log.e(TAG, "threashhold: " + (flyingDistance - maxHorizontalDist));
                if (maxHorizontalDist - flyingDistance <= 5) {
                    CompleteFlyingDataModel.getInstance().setLandingPoint(i);
                    break;
                }
            }
        }*/

        return totalEnergyRequired < totalEnergyAvailable;
    }


    /**
     * This API is to calculate total distance between two geo-points(i.e. 2 latitude and 2 longitude)
     *
     * @param lat1 : First location latitude.
     * @param lon1 : First location longitude.
     * @param lat2 : Second location latitude.
     * @param lon2 : Second location longitude.
     * @return Total distance in K.M.
     */
    static double haversine(double lat1, double lon1,
                            double lat2, double lon2) {
        // distance between latitudes and longitudes
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);

        // convert to radians
        lat1 = Math.toRadians(lat1);
        lat2 = Math.toRadians(lat2);

        // apply formulae
        double a = Math.pow(Math.sin(dLat / 2), 2) +
                Math.pow(Math.sin(dLon / 2), 2) *
                        Math.cos(lat1) *
                        Math.cos(lat2);
        double rad = 6371;
        double c = 2 * Math.asin(Math.sqrt(a));
        return (rad * c);
    }
}
