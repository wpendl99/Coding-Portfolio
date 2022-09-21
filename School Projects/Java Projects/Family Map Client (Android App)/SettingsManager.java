package com.wkp23.familymapclient;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.TreeMap;

public class SettingsManager {
    Map<String, SettingsItem> data;

    public SettingsManager() {
        data = new LinkedHashMap<>();

        data.put("Life Story Lines", new SettingsItem("Life Story Lines", "Show life story lines", true, true));
        data.put("Family Tree Lines", new SettingsItem("Family Tree Lines", "Show family tree lines", true, true));
        data.put("Spouse Lines", new SettingsItem("Spouse Lines", "Show spouse lines", true, false));
        data.put("Father's Side", new SettingsItem("Father's Side", "Filter by father's side of family", true, true));
        data.put("Mother's Side", new SettingsItem("Mother's Side", "Filter by mother's side of family", true, true));
        data.put("Male Events", new SettingsItem("Male Events", "Filter events based on gender", true, true));
        data.put("Female Events", new SettingsItem("Female Events", "Filter events based on gender", true, true));
        data.put("Logout", new SettingsItem("Logout", "Return to login screen", false, false));
    }

    public void setSetting(String key, Boolean status) {
        SettingsItem setting = data.get(key);
        setting.setStatus(status);
        data.put(key, setting);
    }

    public boolean checkSetting(String key){
        return data.get(key).getStatus();
    }

    public Collection<SettingsItem> getData() {
        return data.values();
    }
}
