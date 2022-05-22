package com.aerokube.lightning.adapter;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.instanceOf;

public class ScreenshotTest extends BaseTest {

    @Test
    void testPageScreenshot() {
        test(driver -> {
            driver.get("https://example.com");
            assertThat(driver, instanceOf(TakesScreenshot.class));
            byte[] screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
            assertThat(screenshot.length, greaterThan(0));
        });
    }

    @Test
    void testElementScreenshot() {
        test(driver -> {
            driver.get("https://example.com");
            WebElement body = driver.findElement(By.tagName("body"));
            byte[] screenshot = body.getScreenshotAs(OutputType.BYTES);
            assertThat(screenshot.length, greaterThan(0));
        });
    }

}
