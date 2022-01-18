package com.aerokube.lightning.extensions;

import com.aerokube.lightning.FileUtils;
import com.aerokube.lightning.WebDriver;
import com.aerokube.lightning.WebDriverExtension;
import com.aerokube.lightning.api.MozillaApi;
import com.aerokube.lightning.model.AddonInstallRequest;
import com.aerokube.lightning.model.AddonUninstallRequest;
import com.aerokube.lightning.model.ContextRequest;
import com.aerokube.lightning.model.FirefoxContext;

import javax.annotation.Nonnull;
import java.nio.file.Path;
import java.util.Base64;

public class FirefoxCommands extends WebDriverExtension {

    private final MozillaApi mozillaApi;

    public FirefoxCommands(WebDriver webDriver) {
        super(webDriver);
        mozillaApi = api(MozillaApi.class);
    }

    @Nonnull
    public String installAddon(@Nonnull Path addon) {
        String encodedAddon = FileUtils.encodeFileToBase64(addon);
        AddonInstallRequest addonInstallRequest = new AddonInstallRequest()
                .addon(encodedAddon).temporary(false);
        return execute(() -> mozillaApi.installAddon(getSessionId(), addonInstallRequest).getValue());
    }

    @Nonnull
    public FirefoxCommands uninstallAddon(@Nonnull String id) {
        AddonUninstallRequest addonUninstallRequest = new AddonUninstallRequest().id(id);
        execute(() -> mozillaApi.uninstallAddon(getSessionId(), addonUninstallRequest));
        return this;
    }

    @Nonnull
    public FirefoxCommands context(@Nonnull FirefoxContext context) {
        ContextRequest contextRequest = new ContextRequest().context(context);
        execute(() -> mozillaApi.setContext(getSessionId(), contextRequest));
        return this;
    }

    @Nonnull
    public FirefoxContext context() {
        String context = execute(() -> mozillaApi.getContext(getSessionId()).getValue());
        return FirefoxContext.fromValue(context);
    }

    @Nonnull
    public byte[] fullScreenshot() {
        return execute(() -> {
            String encodedBytes = mozillaApi.takeFullScreenshot(getSessionId()).getValue();
            return Base64.getDecoder().decode(encodedBytes);
        });
    }
}
