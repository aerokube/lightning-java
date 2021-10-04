package com.aerokube.lightning;

import com.aerokube.lightning.model.FirefoxOptionsLog.LevelEnum;

import javax.annotation.Nonnull;
import java.nio.file.Path;
import java.time.Duration;
import java.util.List;

public interface Capabilities {

    @Nonnull
    static Capabilities create() {
        return new StdCapabilities();
    }

    @Nonnull
    Capabilities browserName(@Nonnull String browserName);

    @Nonnull
    Capabilities browserVersion(@Nonnull String browserVersion);

    @Nonnull
    Capabilities platformName(@Nonnull String platformName);

    @Nonnull
    Capabilities enableStrictFileInteractability();

    @Nonnull
    Capabilities acceptInsecureCerts();

    @Nonnull
    Capabilities doNotWaitForPageLoad();

    @Nonnull
    Capabilities waitForPageDOMLoadOnly();

    @Nonnull
    Capabilities acceptAllPrompts(boolean returnError);

    @Nonnull
    Capabilities dismissAllPrompts(boolean returnError);

    @Nonnull
    Proxy proxy();

    @Nonnull
    Capabilities capability(@Nonnull String key, @Nonnull Object value);

    @Nonnull
    Chrome chrome();

    @Nonnull
    Firefox firefox();

    @Nonnull
    Edge edge();

    @Nonnull
    Opera opera();

    @Nonnull
    Selenoid selenoid();

    @Nonnull
    Moon moon();

    @Nonnull
    Safari safari();

    @Nonnull
    com.aerokube.lightning.model.Capabilities raw();

    interface Proxy {

        @Nonnull
        Capabilities autoConfiguration(@Nonnull String url);

        @Nonnull
        Capabilities autoDetect();

        @Nonnull
        Capabilities system();

        @Nonnull
        Capabilities http(@Nonnull String hostPort);

        @Nonnull
        Capabilities http(@Nonnull String hostPort, @Nonnull List<String> hostsThatBypassProxy);

        @Nonnull
        Capabilities socks(@Nonnull String hostPort, int socksVersion);

    }

    interface Chrome extends Capabilities {

        @Nonnull
        Chrome args(@Nonnull String... args);

        @Nonnull
        Chrome binary(@Nonnull String binary);

        @Nonnull
        Chrome debuggerAddress(@Nonnull String address);

        @Nonnull
        Chrome detach();

        @Nonnull
        Chrome excludeSwitches(@Nonnull String... switches);

        @Nonnull
        Chrome extensions(@Nonnull Path... extensions);

        @Nonnull
        Chrome minidumpPath(@Nonnull String path);

        @Nonnull
        MobileEmulation mobileEmulation();

        @Nonnull
        PerformanceLogging performanceLogging();

        @Nonnull
        Chrome windowTypes(@Nonnull String... types);

        interface MobileEmulation extends Capabilities {

            @Nonnull
            Chrome deviceName(@Nonnull String deviceName);

            @Nonnull
            DeviceMetrics deviceMetrics();

            @Nonnull
            MobileEmulation userAgent(@Nonnull String userAgent);

            interface DeviceMetrics extends MobileEmulation {

                @Nonnull
                DeviceMetrics width(int width);

                @Nonnull
                DeviceMetrics height(int height);

                @Nonnull
                DeviceMetrics pixelRatio(float ratio);

                @Nonnull
                DeviceMetrics touch();

            }

        }

        interface PerformanceLogging extends Capabilities {

            @Nonnull
            PerformanceLogging enableNetwork();

            @Nonnull
            PerformanceLogging enablePage();

            @Nonnull
            PerformanceLogging traceCategories(@Nonnull String... categories);

            @Nonnull
            PerformanceLogging bufferUsageReportingInterval(@Nonnull Duration interval);

        }

    }

    interface Edge extends Capabilities {

        @Nonnull
        Edge args(@Nonnull String... args);

        @Nonnull
        Edge binary(@Nonnull String binary);

        @Nonnull
        Edge extensions(@Nonnull Path... extensions);

    }

    interface Firefox extends Capabilities {

        @Nonnull
        Firefox androidPackage(@Nonnull String pkg);

        @Nonnull
        Firefox androidActivity(@Nonnull String activity);

        @Nonnull
        Firefox androidDeviceSerial(@Nonnull String serial);

        @Nonnull
        Firefox androidIntentArguments(@Nonnull String... arguments);

        @Nonnull
        Firefox log(@Nonnull LevelEnum level);

        @Nonnull
        Firefox environmentVariable(@Nonnull String key, @Nonnull String value);

        @Nonnull
        Firefox preference(@Nonnull String key, @Nonnull String value);

        @Nonnull
        Firefox preference(@Nonnull String key, boolean value);

        @Nonnull
        Firefox preference(@Nonnull String key, int value);

        @Nonnull
        Firefox profile(@Nonnull Path directory);

    }

    interface Moon extends Capabilities {

        @Nonnull
        Moon enableVNC();

        @Nonnull
        Moon environmentVariable(@Nonnull String key, @Nonnull String value);

        @Nonnull
        Moon logName(@Nonnull String name);

        @Nonnull
        Moon name(@Nonnull String name);

        @Nonnull
        Moon screenResolution(@Nonnull String screenResolution);

        @Nonnull
        Moon sessionTimeout(@Nonnull Duration duration);

        @Nonnull
        Moon s3KeyPattern(@Nonnull String pattern);

        @Nonnull
        Moon timeZone(@Nonnull String timeZone);

        @Nonnull
        Moon videoFrameRate(int frameRate);

        @Nonnull
        Moon videoName(@Nonnull String videoName);

        @Nonnull
        Moon videoScreenSize(@Nonnull String videoScreenSize);

        @Nonnull
        MobileDevice mobileDevice();

        interface MobileDevice extends Capabilities {

            @Nonnull
            MobileDevice deviceName(@Nonnull String name);

            @Nonnull
            MobileDevice landscape();

        }

    }

    interface Opera extends Capabilities {

        @Nonnull
        Opera args(@Nonnull String... args);

        @Nonnull
        Opera binary(@Nonnull String binary);

        @Nonnull
        Opera extensions(@Nonnull Path... extension);

    }

    interface Selenoid extends Capabilities {

        @Nonnull
        Selenoid enableLog();

        @Nonnull
        Selenoid enableVideo();

        @Nonnull
        Selenoid enableVNC();

        @Nonnull
        Selenoid environmentVariable(@Nonnull String key, @Nonnull String value);

        @Nonnull
        Selenoid logName(@Nonnull String name);

        @Nonnull
        Selenoid name(@Nonnull String name);

        @Nonnull
        Selenoid screenResolution(@Nonnull String screenResolution);

        @Nonnull
        Selenoid sessionTimeout(@Nonnull Duration duration);

        @Nonnull
        Selenoid s3KeyPattern(@Nonnull String pattern);

        @Nonnull
        Selenoid timeZone(@Nonnull String timeZone);

        @Nonnull
        Selenoid videoFrameRate(int frameRate);

        @Nonnull
        Selenoid videoName(@Nonnull String videoName);

        @Nonnull
        Selenoid videoScreenSize(@Nonnull String videoScreenSize);

    }

    interface Safari extends Capabilities {

        @Nonnull
        Safari automaticInspection();

        @Nonnull
        Safari automaticProfiling();

    }

}
