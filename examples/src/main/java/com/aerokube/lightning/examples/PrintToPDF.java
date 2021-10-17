package com.aerokube.lightning.examples;

import com.aerokube.lightning.Capabilities;
import com.aerokube.lightning.WebDriver;

import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * This example demonstrates how to generate print version of the page to PDF
 */
public class PrintToPDF {

    public static void main(String[] args) throws Exception {
        String baseUri = "http://localhost:4444/wd/hub";

        Capabilities capabilities = Capabilities.create().firefox();
        WebDriver driver = WebDriver.create(baseUri, capabilities);

        byte[] pdfBytes = driver
                .navigation().navigate("https://example.com")
                .print().landscape()
                .addPages(1, 2)
                .addPages("3-5")
                .scale(0.5f)
                .pdf();
        Files.write(Paths.get("printed.pdf"), pdfBytes);
    }

}
