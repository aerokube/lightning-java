package com.aerokube.lightning;

import com.aerokube.lightning.ExtensionCapabilities.*;

import javax.annotation.Nonnull;
import java.lang.reflect.Constructor;
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
    public Capabilities capability(@Nonnull String key, @Nonnull Object value) {
        capabilities.put(key, value);
        return this;
    }

    @Nonnull
    @Override
    public Object capability(@Nonnull String key) {
        return capabilities.get(key);
    }

    @Nonnull
    @Override
    public Chrome chrome() {
        return new ChromeCapabilities(this);
    }

    @Nonnull
    @Override
    public Firefox firefox() {
        return new FirefoxCapabilities(this);
    }

    @Nonnull
    @Override
    public Edge edge() {
        return new EdgeCapabilities(this);
    }

    @Nonnull
    @Override
    public Opera opera() {
        return new OperaCapabilities(this);
    }

    @Override
    public <T extends Capabilities> T extension(Class<T> cls) {
        try {
            Constructor<T> constructor = cls.getConstructor(Capabilities.class);
            return constructor.newInstance(this);
        } catch (Exception e) {
            throw new WebDriverException(String.format("Failed to initialize extension %s", cls.getCanonicalName()), e);
        }
    }

    @Nonnull
    @Override
    public Safari safari() {
        return new SafariCapabilities(this);
    }

}
