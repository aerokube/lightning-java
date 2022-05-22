package com.aerokube.lightning;

import javax.annotation.Nonnull;
import java.time.Instant;
import java.util.Optional;

public interface Cookie {

    @Nonnull
    static CookieBuilder create(@Nonnull String name, @Nonnull String value) {
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

    interface CookieBuilder {

        @Nonnull
        CookieBuilder path(@Nonnull String path);

        @Nonnull
        CookieBuilder domain(@Nonnull String domain);

        @Nonnull
        CookieBuilder secureOnly();

        @Nonnull
        CookieBuilder httpOnly();

        @Nonnull
        CookieBuilder expires(@Nonnull Instant expires);

        @Nonnull
        CookieBuilder sameSitePolicy(@Nonnull com.aerokube.lightning.model.Cookie.SameSiteEnum sameSitePolicy);

        @Nonnull
        Cookie build();
    }
}
