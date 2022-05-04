package com.microdrones.technical_test.DataParser;

import com.microdrones.technical_test.ClassFunctions.PrintLog;
import com.microdrones.technical_test.DataDeclaration.DataConstants;
import com.microdrones.technical_test.Model.CompleteFlyingDataModel;
import com.microdrones.technical_test.Model.DroneModel;

import org.json.JSONObject;

public class DroneDataParser {
    private static final String TAG = "DroneDataParser";
    private static String _strDroneData;
    private static JSONObject _jsonDroneData, _jsonCurrentLoadInFlight;
    private static DroneModel droneModel;

    public DroneDataParser(String _strDroneData) {
        DroneDataParser._strDroneData = _strDroneData;
        droneModel = new DroneModel();
        setDroneData();
    }

    private void setDroneData() {
        try {
            _jsonDroneData = new JSONObject(_strDroneData);

            try {
                _jsonCurrentLoadInFlight = new JSONObject(_jsonDroneData.getJSONObject(DataConstants.Drone.currentLoadInFlight).toString());
                droneModel.setAscension(_jsonCurrentLoadInFlight.getDouble(DataConstants.Drone.ascension));
                droneModel.setDescent(_jsonCurrentLoadInFlight.getDouble(DataConstants.Drone.descent));
                droneModel.setTranslation(_jsonCurrentLoadInFlight.getDouble(DataConstants.Drone.translation));
            } catch (Exception e) {
                PrintLog.Log(TAG, "Exception in reading the CurrentLoadInFlight: " + e.getMessage());
            }

            try {
                droneModel.setForcedLandingCharge(_jsonDroneData.getDouble(DataConstants.Drone.forcedLandingCharge));
            } catch (Exception e) {
                PrintLog.Log(TAG, "Exception in reading the ForcedLandingCharge: " + e.getMessage());
            }

            CompleteFlyingDataModel.getInstance().setDroneModel(droneModel);
        } catch (Exception e) {
            PrintLog.Log(TAG, "Exception in reading Drone JSON: " + e.getMessage());
        }
    }
}
