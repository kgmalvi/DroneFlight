package com.microdrones.technical_test.Model;

import com.microdrones.technical_test.DataParser.ConfigurationDataParser;
import com.microdrones.technical_test.DataParser.DroneDataParser;
import com.microdrones.technical_test.DataParser.MissionDataParser;

public class CompleteFlyingDataModel {

    private static CompleteFlyingDataModel completeFlyingDataModel = null;
    private MissionModel missionModel;
    private ConfigurationModel configurationModel;
    private DroneModel droneModel;


    private int landingPoint;

    public static CompleteFlyingDataModel getInstance() {
        if (completeFlyingDataModel == null) {
            completeFlyingDataModel = new CompleteFlyingDataModel();
        }
        return completeFlyingDataModel;
    }

    public static void init(String _strConfigData, String _strDroneData, String _strMissionData) {
        completeFlyingDataModel = new CompleteFlyingDataModel();
        new MissionDataParser(_strMissionData);
        new ConfigurationDataParser(_strConfigData);
        new DroneDataParser(_strDroneData);
    }


    public MissionModel getMissionModel() {
        return missionModel;
    }

    public void setMissionModel(MissionModel missionModel) {
        this.missionModel = missionModel;
    }

    public ConfigurationModel getConfigurationModel() {
        return configurationModel;
    }

    public void setConfigurationModel(ConfigurationModel configurationModel) {
        this.configurationModel = configurationModel;
    }

    public DroneModel getDroneModel() {
        return droneModel;
    }

    public void setDroneModel(DroneModel droneModel) {
        this.droneModel = droneModel;
    }

    public int getLandingPoint() {
        return landingPoint;
    }

    public void setLandingPoint(int landingPoint) {
        this.landingPoint = landingPoint;
    }
}
