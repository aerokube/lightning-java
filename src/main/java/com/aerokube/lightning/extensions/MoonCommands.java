package com.aerokube.lightning.extensions;

import com.aerokube.lightning.FileUtils;
import com.aerokube.lightning.WebDriver;
import com.aerokube.lightning.WebDriverException;
import com.aerokube.lightning.WebDriverExtension;
import com.aerokube.lightning.api.AerokubeApi;
import com.aerokube.lightning.model.ClipboardData;

import javax.annotation.Nonnull;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Base64;
import java.util.List;

public class MoonCommands extends WebDriverExtension {

    private final AerokubeApi aerokubeApi;

    public MoonCommands(WebDriver webDriver) {
        super(webDriver);
        aerokubeApi = api(AerokubeApi.class);
    }

    @Nonnull
    public MoonCommands updateClipboardText(@Nonnull String text) {
        execute(() -> {
            aerokubeApi.updateClipboard(getSessionId(), new ClipboardData().value(text));
            return null;
        });
        return this;
    }

    @Nonnull
    public MoonCommands updateClipboardImage(@Nonnull Path image) {
        execute(() -> {
            String encodedImage = FileUtils.encodeFileToBase64(image);
            aerokubeApi.updateClipboard(
                    getSessionId(),
                    new ClipboardData()
                            .value(encodedImage)
                            .media(ClipboardData.MediaEnum.IMAGE_PNG)
            );
            return null;
        });
        return this;
    }

    @Nonnull
    public String getClipboardText() {
        return execute(() -> {
            ClipboardData clipboard = aerokubeApi.getClipboard(getSessionId());
            if (clipboard.getMedia() != null) {
                throw new WebDriverException(String.format("Received non-text data from the clipboard: %s", clipboard.getMedia()));
            }
            return clipboard.getValue();
        });
    }

    @Nonnull
    public byte[] getClipboardImage() {
        return execute(() -> {
            ClipboardData clipboard = aerokubeApi.getClipboard(getSessionId());
            if (clipboard.getMedia() == null || !ClipboardData.MediaEnum.IMAGE_PNG.equals(clipboard.getMedia())) {
                throw new WebDriverException("Received non-image data from the clipboard");
            }
            return Base64.getDecoder().decode(clipboard.getValue().getBytes(StandardCharsets.UTF_8));
        });
    }

    @Nonnull
    public List<String> listFiles() {
        return execute(() -> aerokubeApi.listRemoteFiles(getSessionId(), true).getValue());
    }

    @Nonnull
    public byte[] downloadFile(@Nonnull String file) {
        return execute(() -> Files.readAllBytes(aerokubeApi.downloadRemoteFile(getSessionId(), file).toPath()));
    }

    @Nonnull
    public MoonCommands deleteFile(@Nonnull String file) {
        execute(() -> {
            aerokubeApi.deleteRemoteFile(getSessionId(), file);
            return null;
        });
        return this;
    }

}
