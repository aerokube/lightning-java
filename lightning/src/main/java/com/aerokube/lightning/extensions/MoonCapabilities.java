package com.aerokube.lightning.extensions;

import com.aerokube.lightning.Capabilities;
import com.aerokube.lightning.ExtensionCapabilities;
import com.aerokube.lightning.model.*;

import javax.annotation.Nonnull;
import java.time.Duration;

import static com.aerokube.lightning.model.Capabilities.JSON_PROPERTY_MOON_COLON_OPTIONS;
import static com.aerokube.lightning.model.MoonMobileDevice.OrientationEnum.LANDSCAPE;

public class MoonCapabilities extends ExtensionCapabilities implements MobileDevice {

    private final MoonOptions moonOptions;
    private final MoonMobileDevice moonMobileDevice;

    public MoonCapabilities(@Nonnull Capabilities capabilities) {
        super(capabilities);
        this.moonOptions = new MoonOptions();
        this.moonMobileDevice = new MoonMobileDevice();
        capabilities.capability(JSON_PROPERTY_MOON_COLON_OPTIONS, moonOptions);
    }

    @Nonnull
    public MoonCapabilities additionalFonts() {
        moonOptions.additionalFonts(true);
        return this;
    }

    @Nonnull
    public MoonCapabilities context(String context) {
        moonOptions.context(context);
        return this;
    }

    @Nonnull
    public MoonCapabilities enableVideo() {
        moonOptions.enableVideo(true);
        return this;
    }

    @Nonnull
    public MoonCapabilities environmentVariable(@Nonnull String key, @Nonnull String value) {
        moonOptions.addEnvItem(String.format("%s=%s", key, value));
        return this;
    }

    @Nonnull
    public MoonCapabilities label(@Nonnull String key, @Nonnull String value) {
        moonOptions.putLabelsItem(key, value);
        return this;
    }

    @Nonnull
    public MoonCapabilities logLevel(ChromiumLogLevel logLevel) {
        moonOptions.logLevel(new MoonLogLevel(logLevel));
        return this;
    }

    @Nonnull
    public MoonCapabilities logLevel(FirefoxLogLevel logLevel) {
        moonOptions.logLevel(new MoonLogLevel(logLevel));
        return this;
    }

    @Nonnull
    public MoonCapabilities name(@Nonnull String name) {
        moonOptions.setName(name);
        return this;
    }

    @Nonnull
    public MoonCapabilities nameserver(@Nonnull String nameserver) {
        moonOptions.addNameserversItem(nameserver);
        return this;
    }

    @Nonnull
    public MoonCapabilities host(@Nonnull String domainName, @Nonnull String ipAddress) {
        moonOptions.addHostsItem(String.format("%s:%s", domainName, ipAddress));
        return this;
    }

    @Nonnull
    public MoonCapabilities screenResolution(@Nonnull String screenResolution) {
        moonOptions.setScreenResolution(screenResolution);
        return this;
    }

    @Nonnull
    public MoonCapabilities sessionTimeout(@Nonnull Duration duration) {
        moonOptions.setSessionTimeout(ExtensionCapabilities.golangDuration(duration));
        return this;
    }

    @Nonnull
    public MoonCapabilities pattern(@Nonnull String pattern) {
        moonOptions.setPattern(pattern);
        return this;
    }

    @Nonnull
    public MoonCapabilities timeZone(@Nonnull String timeZone) {
        return environmentVariable("TZ", timeZone);
    }

    @Nonnull
    public MoonCapabilities videoFrameRate(int frameRate) {
        moonOptions.setVideoFrameRate((long) frameRate);
        return this;
    }

    @Nonnull
    public MoonCapabilities videoName(@Nonnull String videoName) {
        moonOptions.setVideoName(videoName);
        return this;
    }

    @Nonnull
    public MoonCapabilities videoScreenSize(@Nonnull String videoScreenSize) {
        moonOptions.setVideoScreenSize(videoScreenSize);
        return this;
    }

    @Nonnull
    public MobileDevice mobileDevice() {
        moonOptions.setMobileDevice(moonMobileDevice);
        return this;
    }

    @Nonnull
    public MobileDevice deviceName(@Nonnull String name) {
        moonMobileDevice.setDeviceName(name);
        return this;
    }

    @Nonnull
    public MobileDevice landscape() {
        moonMobileDevice.setOrientation(LANDSCAPE);
        return this;
    }

}
