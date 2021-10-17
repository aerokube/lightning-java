package com.aerokube.lightning.examples;

import com.aerokube.lightning.By;
import com.aerokube.lightning.Capabilities;
import com.aerokube.lightning.WebDriver;
import com.aerokube.lightning.WebElement;

import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;

/**
 * This example demonstrates how to find elements and upload files
 */
public class FindElements {

    public static void main(String[] args) throws Exception {
        String baseUri = "http://localhost:4444/wd/hub";

        Capabilities capabilities = Capabilities.create()
                .firefox().preference("intl.accept_languages", "en,ru");

        WebDriver driver = WebDriver.create(baseUri, capabilities)
                .timeouts().setPageLoadTimeout(Duration.ofMinutes(2));

        WebElement fileInput = driver
                .navigation().navigate("https://example.com")
                .elements().findFirst(By.cssSelector("input[type=file]"));

        Path fileToUpload = Files.createTempFile("lightning-example", "");
        Files.writeString(fileToUpload, "test-value");
        String fileRemotePath = driver.document().uploadFile(fileToUpload);

        //Uploading the file and setting file input field value are two separate method calls
        fileInput.sendKeys(fileRemotePath);
    }

}
