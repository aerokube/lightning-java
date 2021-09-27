package com.aerokube.lightning;

import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.Optional;

import static com.aerokube.lightning.model.Cookie.SameSiteEnum.NONE;
import static java.time.Duration.ofMinutes;
import static java.time.Instant.now;
import static java.time.temporal.ChronoUnit.SECONDS;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class CookiesTest extends BaseTest {

    private static final String COOKIE_NAME = "my-cookie";
    private static final String COOKIE_VALUE = "some-value";
    private static final String COOKIE_DOMAIN = "example.com";
    private static final Instant COOKIE_EXPIRES = now().plus(ofMinutes(10));

    @Test
    void testCookies() {
        test(driver -> {
            driver.navigation()
                    .navigate("https://example.com");
            driver.cookies().deleteAll();
            assertThat(driver.cookies().getAll(), is(empty()));

            Cookie cookie = Cookie.create(COOKIE_NAME, COOKIE_VALUE)
                    .domain(COOKIE_DOMAIN)
                    .expires(COOKIE_EXPIRES)
                    .path("/")
                    .httpOnly()
                    .secureOnly()
                    .sameSitePolicy(NONE)
                    .build();
            driver.cookies().add(cookie);

            driver.navigation().refresh();

            assertThat(driver.cookies().getAll(), hasSize(1));

            Cookie fetchedCookie = driver.cookies().get(COOKIE_NAME);
            assertThat(fetchedCookie.getName(), equalTo(COOKIE_NAME));
            assertThat(fetchedCookie.getValue(), equalTo(COOKIE_VALUE));
            assertThat(fetchedCookie.getDomain(), endsWith(COOKIE_DOMAIN));
            assertThat(fetchedCookie.getPath(), equalTo("/"));
            Optional<Instant> expires = fetchedCookie.getExpires();
            assertThat(expires.isPresent(), is(true));
            assertThat(expires.get(), equalTo(COOKIE_EXPIRES.truncatedTo(SECONDS)));

            driver.cookies().delete(COOKIE_NAME);
            assertThat(driver.cookies().getAll(), is(empty()));
        });
    }

}
