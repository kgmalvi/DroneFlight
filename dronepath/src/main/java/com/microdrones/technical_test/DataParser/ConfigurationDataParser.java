package com.microdrones.technical_test.DataParser;

import com.microdrones.technical_test.ClassFunctions.PrintLog;
import com.microdrones.technical_test.DataDeclaration.DataConstants;
import com.microdrones.technical_test.Model.CompleteFlyingDataModel;
import com.microdrones.technical_test.Model.ConfigurationModel;

import org.json.JSONObject;

public class ConfigurationDataParser {

    private static final String TAG = "ConfigDataParser";
    private static String _strConfigData;
    private static JSONObject _jsonConfigData, _jsonVerticalSpeed, _jsonEnergy, _jsonPayload;
    private static ConfigurationModel configModel;

    public ConfigurationDataParser(String _strConfigData) {
        ConfigurationDataParser._strConfigData = _strConfigData;
        configModel = new ConfigurationModel();
        setConfigData();
    }

    private void setConfigData() {
        try {
            _jsonConfigData = new JSONObject(_strConfigData);

            try {
                _jsonVerticalSpeed = new JSONObject(_jsonConfigData.getJSONObject(DataConstants.Configuration.verticalSpeeds).toString());
                configModel.setAscension(_jsonVerticalSpeed.getDouble(DataConstants.Configuration.ascension));
                configModel.setDescent(_jsonVerticalSpeed.getDouble(DataConstants.Configuration.descent));
            } catch (Exception e) {
                PrintLog.Log(TAG, "Exception in reading the vertical Speed: " + e.getMessage());
            }

            try {
                _jsonEnergy = new JSONObject(_jsonConfigData.getJSONObject(DataConstants.Configuration.energy).toString());
                configModel.setNumberOfBatteries(_jsonEnergy.getDouble(DataConstants.Configuration.numberOfBatteries));
                configModel.setCapacity(_jsonEnergy.getDouble(DataConstants.Configuration.capacity));
            } catch (Exception e) {
                PrintLog.Log(TAG, "Exception in reading the Energy: " + e.getMessage());
            }

            try {
                _jsonPayload = new JSONObject(_jsonConfigData.getJSONObject(DataConstants.Configuration.payload).toString());
                configModel.setAdditionalLoad(_jsonPayload.getDouble(DataConstants.Configuration.additionalLoad));
            } catch (Exception e) {
                configModel.setAdditionalLoad(0);
                PrintLog.Log(TAG, "Exception in reading the Payload: " + e.getMessage());
            }

            CompleteFlyingDataModel.getInstance().setConfigurationModel(configModel);
        } catch (Exception e) {
            PrintLog.Log(TAG, "Exception in reading Configuration JSON: " + e.getMessage());
        }
    }
}
