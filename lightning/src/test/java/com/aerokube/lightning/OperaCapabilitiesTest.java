package com.aerokube.lightning;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.nio.file.Path;

import static com.aerokube.lightning.ChromeCapabilitiesTest.extensionPath;
import static com.aerokube.lightning.Image.OPERA;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

@Disabled("Opera driver is not compatible with W3C: https://github.com/operasoftware/operachromiumdriver/issues/88")
public class OperaCapabilitiesTest extends BaseTest {

    @Override
    protected Image getImage() {
        return OPERA;
    }

    @Test
    void testBasicCapabilities() {
        test(
                () -> Capabilities.create()
                        .opera().binary("/usr/bin/opera")
                        .args("no-sandbox"),
                driver -> driver.navigation().navigate("https://example.com")
        );
    }

    @Test
    void testUploadExtension() {
        Path extensionPath = extensionPath();
        test(
                () -> Capabilities.create()
                        .opera()
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
