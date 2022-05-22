package com.aerokube.lightning;

import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThan;

public class ScreenshotTest extends BaseTest {

    @Test
    void testPageScreenshot() {
        test(driver -> {
            driver.navigation()
                    .navigate("https://example.com");
            byte[] screenshot = driver.screenshot().take();
            assertThat(screenshot.length, greaterThan(0));
        });
    }

    @Test
    void testElementScreenshot() {
        test(driver -> {
            driver.navigation()
                    .navigate("https://example.com");
            WebElement body = driver.elements().findFirst(By.tagName("body"));
            byte[] screenshot = driver.screenshot().take(body);
            assertThat(screenshot.length, greaterThan(0));
        });
    }

}
