package com.aerokube.lightning;

import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

class NavigationTest extends BaseTest {

    @Test
    void testNavigation() {
        test(driver -> {
            String url = driver.navigation()
                    .navigate("https://example.com/")
                    .navigate("https://example.org/")
                    .refresh()
                    .back()
                    .forward()
                    .getUrl();
            assertThat(url, is(not(emptyString())));
            String title = driver.navigation().getTitle();
            assertThat(title, is(not(emptyString())));
        });
    }

    @Test
    void testAcceptInsecureCerts() {
        test(() -> Capabilities.create().browserName("chrome").acceptInsecureCerts(), driver -> {
            String url = driver.navigation()
                    .navigate("https://self-signed.badssl.com/")
                    .getUrl();
            assertThat(url, is(not(emptyString())));
        });
    }

}
