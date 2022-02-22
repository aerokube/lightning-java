package com.aerokube.lightning.extensions;

import com.aerokube.lightning.Capabilities;
import com.aerokube.lightning.ExtensionCapabilities;
import com.aerokube.lightning.FileUtils;
import com.aerokube.lightning.model.SelenoidOptions;

import javax.annotation.Nonnull;
import java.nio.file.Path;
import java.time.Duration;

import static com.aerokube.lightning.model.Capabilities.JSON_PROPERTY_SELENOID_COLON_OPTIONS;

public class SelenoidCapabilities extends ExtensionCapabilities {

    private final SelenoidOptions selenoidOptions;

    public SelenoidCapabilities(@Nonnull Capabilities capabilities) {
        super(capabilities);
        this.selenoidOptions = new SelenoidOptions();
        capabilities.capability(JSON_PROPERTY_SELENOID_COLON_OPTIONS, selenoidOptions);
    }

    static String rootCAEnv(@Nonnull Path certificate) {
        String variableName = String.format(
                "ROOT_CA_%s",
                certificate.getFileName().toString().toUpperCase()
                        .replace(" ", "_")
                        .replace("-", "_")
                        .replace(".", "_")
        );
        return String.format("%s=%s", variableName, FileUtils.encodeFileToBase64(certificate));
    }

    @Nonnull
    public SelenoidCapabilities enableLog() {
        selenoidOptions.setEnableLog(true);
        return this;
    }

    @Nonnull
    public SelenoidCapabilities enableVideo() {
        selenoidOptions.setEnableVideo(true);
        return this;
    }

    @Nonnull
    public SelenoidCapabilities enableVNC() {
        selenoidOptions.setEnableVNC(true);
        return this;
    }

    @Nonnull
    public SelenoidCapabilities environmentVariable(@Nonnull String key, @Nonnull String value) {
        selenoidOptions.addEnvItem(String.format("%s=%s", key, value));
        return this;
    }

    @Nonnull
    public SelenoidCapabilities label(@Nonnull String key, @Nonnull String value) {
        selenoidOptions.putLabelsItem(key, value);
        return this;
    }

    @Nonnull
    public SelenoidCapabilities logName(@Nonnull String name) {
        selenoidOptions.setLogName(name);
        return this;
    }

    @Nonnull
    public SelenoidCapabilities name(@Nonnull String name) {
        selenoidOptions.setName(name);
        return this;
    }

    @Nonnull
    public SelenoidCapabilities screenResolution(@Nonnull String screenResolution) {
        selenoidOptions.setScreenResolution(screenResolution);
        return this;
    }

    @Nonnull
    public SelenoidCapabilities sessionTimeout(@Nonnull Duration duration) {
        selenoidOptions.setSessionTimeout(ExtensionCapabilities.golangDuration(duration));
        return this;
    }

    @Nonnull
    public SelenoidCapabilities s3KeyPattern(@Nonnull String pattern) {
        selenoidOptions.setS3KeyPattern(pattern);
        return this;
    }

    @Nonnull
    public SelenoidCapabilities timeZone(@Nonnull String timeZone) {
        selenoidOptions.setTimeZone(timeZone);
        return this;
    }

    @Nonnull
    public SelenoidCapabilities videoFrameRate(int frameRate) {
        selenoidOptions.setVideoFrameRate((long) frameRate);
        return this;
    }

    @Nonnull
    public SelenoidCapabilities videoName(@Nonnull String videoName) {
        selenoidOptions.setVideoName(videoName);
        return this;
    }

    @Nonnull
    public SelenoidCapabilities videoScreenSize(@Nonnull String videoScreenSize) {
        selenoidOptions.setVideoScreenSize(videoScreenSize);
        return this;
    }

    @Nonnull
    public SelenoidCapabilities rootCertificationAuthority(@Nonnull Path certificate) {
        selenoidOptions.addEnvItem(rootCAEnv(certificate));
        return this;
    }
}
