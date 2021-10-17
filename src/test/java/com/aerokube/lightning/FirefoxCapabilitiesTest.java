package com.aerokube.lightning;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static com.aerokube.lightning.Image.FIREFOX;
import static com.aerokube.lightning.model.FirefoxOptionsLog.LevelEnum.DEBUG;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

public class FirefoxCapabilitiesTest extends BaseTest {

    @Override
    protected Image getImage() {
        return FIREFOX;
    }

    @Override
    protected String getUri(int port) {
        return String.format("http://localhost:%s/wd/hub", port);
    }

    @Test
    void testBasicCapabilities() {
        test(
                () -> Capabilities.create()
                        .firefox()
                        .binary("/usr/bin/firefox").args("-headless")
                        .log(DEBUG)
                        .environmentVariable("LANG", "en_US.UTF-8")
                        .preference("intl.accept_languages", "en,ru")
                        .preference("app.update.background.enabled", true)
                        .preference("app.update.interval", 45000),
                driver -> driver.navigation().navigate("https://example.com")
        );
    }

    @Test
    void testUploadProfile() throws Exception {
        /*
            1) Create profile with temporary file
            2) Upload profile
            3) Profile path in container is returned in capabilities
            4) Copy file from container and compare contents
         */
        Path profileDir = Files.createTempDirectory("lightning");
        final String testFileName = "test-file";
        final String testString = "test-string";
        Path filePath = profileDir.resolve(testFileName);
        Files.writeString(filePath, testString);

        Path copiedFileDir = Files.createTempDirectory("lightning");
        test(
                () -> Capabilities.create()
                        .firefox()
                        .profile(profileDir),
                driver -> {
                    String remotePath = String.valueOf(driver.getCapabilities().capability("moz:profile"));

                    Path uploadedFilePath = Paths.get(remotePath).resolve(testFileName);
                    Path copiedFilePath = copiedFileDir.resolve(testFileName);
                    browserContainer.copyFileFromContainer(
                            uploadedFilePath.toAbsolutePath().toString(),
                            copiedFilePath.toAbsolutePath().toString()
                    );
                    try {
                        String copiedFileContents = new String(Files.readAllBytes(copiedFilePath));
                        assertThat(copiedFileContents, is(equalTo(testString)));
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
        );
    }

}
