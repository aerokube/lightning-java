package com.aerokube.lightning;

import javax.annotation.Nonnull;
import java.time.Instant;
import java.util.Optional;

public class Cookie implements WebDriver.Cookies.Cookie {

    private final com.aerokube.lightning.model.Cookie cookie;

    Cookie(@Nonnull com.aerokube.lightning.model.Cookie cookie) {
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

    public static class CookieBuilder implements WebDriver.Cookies.CookieBuilder {

        private final com.aerokube.lightning.model.Cookie cookie;

        CookieBuilder(String name, String value) {
            this.cookie = new com.aerokube.lightning.model.Cookie().name(name).value(value);
        }

        @Nonnull
        @Override
        public WebDriver.Cookies.CookieBuilder path(@Nonnull String path) {
            cookie.setPath(path);
            return this;
        }

        @Nonnull
        @Override
        public WebDriver.Cookies.CookieBuilder domain(@Nonnull String domain) {
            cookie.setDomain(domain);
            return this;
        }

        @Nonnull
        @Override
        public WebDriver.Cookies.CookieBuilder secureOnly(boolean secureOnly) {
            cookie.setSecure(secureOnly);
            return this;
        }

        @Nonnull
        @Override
        public WebDriver.Cookies.CookieBuilder httpOnly(boolean httpOnly) {
            cookie.setHttpOnly(httpOnly);
            return this;
        }

        @Nonnull
        @Override
        public WebDriver.Cookies.CookieBuilder expires(@Nonnull Instant expires) {
            cookie.setExpiry(expires.getEpochSecond());
            return this;
        }

        @Nonnull
        @Override
        public WebDriver.Cookies.CookieBuilder sameSitePolicy(@Nonnull com.aerokube.lightning.model.Cookie.SameSiteEnum sameSitePolicy) {
            cookie.setSameSite(sameSitePolicy);
            return this;
        }

        @Nonnull
        @Override
        public WebDriver.Cookies.Cookie build() {
            return new Cookie(cookie);
        }
    }
}
