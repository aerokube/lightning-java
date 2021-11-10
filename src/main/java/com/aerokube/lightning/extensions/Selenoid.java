package com.aerokube.lightning.extensions;

import com.aerokube.lightning.Capabilities;
import com.aerokube.lightning.ExtensionCapabilities;
import com.aerokube.lightning.FileUtils;
import com.aerokube.lightning.model.SelenoidOptions;

import javax.annotation.Nonnull;
import java.nio.file.Path;
import java.time.Duration;

import static com.aerokube.lightning.model.Capabilities.JSON_PROPERTY_SELENOID_COLON_OPTIONS;

public class Selenoid extends ExtensionCapabilities {

    private final SelenoidOptions selenoidOptions;

    public Selenoid(@Nonnull Capabilities capabilities) {
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
    public Selenoid enableLog() {
        selenoidOptions.setEnableLog(true);
        return this;
    }

    @Nonnull
    public Selenoid enableVideo() {
        selenoidOptions.setEnableVideo(true);
        return this;
    }

    @Nonnull
    public Selenoid enableVNC() {
        selenoidOptions.setEnableVNC(true);
        return this;
    }

    @Nonnull
    public Selenoid environmentVariable(@Nonnull String key, @Nonnull String value) {
        selenoidOptions.addEnvItem(String.format("%s=%s", key, value));
        return this;
    }

    @Nonnull
    public Selenoid logName(@Nonnull String name) {
        selenoidOptions.setLogName(name);
        return this;
    }

    @Nonnull
    public Selenoid name(@Nonnull String name) {
        selenoidOptions.setName(name);
        return this;
    }

    @Nonnull
    public Selenoid screenResolution(@Nonnull String screenResolution) {
        selenoidOptions.setScreenResolution(screenResolution);
        return this;
    }

    @Nonnull
    public Selenoid sessionTimeout(@Nonnull Duration duration) {
        selenoidOptions.setSessionTimeout(ExtensionCapabilities.golangDuration(duration));
        return this;
    }

    @Nonnull
    public Selenoid s3KeyPattern(@Nonnull String pattern) {
        selenoidOptions.setS3KeyPattern(pattern);
        return this;
    }

    @Nonnull
    public Selenoid timeZone(@Nonnull String timeZone) {
        selenoidOptions.setTimeZone(timeZone);
        return this;
    }

    @Nonnull
    public Selenoid videoFrameRate(int frameRate) {
        selenoidOptions.setVideoFrameRate((long) frameRate);
        return this;
    }

    @Nonnull
    public Selenoid videoName(@Nonnull String videoName) {
        selenoidOptions.setVideoName(videoName);
        return this;
    }

    @Nonnull
    public Selenoid videoScreenSize(@Nonnull String videoScreenSize) {
        selenoidOptions.setVideoScreenSize(videoScreenSize);
        return this;
    }

    @Nonnull
    public Selenoid rootCertificationAuthority(@Nonnull Path certificate) {
        selenoidOptions.addEnvItem(rootCAEnv(certificate));
        return this;
    }
}
