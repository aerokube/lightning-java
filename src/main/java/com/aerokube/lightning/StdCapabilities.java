package com.aerokube.lightning;

import javax.annotation.Nonnull;
import java.io.Serializable;
import java.util.Collections;
import java.util.List;

import static com.aerokube.lightning.model.Capabilities.*;
import static com.aerokube.lightning.model.Proxy.ProxyTypeEnum.*;

public class StdCapabilities implements Capabilities, Capabilities.Proxy {

    private final com.aerokube.lightning.model.Capabilities capabilities = new com.aerokube.lightning.model.Capabilities();

    @Override
    @Nonnull
    public com.aerokube.lightning.model.Capabilities raw() {
        return capabilities;
    }

    @Override
    @Nonnull
    public StdCapabilities browserName(@Nonnull String browserName) {
        capabilities.put(JSON_PROPERTY_BROWSER_NAME, browserName);
        return this;
    }

    @Override
    @Nonnull
    public Capabilities browserVersion(@Nonnull String browserVersion) {
        capabilities.put(JSON_PROPERTY_BROWSER_VERSION, browserVersion);
        return this;
    }

    @Override
    @Nonnull
    public Capabilities platformName(@Nonnull String platformName) {
        capabilities.put(JSON_PROPERTY_PLATFORM_NAME, platformName);
        return this;
    }

    @Override
    @Nonnull
    public Capabilities enableStrictFileInteractability() {
        capabilities.put(JSON_PROPERTY_STRICT_FILE_INTERACTABILITY, true);
        return this;
    }

    @Override
    @Nonnull
    public Capabilities acceptInsecureCerts() {
        capabilities.put(JSON_PROPERTY_ACCEPT_INSECURE_CERTS, true);
        return this;
    }

    @Override
    @Nonnull
    public Capabilities doNotWaitForPageLoad() {
        capabilities.put(JSON_PROPERTY_PAGE_LOAD_STRATEGY, PageLoadStrategyEnum.NONE);
        return this;
    }

    @Override
    @Nonnull
    public Capabilities waitForPageDOMLoadOnly() {
        capabilities.put(JSON_PROPERTY_PAGE_LOAD_STRATEGY, PageLoadStrategyEnum.EAGER);
        return this;
    }

    @Override
    @Nonnull
    public Capabilities acceptAllPrompts(boolean returnError) {
        capabilities.put(
                JSON_PROPERTY_UNHANDLED_PROMPT_BEHAVIOR,
                returnError ? UnhandledPromptBehaviorEnum.ACCEPT_AND_NOTIFY : UnhandledPromptBehaviorEnum.ACCEPT
        );
        return this;
    }

    @Override
    @Nonnull
    public Capabilities dismissAllPrompts(boolean returnError) {
        capabilities.put(
                JSON_PROPERTY_UNHANDLED_PROMPT_BEHAVIOR,
                returnError ? UnhandledPromptBehaviorEnum.DISMISS_AND_NOTIFY : UnhandledPromptBehaviorEnum.DISMISS);
        return this;
    }

    @Nonnull
    @Override
    public Proxy proxy() {
        return this;
    }

    @Nonnull
    @Override
    public Capabilities autoConfiguration(@Nonnull String url) {
        com.aerokube.lightning.model.Proxy proxy = new com.aerokube.lightning.model.Proxy()
                .proxyType(PAC).proxyAutoconfigUrl(url);
        capabilities.put(JSON_PROPERTY_PROXY, proxy);
        return this;
    }

    @Nonnull
    @Override
    public Capabilities autoDetect() {
        com.aerokube.lightning.model.Proxy proxy = new com.aerokube.lightning.model.Proxy()
                .proxyType(AUTODETECT);
        capabilities.put(JSON_PROPERTY_PROXY, proxy);
        return this;
    }

    @Nonnull
    @Override
    public Capabilities system() {
        com.aerokube.lightning.model.Proxy proxy = new com.aerokube.lightning.model.Proxy()
                .proxyType(SYSTEM);
        capabilities.put(JSON_PROPERTY_PROXY, proxy);
        return this;
    }

    @Nonnull
    @Override
    public Capabilities http(@Nonnull String hostPort) {
        return http(hostPort, Collections.emptyList());
    }

    @Nonnull
    @Override
    public Capabilities http(@Nonnull String hostPort, @Nonnull List<String> hostsThatBypassProxy) {
        com.aerokube.lightning.model.Proxy proxy = new com.aerokube.lightning.model.Proxy()
                .proxyType(MANUAL).httpProxy(hostPort)
                .sslProxy(hostPort).noProxy(hostsThatBypassProxy);
        capabilities.put(JSON_PROPERTY_PROXY, proxy);
        return this;
    }

    @Nonnull
    @Override
    public Capabilities socks(@Nonnull String hostPort, int socksVersion) {
        com.aerokube.lightning.model.Proxy proxy = new com.aerokube.lightning.model.Proxy()
                .proxyType(MANUAL).socksVersion(socksVersion).socksProxy(hostPort);
        capabilities.put(JSON_PROPERTY_PROXY, proxy);
        return this;
    }

    @Override
    @Nonnull
    public Capabilities capability(@Nonnull String key, @Nonnull Serializable value) {
        capabilities.put(key, value);
        return this;
    }


}
