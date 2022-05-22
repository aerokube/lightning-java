package com.aerokube.lightning;

import org.junit.jupiter.api.Test;

import java.time.Duration;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

public class TimeoutsTest extends BaseTest {

    @Test
    void testTimeouts() {
        test(driver -> {
            driver.timeouts()
                    .setImplicitWaitTimeout(Duration.ofMinutes(2))
                    .setPageLoadTimeout(Duration.ofMinutes(3))
                    .setScriptTimeout(Duration.ofMinutes(4));
            assertThat(driver.timeouts().getImplicitWaitTimeout(), equalTo(Duration.ofMinutes(2)));
            assertThat(driver.timeouts().getPageLoadTimeout(), equalTo(Duration.ofMinutes(3)));
            assertThat(driver.timeouts().getScriptTimeout().isPresent(), is(true));
            assertThat(driver.timeouts().getScriptTimeout().get(), equalTo(Duration.ofMinutes(4)));
        });
    }

}
