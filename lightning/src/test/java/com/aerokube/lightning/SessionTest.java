package com.aerokube.lightning;

import com.aerokube.lightning.WebDriver.Session.Status;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

class SessionTest extends BaseTest {

    @Test
    void testSessionId() {
        test(driver -> assertThat(driver.getSessionId().length(), greaterThan(0)));
    }

    @Test
    void testStatus() {
        test(driver -> {
            Status status = driver.session().status();
            assertThat(status.isReady(), equalTo(true));
            assertThat(status.getMessage(), is(not(emptyString())));
        });
    }

}
