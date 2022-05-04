package com.microdrones.technical_test.DataParser;

import com.microdrones.technical_test.ClassFunctions.PrintLog;
import com.microdrones.technical_test.DataDeclaration.DataConstants;
import com.microdrones.technical_test.Model.CompleteFlyingDataModel;
import com.microdrones.technical_test.Model.MissionModel;

import org.json.JSONArray;
import org.json.JSONObject;

public class MissionDataParser {

    private static final String TAG = "MissionDataParser";
    private static String _strMissionData;
    private static JSONObject _jsonMissionData;
    private static JSONObject _jsonPointData;
    private static JSONArray _jsonPointsListData;
    private static MissionModel missionModel;
    private static MissionModel.Points points;

    public MissionDataParser(String _strMissionData) {
        MissionDataParser._strMissionData = _strMissionData;
        missionModel = new MissionModel();
        setMissionData();
    }

    private void setMissionData() {
        try {
            _jsonMissionData = new JSONObject(_strMissionData);
            missionModel.setName(_jsonMissionData.getString(DataConstants.Mission.name));
            missionModel.setHorizontalSpeed(_jsonMissionData.getDouble(DataConstants.Mission.horizontalSpeed));
            missionModel.setAltitude(_jsonMissionData.getDouble(DataConstants.Mission.altitude));

            _jsonPointsListData = new JSONArray(_jsonMissionData.getJSONArray(DataConstants.Mission.points).toString());
            for (int i = 0; i < _jsonPointsListData.length(); i++) {
                try {
                    points = new MissionModel.Points();
                    _jsonPointData = _jsonPointsListData.getJSONObject(i);
                    points.setLatitude(_jsonPointData.getDouble(DataConstants.Mission.latitude));
                    points.setLongitude(_jsonPointData.getDouble(DataConstants.Mission.longitude));
                    missionModel.setPointsList(points);
                } catch (Exception e) {
                    PrintLog.Log(TAG, "Exception in reading points JSON: " + e.getMessage());
                    break;
                }
            }
            CompleteFlyingDataModel.getInstance().setMissionModel(missionModel);
        } catch (Exception e) {
            PrintLog.Log(TAG, "Exception in reading mission JSON: " + e.getMessage());
        }
    }
}
