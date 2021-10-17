package com.aerokube.lightning.examples;

import com.aerokube.lightning.Capabilities;
import com.aerokube.lightning.WebDriver;

/**
 * This example demonstrates how to use specific capabilities
 */
public class ExtensionCapabilities {

    public static void main(String[] args) {
        String baseUri = "http://localhost:4444/wd/hub";

        Capabilities capabilities = Capabilities.create()
                .chrome().args("headless") // Use similar methods for opera(), edge(), safari() and so on
                .mobileEmulation().deviceName("Nexus 4")
                .selenoid().enableVNC().name("MyCoolTest");
        WebDriver driver = WebDriver.create(baseUri, capabilities);

        String pageSource = driver
                .navigation().navigate("https://example.com")
                .document().getPageSource();
        System.out.println(pageSource);
    }

}
