package com.aerokube.lightning.extensions;

import com.aerokube.lightning.Capabilities;

import javax.annotation.Nonnull;

public interface MobileDevice extends Capabilities {

    @Nonnull
    MobileDevice deviceName(@Nonnull String name);

    @Nonnull
    MobileDevice landscape();

}
