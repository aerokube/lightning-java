package com.aerokube.lightning;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.Duration;

public class Test {

    public static void main(String[] args) throws IOException {

        Capabilities capabilities = new Capabilities();
        capabilities.setBrowserName("chrome");
        WebDriver driver = WebDriver.create(
                capabilities,
                "http://localhost:4444/",
                apiClient -> apiClient.setReadTimeout(Duration.ofMinutes(5))
        );
        System.out.printf("id = %s\n", driver.getSessionId());

        try {
            driver.navigation().navigate("https://aerokube.com/");

            byte[] data = driver.screenshot().takeScreenshot();
            Files.write(Paths.get("screenshot.png"), data);

        } finally {
            driver.session().delete();
        }

    }

}
