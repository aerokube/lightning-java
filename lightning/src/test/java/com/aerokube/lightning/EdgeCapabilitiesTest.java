package com.aerokube.lightning;

import org.junit.jupiter.api.Test;

import java.nio.file.Path;

import static com.aerokube.lightning.ChromeCapabilitiesTest.extensionPath;
import static com.aerokube.lightning.Image.EDGE;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

public class EdgeCapabilitiesTest extends BaseTest {

    @Override
    protected Image getImage() {
        return EDGE;
    }

    @Test
    void testBasicCapabilities() {
        test(
                () -> Capabilities.create()
                        .edge().binary("/usr/bin/microsoft-edge")
                        .args("no-sandbox"),
                driver -> driver.navigation().navigate("https://example.com")
        );
    }

    @Test
    void testUploadExtension() {
        Path extensionPath = extensionPath();
        test(
                () -> Capabilities.create()
                        .edge()
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
