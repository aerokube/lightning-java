package com.aerokube.lightning.extensions;

import com.aerokube.lightning.BaseTest;
import com.aerokube.lightning.Capabilities;
import com.aerokube.lightning.Image;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.nio.file.Path;
import java.nio.file.Paths;

import static com.aerokube.lightning.Image.FIREFOX;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@Disabled("need to debug session startup in Firefox images when selenoid:options is passed")
public class SelenoidTest extends BaseTest {

    @Override
    protected Image getImage() {
        return FIREFOX;
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
