package com.aerokube.lightning;

import java.io.Serializable;

import static com.aerokube.lightning.model.Capabilities.JSON_PROPERTY_BROWSER_NAME;

public class Capabilities {

    private final com.aerokube.lightning.model.Capabilities capabilities = new com.aerokube.lightning.model.Capabilities();

    com.aerokube.lightning.model.Capabilities raw() {
        return capabilities;
    }

    public void setBrowserName(String browserName) {
        capabilities.put(JSON_PROPERTY_BROWSER_NAME, browserName);
    }

    public void setCapability(String key, Serializable value) {
        capabilities.put(key, value);
    }

}
