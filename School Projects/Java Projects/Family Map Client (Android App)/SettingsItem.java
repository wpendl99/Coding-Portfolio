package com.wkp23.familymapclient;

public class SettingsItem {
    private final String name;
    private final String description;
    private final boolean toggleable;
    private boolean status;

    public SettingsItem(String name, String description, boolean toggleable, boolean status) {
        this.name = name;
        this.description = description;
        this.toggleable = toggleable;
        this.status = status;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public boolean isToggleable() {
        return toggleable;
    }

    public boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }
}
