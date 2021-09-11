package com.aerokube.lightning;

import static com.aerokube.lightning.model.Capabilities.JSON_PROPERTY_BROWSER_NAME;

public class Capabilities {

    private final com.aerokube.lightning.model.Capabilities capabilities = new com.aerokube.lightning.model.Capabilities();

    com.aerokube.lightning.model.Capabilities raw() {
        return capabilities;
    }

    public void setBrowserName(String browserName) {
        capabilities.put(JSON_PROPERTY_BROWSER_NAME, browserName);
    }

    public void setCapability(String key, String value) {
        capabilities.put(key, value);
    }

    public void setCapability(String key, int value) {
        capabilities.put(key, value);
    }

    public void setCapability(String key, boolean value) {
        capabilities.put(key, value);
    }

    public void setCapability(String key, Object value) {
        capabilities.put(key, value);
    }

}
