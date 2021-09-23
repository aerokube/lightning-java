package com.aerokube.lightning;

import javax.annotation.Nonnull;
import java.io.Serializable;
import java.util.List;

public interface Capabilities {

    @Nonnull
    static Capabilities create() {
        return new StdCapabilities();
    }

    @Nonnull
    Capabilities browserName(@Nonnull String browserName);

    @Nonnull
    Capabilities browserVersion(@Nonnull String browserVersion);

    @Nonnull
    Capabilities platformName(@Nonnull String platformName);

    @Nonnull
    Capabilities enableStrictFileInteractability();

    @Nonnull
    Capabilities acceptInsecureCerts();

    @Nonnull
    Capabilities doNotWaitForPageLoad();

    @Nonnull
    Capabilities waitForPageDOMLoadOnly();

    @Nonnull
    Capabilities acceptAllPrompts(boolean returnError);

    @Nonnull
    Capabilities dismissAllPrompts(boolean returnError);

    @Nonnull
    Proxy proxy();

    @Nonnull
    Capabilities capability(@Nonnull String key, @Nonnull Serializable value);

    @Nonnull
    com.aerokube.lightning.model.Capabilities raw();

    interface Proxy {

        @Nonnull
        Capabilities autoConfiguration(@Nonnull String url);

        @Nonnull
        Capabilities autoDetect();

        @Nonnull
        Capabilities system();

        @Nonnull
        Capabilities http(@Nonnull String hostPort);

        @Nonnull
        Capabilities http(@Nonnull String hostPort, @Nonnull List<String> hostsThatBypassProxy);

        @Nonnull
        Capabilities socks(@Nonnull String hostPort, int socksVersion);

    }

}
