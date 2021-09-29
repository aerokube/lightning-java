package com.aerokube.lightning;

import org.junit.Ignore;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThan;

@Ignore // TODO: remove it when chrome options support is implemented
public class PrintTest extends BaseTest {

    @Test
    void testPrint() {
        test(driver -> {
            driver.navigation()
                    .navigate("https://example.com");
            byte[] pdf = driver.print()
                    .width(210f)
                    .height(297f)
                    .addPages(1)
                    .addPages("2-3")
                    .scale(0.5f)
                    .originalSize()
                    .marginTop(1.5f)
                    .marginBottom(1.5f)
                    .marginLeft(1.5f)
                    .marginRight(1.5f)
                    .pdf();
            assertThat(pdf.length, greaterThan(0));
        });
    }

    @Override
    protected String getImage() {
        return "browsers/firefox:92.0"; // Chrome only supports PDF in headless mode
    }
}
