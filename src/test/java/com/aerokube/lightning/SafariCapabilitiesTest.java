package com.aerokube.lightning;

import org.junit.jupiter.api.Test;

import static com.aerokube.lightning.Image.SAFARI;

public class SafariCapabilitiesTest extends BaseTest {

    @Override
    protected Image getImage() {
        return SAFARI;
    }

    @Test
    void testBasicCapabilities() {
        test(
                () -> Capabilities.create()
                        .safari().automaticInspection()
                        .automaticProfiling(),
                driver -> driver.navigation().navigate("https://example.com")
        );
    }

}
