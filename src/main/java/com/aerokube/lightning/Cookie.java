package com.aerokube.lightning;

import javax.annotation.Nonnull;
import java.time.Instant;
import java.util.Optional;

public interface Cookie {

    static WebDriver.Cookies.CookieBuilder create(@Nonnull String name, @Nonnull String value) {
        return new StdCookie.CookieBuilder(name, value);
    }

    @Nonnull
    String getName();

    @Nonnull
    String getValue();

    @Nonnull
    String getPath();

    @Nonnull
    String getDomain();

    boolean isSecureOnly();

    boolean isHttpOnly();

    @Nonnull
    Optional<Instant> getExpires();

    @Nonnull
    com.aerokube.lightning.model.Cookie.SameSiteEnum getSameSitePolicy();
}
