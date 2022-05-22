package com.aerokube.lightning;

import net.lightbody.bmp.BrowserMobProxy;
import net.lightbody.bmp.BrowserMobProxyServer;
import net.lightbody.bmp.core.har.HarEntry;
import net.lightbody.bmp.proxy.CaptureType;
import org.junit.jupiter.api.Test;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

class NavigationTest extends BaseTest {

    @Test
    void testNavigation() {
        test(driver -> {
            String url = driver.navigation()
                    .navigate("https://example.com/")
                    .navigate("https://example.org/")
                    .refresh()
                    .back()
                    .forward()
                    .getUrl();
            assertThat(url, is(not(emptyString())));
            String title = driver.navigation().getTitle();
            assertThat(title, is(not(emptyString())));
        });
    }

    @Test
    void testAcceptInsecureCerts() {
        test(() -> Capabilities.create().browserName("chrome").acceptInsecureCerts(), driver -> {
            String url = driver.navigation()
                    .navigate("https://self-signed.badssl.com/")
                    .getUrl();
            assertThat(url, is(not(emptyString())));
        });
    }

    @Test
    void testHttpProxy() throws UnknownHostException {
        final BrowserMobProxy proxy = new BrowserMobProxyServer();
        try {
            proxy.start();
            proxy.setHarCaptureTypes(CaptureType.REQUEST_HEADERS);
            proxy.newHar();

            final String proxyHost = String.format(
                    "%s:%d",
                    InetAddress.getLocalHost().getHostAddress(),
                    proxy.getPort()
            );

            test(
                    () -> Capabilities.create().chrome().proxy().http(proxyHost),
                    driver -> {
                        // We intentionally use http here to not deal with certs
                        driver.navigation().navigate("http://example.com/");
                        List<HarEntry> proxyLogEntries = proxy.getHar().getLog().getEntries();
                        assertThat(proxyLogEntries, is(not(empty())));
                    }
            );

        } finally {
            proxy.endHar();
            proxy.stop();
        }

    }

}
