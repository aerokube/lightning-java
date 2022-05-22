package com.aerokube.lightning;

import org.junit.jupiter.api.Test;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

public class ChromeCapabilitiesTest extends BaseTest {

    static Path extensionPath() {
        return Paths.get("src", "test", "resources", "test.crx");
    }

    @Test
    void testBasicCapabilities() {
        test(
                () -> Capabilities.create()
                        .chrome().binary("/usr/bin/google-chrome")
                        .args("no-sandbox").excludeSwitches("help"),
                driver -> driver.navigation().navigate("https://example.com")
        );
    }

    @Test
    void testMobileEmulationDeviceName() {
        test(
                () -> Capabilities.create()
                        .chrome()
                        .mobileEmulation().deviceName("Nexus 4"),
                driver -> driver.navigation().navigate("https://example.com")
        );
    }

    @Test
    void testMobileEmulationCustomDevice() {
        test(
                () -> Capabilities.create()
                        .chrome()
                        .mobileEmulation().deviceMetrics()
                        .width(384).height(640).pixelRatio(2.0f)
                        .userAgent("Mozilla/5.0 (Linux; Android 4.4.2; Nexus 4 Build/KOT49H) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/%s Mobile Safari/537.36"),
                driver -> driver.navigation().navigate("https://example.com")
        );
    }

    @Test
    void testPerformanceLogging() {
        test(
                () -> Capabilities.create()
                        .chrome().performanceLogging()
                        .enableNetwork().enablePage()
                        .bufferUsageReportingInterval(Duration.ofSeconds(5))
                        .traceCategories("browser", "devtools.timeline", "devtools"),
                driver -> driver.navigation().navigate("https://example.com")
        );
    }

    @Test
    void testUploadExtension() {
        Path extensionPath = extensionPath();
        test(
                () -> Capabilities.create()
                        .chrome()
                        .extensions(extensionPath),
                driver -> {
                    WebElement body = driver
                            .navigation().navigate("https://example.com")
                            .elements().findFirst(By.tagName("body"));
                    assertThat(body.getCssProperty("background-color"), equalTo("rgba(0, 0, 0, 1)"));
                }
        );
    }

}
