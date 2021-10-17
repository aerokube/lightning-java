package com.aerokube.lightning.examples;

import com.aerokube.lightning.Capabilities;
import com.aerokube.lightning.Cookie;
import com.aerokube.lightning.WebDriver;

import java.time.Duration;
import java.time.Instant;

/**
 * This example demonstrates how to manipulate cookies
 */
public class Cookies {

    public static void main(String[] args) {
        String baseUri = "http://localhost:4444/wd/hub";

        Capabilities capabilities = Capabilities.create().chrome();
        WebDriver driver = WebDriver.create(baseUri, capabilities);

        Cookie cookie = Cookie.create("my-cookie", "some-value")
                .expires(Instant.now().plus(Duration.ofDays(10)))
                .build();
        driver
                .navigation().navigate("https://example.com")
                .cookies().deleteAll().add(cookie)
                .getAll().forEach(c -> System.out.println(c.getName()));

    }

}
