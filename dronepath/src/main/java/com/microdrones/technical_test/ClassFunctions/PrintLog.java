package com.microdrones.technical_test.ClassFunctions;

public class PrintLog {
    public static void Log(String TAG, String Data) {
        Log(TAG + " :: " + Data);
    }

    public static void Log(String Data) {
        // TODO: Uncomment following to see debug logs.
        // System.out.println("Drone" + " :: " + Data);
    }
}
