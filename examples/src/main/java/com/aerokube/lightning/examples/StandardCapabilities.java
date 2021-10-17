package com.aerokube.lightning.examples;

import com.aerokube.lightning.Capabilities;
import com.aerokube.lightning.WebDriver;

import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * This example demonstrates how to initialize Selenium session, open a page and take screenshot
 */
public class StandardCapabilities {

    public static void main(String[] args) throws Exception {
        String baseUri = "http://localhost:4444/wd/hub";

        Capabilities capabilities = Capabilities.create()
                .browserName("chrome")
                .browserVersion("94.0")
                .acceptInsecureCerts();
        WebDriver driver = WebDriver.create(baseUri, capabilities);

        byte[] screenshotBytes = driver
                .navigation().navigate("https://example.com")
                .screenshot().take();
        Files.write(Paths.get("screenshot.png"), screenshotBytes);
    }

}
