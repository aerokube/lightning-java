package com.aerokube.lightning;

import org.junit.jupiter.api.Test;

import java.time.Duration;

import static com.aerokube.lightning.Image.CHROME;

public class SelenoidCapabilitiesTest extends BaseTest {

    @Override
    protected Image getImage() {
        return CHROME;
    }

    @Test
    void testBasicCapabilities() {
        test(
                () -> Capabilities.create()
                        .browserName("chrome")
                        .selenoid().environmentVariable("LANG", "en_US.UTF-8")
                        .enableVNC()
                        .enableVideo().videoName("my-video.mp4")
                        .videoScreenSize("1280x1024").videoFrameRate(24)
                        .s3KeyPattern("$browserName/$sessionId")
                        .enableLog().logName("my-log.log")
                        .name("my-cool-test-name").timeZone("Europe/Moscow")
                        .screenResolution("1280x1024x24").sessionTimeout(Duration.ofMinutes(5)),
                driver -> driver.navigation().navigate("https://example.com")
        );
    }

}
