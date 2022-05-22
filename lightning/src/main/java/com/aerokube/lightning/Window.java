package com.aerokube.lightning;

import javax.annotation.Nonnull;

public interface Window {

    @Nonnull
    Window close();

    @Nonnull
    Window fullscreen();

    @Nonnull
    Window maximize();

    @Nonnull
    Window minimize();

    @Nonnull
    Window setSize(int width, int height);

    @Nonnull
    Size getSize();

    @Nonnull
    Position getPosition();

    @Nonnull
    Window setPosition(int x, int y);

    @Nonnull
    Window switchTo();

    @Nonnull
    String getHandle();
}
