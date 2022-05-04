package com.microdrones.technical_test;

import com.microdrones.technical_test.Calculations.Calculations;
import com.microdrones.technical_test.ClassFunctions.PrintLog;
import com.microdrones.technical_test.Model.CompleteFlyingDataModel;

import java.io.File;
import java.util.Scanner;

public class Main {

    private static final String TAG = "Main";
    private static String _strConfigData = "", _strDroneData = "", _strMissionData = "";
    private static double totalDistanceForMission;


    public static void main(String[] args) {

        if (args[0] != null && !args[0].equals("")) {
            PrintLog.Log("Reading Data");
            readDataAndCalculate(args[0]);
        } else {
            PrintLog.Log(TAG, "Pass Module Path in Project Configuration...");
        }

        //PrintLog.Log(CompleteFlyingDataModel.getInstance().getMissionModel().getName());


        //  totalDistanceForMission(_strJSONInput);
        /*double horizontalFlightTime = totalDistanceForMission * 6; // distance * speed (mission1 :: horizontalSpeed)
        double ascendingFlightTime = 50 * 5;
        double descendingFlightTime = 50 * 1;

        double totalFlightTime = ascendingFlightTime + horizontalFlightTime + descendingFlightTime;

        System.out.println("totalFlightTime: " + totalFlightTime + " seconds.");*/


    }

    private static void readDataAndCalculate(String dirPath) {
        Scanner scanner = null;
        String filePath = dirPath + "/src/main/java/com/microdrones/technical_test/data";
        File configFile = new File(filePath + "/configurations");
        String[] configFileList = configFile.list();
        File droneFile = new File(filePath + "/drones");
        String[] droneFileList = droneFile.list();
        File missionFile = new File(filePath + "/missions");
        String[] missionFileList = missionFile.list();

        /** Assumptions:
         * - number of files are equal in all data folders.
         * - Calculations would be done using Drone-n.json, Config-n.json & Missions-n.json, where as n would remain same for all 3 files.
         * e.g. Drone-1.json, Config-1.json & Missions-1.json
         * Drone-2.json, Config-2.json & Missions-2.json... etc...
         */

        for (int i = 0; i < configFileList.length; i++) {
            _strMissionData = "";
            _strConfigData = "";
            _strDroneData = "";
            try {
                scanner = new Scanner((new File(configFile.getAbsolutePath() + "/" + configFileList[i])));
                while (scanner.hasNextLine()) {
                    _strConfigData += scanner.nextLine();
                }

                scanner = new Scanner((new File(droneFile.getAbsolutePath() + "/" + droneFileList[i])));
                while (scanner.hasNextLine()) {
                    _strDroneData += scanner.nextLine();
                }

                scanner = new Scanner((new File(missionFile.getAbsolutePath() + "/" + missionFileList[i])));
                while (scanner.hasNextLine()) {
                    _strMissionData += scanner.nextLine();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            CompleteFlyingDataModel.getInstance().init(_strConfigData, _strDroneData, _strMissionData);

            boolean checkFlight = Calculations.checkIfFlightPossible();
            if (checkFlight) {
                System.out.println("mission-" + i + ": true - The mission was flown successfully.");
            } else {
                System.out.println("mission-" + i + ": false - The mission could not be flown successfully.");
            }
        }
    }
}
