package com.aerokube.lightning.extensions;

import com.aerokube.lightning.*;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;

import static com.aerokube.lightning.Image.FIREFOX;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.Matchers.emptyString;

@Disabled("need to debug session startup in Firefox images when selenoid:options is passed")
public class SelenoidTest extends BaseTest {

    @Override
    protected Image getImage() {
        return FIREFOX;
    }

    @Override
    protected String getUri(int port) {
        return String.format("http://localhost:%s/wd/hub", port);
    }

    @Test
    void testRootCertificationAuthority() {
        Path certPath = Paths.get("src", "test", "resources", "self-signed-root-ca.crt");
        test(
                () -> Capabilities.create()
                        .firefox()
                        .extension(Selenoid.class).rootCertificationAuthority(certPath),
                driver -> {
                    String url = driver.navigation().navigate("https://self-signed.badssl.com/")
                            .getUrl();
                    assertThat(url, is(not(emptyString())));
                }
        );
    }

}
