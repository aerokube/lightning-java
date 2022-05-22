package com.aerokube.lightning;

import javax.annotation.Nonnull;
import java.time.Instant;
import java.util.Optional;

public class StdCookie implements Cookie {

    private final com.aerokube.lightning.model.Cookie cookie;

    StdCookie(@Nonnull com.aerokube.lightning.model.Cookie cookie) {
        this.cookie = cookie;
    }

    com.aerokube.lightning.model.Cookie raw() {
        return cookie;
    }

    @Nonnull
    @Override
    public String getName() {
        return cookie.getName();
    }

    @Nonnull
    @Override
    public String getValue() {
        return cookie.getValue();
    }

    @Nonnull
    @Override
    public String getPath() {
        return cookie.getPath();
    }

    @Nonnull
    @Override
    public String getDomain() {
        return cookie.getDomain();
    }

    @Override
    public boolean isSecureOnly() {
        return cookie.getSecure();
    }

    @Override
    public boolean isHttpOnly() {
        return cookie.getHttpOnly();
    }

    @Nonnull
    @Override
    public Optional<Instant> getExpires() {
        Long expiry = cookie.getExpiry();
        return expiry != null ? Optional.of(Instant.ofEpochSecond(expiry)) : Optional.empty();
    }

    @Nonnull
    @Override
    public com.aerokube.lightning.model.Cookie.SameSiteEnum getSameSitePolicy() {
        return cookie.getSameSite();
    }

    public static class CookieBuilder implements Cookie.CookieBuilder {

        private final com.aerokube.lightning.model.Cookie cookie;

        CookieBuilder(String name, String value) {
            this.cookie = new com.aerokube.lightning.model.Cookie().name(name).value(value);
        }

        @Nonnull
        @Override
        public Cookie.CookieBuilder path(@Nonnull String path) {
            cookie.setPath(path);
            return this;
        }

        @Nonnull
        @Override
        public Cookie.CookieBuilder domain(@Nonnull String domain) {
            cookie.setDomain(domain);
            return this;
        }

        @Nonnull
        @Override
        public Cookie.CookieBuilder secureOnly() {
            cookie.setSecure(true);
            return this;
        }

        @Nonnull
        @Override
        public Cookie.CookieBuilder httpOnly() {
            cookie.setHttpOnly(true);
            return this;
        }

        @Nonnull
        @Override
        public Cookie.CookieBuilder expires(@Nonnull Instant expires) {
            cookie.setExpiry(expires.getEpochSecond());
            return this;
        }

        @Nonnull
        @Override
        public Cookie.CookieBuilder sameSitePolicy(@Nonnull com.aerokube.lightning.model.Cookie.SameSiteEnum sameSitePolicy) {
            cookie.setSameSite(sameSitePolicy);
            return this;
        }

        @Nonnull
        @Override
        public Cookie build() {
            return new StdCookie(cookie);
        }
    }
}
