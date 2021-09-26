package com.aerokube.lightning;

import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThan;

public class ScreenshotTest extends BaseTest {

    @Test
    public void testPageScreenshot() {
        driver.navigation()
                .navigate("https://aerokube.com");
        byte[] screenshot = driver.screenshot().take();
        assertThat(screenshot.length, greaterThan(0));
    }

    @Test
    public void testElementScreenshot() {
        driver.navigation()
                .navigate("https://aerokube.com");
        WebElement body = driver.elements().findFirst(By.tagName("body"));
        byte[] screenshot = driver.screenshot().take(body);
        assertThat(screenshot.length, greaterThan(0));
    }

}
