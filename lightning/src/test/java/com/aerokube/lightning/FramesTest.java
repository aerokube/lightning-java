package com.aerokube.lightning;

import org.junit.jupiter.api.Test;

public class FramesTest extends BaseTest {

    @Test
    void testFrames() {
        test(driver -> {
            driver.navigation()
                    .navigate("https://www.w3.org/WAI/UA/TS/html401/cp0101/0101-FRAME-TEST.html");

            WebElement frame = driver.elements().findFirst(By.cssSelector("frame[name='target2']"));
            driver.frames().switchTo(frame);

            driver.frames().switchToParent();

            driver.frames().switchTo(1);

            driver.frames().switchToDefault();
        });
    }

}
