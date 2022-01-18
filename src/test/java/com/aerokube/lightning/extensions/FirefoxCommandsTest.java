package com.aerokube.lightning.extensions;

import com.aerokube.lightning.*;
import com.aerokube.lightning.model.FirefoxContext;
import org.junit.jupiter.api.Test;

import java.nio.file.Path;
import java.nio.file.Paths;

import static com.aerokube.lightning.Image.FIREFOX;
import static com.aerokube.lightning.model.FirefoxContext.CHROME;
import static com.aerokube.lightning.model.FirefoxContext.CONTENT;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThan;

public class FirefoxCommandsTest extends BaseTest {

    @Override
    protected Image getImage() {
        return FIREFOX;
    }

    @Test
    void testLoadUnloadExtension() {
        Path extensionPath = Paths.get("src", "test", "resources", "test.xpi");
        test(
                () -> Capabilities.create().firefox(),
                driver -> {
                    WebElement body = driver
                            .navigation().navigate("https://example.com")
                            .elements().findFirst(By.tagName("body"));
                    assertThat(body.getCssProperty("background-color"), equalTo("rgb(240, 240, 242)"));

                    String addonId = driver.extension(FirefoxCommands.class).installAddon(extensionPath);
                    body = driver
                            .navigation().refresh()
                            .elements().findFirst(By.tagName("body"));
                    assertThat(body.getCssProperty("background-color"), equalTo("rgb(0, 0, 0)"));

                    driver.extension(FirefoxCommands.class).uninstallAddon(addonId);
                    body = driver
                            .navigation().refresh()
                            .elements().findFirst(By.tagName("body"));
                    assertThat(body.getCssProperty("background-color"), equalTo("rgb(240, 240, 242)"));
                }
        );
    }

    @Test
    void testContext() {
        test(
                () -> Capabilities.create().firefox(),
                driver -> {
                    FirefoxContext context = driver.extension(FirefoxCommands.class).context();
                    assertThat(context, equalTo(CONTENT));

                    FirefoxContext switchedContext = driver.extension(FirefoxCommands.class).context(CHROME)
                            .context();
                    assertThat(switchedContext, equalTo(CHROME));
                }
        );
    }

    @Test
    void testPageScreenshot() {
        test(
                () -> Capabilities.create().firefox(),
                driver -> {
                    driver.navigation()
                            .navigate("https://example.com");
                    byte[] screenshot = driver.extension(FirefoxCommands.class).fullScreenshot();
                    assertThat(screenshot.length, greaterThan(0));
                }
        );
    }

}
