package com.softdesign.devintensive.data.manager;


public class DataManager {


    private PreferenceManager mPreferenceManager;
    private static DataManager INSTANCE=null;

    public DataManager() {
        this.mPreferenceManager = new PreferenceManager();
    }

    public static DataManager getInstance(){
        if (INSTANCE==null){
            INSTANCE=new DataManager();
        }
        return INSTANCE;
    }
    public PreferenceManager getPreferenceManager() {
        return mPreferenceManager;
    }
}
