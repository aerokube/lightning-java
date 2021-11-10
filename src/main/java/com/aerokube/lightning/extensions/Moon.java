package com.aerokube.lightning.extensions;

import com.aerokube.lightning.Capabilities;
import com.aerokube.lightning.ExtensionCapabilities;
import com.aerokube.lightning.model.MoonMobileDevice;
import com.aerokube.lightning.model.MoonOptions;

import javax.annotation.Nonnull;
import java.nio.file.Path;
import java.time.Duration;

import static com.aerokube.lightning.extensions.Selenoid.rootCAEnv;
import static com.aerokube.lightning.model.Capabilities.JSON_PROPERTY_MOON_COLON_OPTIONS;
import static com.aerokube.lightning.model.MoonMobileDevice.OrientationEnum.LANDSCAPE;

public class Moon extends ExtensionCapabilities implements MobileDevice {

    private final MoonOptions moonOptions;
    private final MoonMobileDevice moonMobileDevice;

    public Moon(@Nonnull Capabilities capabilities) {
        super(capabilities);
        this.moonOptions = new MoonOptions();
        this.moonMobileDevice = new MoonMobileDevice();
        capabilities.capability(JSON_PROPERTY_MOON_COLON_OPTIONS, moonOptions);
    }

    @Nonnull
    public Moon enableVNC() {
        moonOptions.setEnableVNC(true);
        return this;
    }

    @Nonnull
    public Moon environmentVariable(@Nonnull String key, @Nonnull String value) {
        moonOptions.addEnvItem(String.format("%s=%s", key, value));
        return this;
    }

    @Nonnull
    public Moon logName(@Nonnull String name) {
        moonOptions.setLogName(name);
        return this;
    }

    @Nonnull
    public Moon name(@Nonnull String name) {
        moonOptions.setName(name);
        return this;
    }

    @Nonnull
    public Moon screenResolution(@Nonnull String screenResolution) {
        moonOptions.setScreenResolution(screenResolution);
        return this;
    }

    @Nonnull
    public Moon sessionTimeout(@Nonnull Duration duration) {
        moonOptions.setSessionTimeout(ExtensionCapabilities.golangDuration(duration));
        return this;
    }

    @Nonnull
    public Moon s3KeyPattern(@Nonnull String pattern) {
        moonOptions.setS3KeyPattern(pattern);
        return this;
    }

    @Nonnull
    public Moon timeZone(@Nonnull String timeZone) {
        moonOptions.setTimeZone(timeZone);
        return this;
    }

    @Nonnull
    public Moon videoFrameRate(int frameRate) {
        moonOptions.setVideoFrameRate((long) frameRate);
        return this;
    }

    @Nonnull
    public Moon videoName(@Nonnull String videoName) {
        moonOptions.setVideoName(videoName);
        return this;
    }

    @Nonnull
    public Moon videoScreenSize(@Nonnull String videoScreenSize) {
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

    @Nonnull
    public Moon rootCertificationAuthority(@Nonnull Path certificate) {
        moonOptions.addEnvItem(rootCAEnv(certificate));
        return this;
    }

}
