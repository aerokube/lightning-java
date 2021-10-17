package com.aerokube.lightning.examples;

import com.aerokube.lightning.Capabilities;
import com.aerokube.lightning.WebDriver;
import com.aerokube.lightning.Window;

/**
 * This example demonstrates what you can do with windows
 */
public class Windows {

    public static void main(String[] args) {
        String baseUri = "http://localhost:4444/wd/hub";

        Capabilities capabilities = Capabilities.create().chrome();
        WebDriver driver = WebDriver.create(baseUri, capabilities);

        Window currentWindow = driver
                .navigation().navigate("https://example.com")
                .windows().current()
                .setSize(800, 600)
                .setPosition(200, 100);

        Window newWindow = driver
                .windows().createWindow().maximize().switchTo();
        driver.navigation().navigate("https://aerokube.com/");
        newWindow.close();

        currentWindow.switchTo();
    }

}
