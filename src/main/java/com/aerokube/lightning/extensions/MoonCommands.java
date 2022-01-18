package com.aerokube.lightning.extensions;

import com.aerokube.lightning.WebDriver;
import com.aerokube.lightning.WebDriverExtension;
import com.aerokube.lightning.api.AerokubeApi;
import com.aerokube.lightning.model.StringResponse;

import javax.annotation.Nonnull;
import java.util.Base64;
import java.util.List;

public class MoonCommands extends WebDriverExtension {

    private final AerokubeApi aerokubeApi;

    public MoonCommands(WebDriver webDriver) {
        super(webDriver);
        aerokubeApi = api(AerokubeApi.class);
    }

    @Nonnull
    public MoonCommands updateClipboard(@Nonnull byte[] content) {
        execute(() -> {
            String encodedContent = Base64.getEncoder().encodeToString(content);
            aerokubeApi.updateClipboard(getSessionId(), new StringResponse().value(encodedContent));
            return null;
        });
        return this;
    }

    @Nonnull
    public byte[] getClipboard() {
        return execute(() -> {
            String encodedBytes = aerokubeApi.getClipboard(getSessionId()).getValue();
            return Base64.getDecoder().decode(encodedBytes);
        });
    }

    @Nonnull
    public List<String> listFiles() {
        return execute(() -> aerokubeApi.listRemoteFiles(getSessionId(), true).getValue());
    }

    @Nonnull
    public byte[] downloadFile(@Nonnull String file) {
        return execute(() -> {
            String encodedBytes = aerokubeApi.downloadRemoteFile(getSessionId(), file).getValue();
            return Base64.getDecoder().decode(encodedBytes);
        });
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
