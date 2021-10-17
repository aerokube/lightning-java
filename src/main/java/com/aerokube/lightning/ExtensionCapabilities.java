package com.aerokube.lightning;

import com.aerokube.lightning.Capabilities.Chrome.MobileEmulation.DeviceMetrics;
import com.aerokube.lightning.model.*;

import javax.annotation.Nonnull;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;
import java.util.Arrays;
import java.util.function.Consumer;
import java.util.function.Supplier;

import static com.aerokube.lightning.FileUtils.*;
import static com.aerokube.lightning.model.Capabilities.*;
import static com.aerokube.lightning.model.LogLevel.ALL;
import static com.aerokube.lightning.model.LogType.PERFORMANCE;
import static com.aerokube.lightning.model.MoonMobileDevice.OrientationEnum.LANDSCAPE;

public class ExtensionCapabilities implements Capabilities {

    protected final Capabilities capabilities;

    ExtensionCapabilities(Capabilities capabilities) {
        this.capabilities = capabilities;
    }

    private static <T> void update(Supplier<T> source, Consumer<T> action) {
        T object = source.get();
        if (object != null) {
            action.accept(object);
        }
    }

    private static String golangDuration(Duration duration) {
        return String.format("%dms", duration.toMillis());
    }

    @Override
    @Nonnull
    public Capabilities browserName(@Nonnull String browserName) {
        return capabilities.browserName(browserName);
    }

    @Override
    @Nonnull
    public Capabilities browserVersion(@Nonnull String browserVersion) {
        return capabilities.browserVersion(browserVersion);
    }

    @Override
    @Nonnull
    public Capabilities platformName(@Nonnull String platformName) {
        return capabilities.platformName(platformName);
    }

    @Override
    @Nonnull
    public Capabilities enableStrictFileInteractability() {
        return capabilities.enableStrictFileInteractability();
    }

    @Override
    @Nonnull
    public Capabilities acceptInsecureCerts() {
        return capabilities.acceptInsecureCerts();
    }

    @Override
    @Nonnull
    public Capabilities doNotWaitForPageLoad() {
        return capabilities.doNotWaitForPageLoad();
    }

    @Override
    @Nonnull
    public Capabilities waitForPageDOMLoadOnly() {
        return capabilities.waitForPageDOMLoadOnly();
    }

    @Override
    @Nonnull
    public Capabilities acceptAllPrompts(boolean returnError) {
        return capabilities.acceptAllPrompts(returnError);
    }

    @Override
    @Nonnull
    public Capabilities dismissAllPrompts(boolean returnError) {
        return capabilities.dismissAllPrompts(returnError);
    }

    @Override
    @Nonnull
    public Proxy proxy() {
        return capabilities.proxy();
    }

    @Override
    @Nonnull
    public Capabilities capability(@Nonnull String key, @Nonnull Object value) {
        return capabilities.capability(key, value);
    }

    @Nonnull
    @Override
    public Object capability(@Nonnull String key) {
        return capabilities.capability(key);
    }

    @Override
    @Nonnull
    public Chrome chrome() {
        return capabilities.chrome();
    }

    @Override
    @Nonnull
    public Firefox firefox() {
        return capabilities.firefox();
    }

    @Override
    @Nonnull
    public Edge edge() {
        return capabilities.edge();
    }

    @Override
    @Nonnull
    public Opera opera() {
        return capabilities.opera();
    }

    @Override
    @Nonnull
    public Selenoid selenoid() {
        return capabilities.selenoid();
    }

    @Override
    @Nonnull
    public Moon moon() {
        return capabilities.moon();
    }

    @Nonnull
    @Override
    public Safari safari() {
        return capabilities.safari();
    }

    @Override
    @Nonnull
    public com.aerokube.lightning.model.Capabilities raw() {
        return capabilities.raw();
    }

    static class ChromeCapabilities extends ExtensionCapabilities implements Capabilities.Chrome, Chrome.Logging, Chrome.PerformanceLogging, Chrome.MobileEmulation, DeviceMetrics {

        private final ChromeOptions chromeOptions;
        private final LoggingPrefs loggingPrefs;
        private final PerfLoggingPrefs perfLoggingPrefs;
        private final com.aerokube.lightning.model.MobileEmulation mobileEmulation;

        ChromeCapabilities(Capabilities capabilities) {
            super(capabilities);
            this.chromeOptions = new ChromeOptions();
            this.loggingPrefs = new LoggingPrefs();
            this.perfLoggingPrefs = new PerfLoggingPrefs();
            this.mobileEmulation = new com.aerokube.lightning.model.MobileEmulation();
            capabilities
                    .browserName("chrome")
                    .capability(JSON_PROPERTY_GOOG_COLON_CHROME_OPTIONS, chromeOptions);
        }

        @Nonnull
        @Override
        public Chrome args(@Nonnull String... args) {
            chromeOptions.setArgs(Arrays.asList(args));
            return this;
        }

        @Nonnull
        @Override
        public Chrome binary(@Nonnull String binary) {
            chromeOptions.setBinary(binary);
            return this;
        }

        @Nonnull
        @Override
        public Chrome debuggerAddress(@Nonnull String address) {
            chromeOptions.setDebuggerAddress(address);
            return this;
        }

        @Nonnull
        @Override
        public Chrome detach() {
            chromeOptions.setDetach(true);
            return this;
        }

        @Nonnull
        @Override
        public Chrome excludeSwitches(@Nonnull String... switches) {
            if (switches.length > 0) {
                chromeOptions.setExcludeSwitches(Arrays.asList(switches));
            }
            return this;
        }

        @Nonnull
        @Override
        public Chrome extensions(@Nonnull Path... extensions) {
            if (extensions.length > 0) {
                chromeOptions.setExtensions(serializeFiles(extensions));
            }
            return this;
        }

        @Nonnull
        @Override
        public Chrome minidumpPath(@Nonnull String path) {
            chromeOptions.setMinidumpPath(path);
            return this;
        }

        @Nonnull
        @Override
        public MobileEmulation mobileEmulation() {
            chromeOptions.setMobileEmulation(mobileEmulation);
            return this;
        }

        @Nonnull
        @Override
        public Logging logging() {
            return this;
        }

        @Nonnull
        @Override
        public PerformanceLogging performanceLogging() {
            log(PERFORMANCE, ALL);
            chromeOptions.setPerfLoggingPrefs(perfLoggingPrefs);
            return this;
        }

        @Nonnull
        @Override
        public Chrome windowTypes(@Nonnull String... types) {
            if (types.length > 0) {
                chromeOptions.setWindowTypes(Arrays.asList(types));
            }
            return this;
        }

        @Nonnull
        @Override
        public Chrome deviceName(@Nonnull String deviceName) {
            mobileEmulation.setDeviceName(deviceName);
            return this;
        }

        @Nonnull
        @Override
        public DeviceMetrics deviceMetrics() {
            mobileEmulation.setDeviceMetrics(new MobileEmulationDeviceMetrics());
            return this;
        }

        @Nonnull
        @Override
        public MobileEmulation userAgent(@Nonnull String userAgent) {
            mobileEmulation.setUserAgent(userAgent);
            return this;
        }

        @Nonnull
        @Override
        public DeviceMetrics width(int width) {
            update(mobileEmulation::getDeviceMetrics, dm -> dm.setWidth(width));
            return this;
        }

        @Nonnull
        @Override
        public DeviceMetrics height(int height) {
            update(mobileEmulation::getDeviceMetrics, dm -> dm.setHeight(height));
            return this;
        }

        @Nonnull
        @Override
        public DeviceMetrics pixelRatio(float ratio) {
            update(mobileEmulation::getDeviceMetrics, dm -> dm.setPixelRatio(ratio));
            return this;
        }

        @Nonnull
        @Override
        public DeviceMetrics touch() {
            update(mobileEmulation::getDeviceMetrics, dm -> dm.setTouch(true));
            return this;
        }

        @Nonnull
        @Override
        public PerformanceLogging enableNetwork() {
            update(() -> perfLoggingPrefs, pl -> pl.setEnableNetwork(true));
            return this;
        }

        @Nonnull
        @Override
        public PerformanceLogging enablePage() {
            update(() -> perfLoggingPrefs, pl -> pl.setEnablePage(true));
            return this;
        }

        @Nonnull
        @Override
        public PerformanceLogging traceCategories(@Nonnull String... categories) {
            if (categories.length > 0) {
                update(() -> perfLoggingPrefs, pl -> pl.setTraceCategories(String.join(",", categories)));
            }
            return this;
        }

        @Nonnull
        @Override
        public PerformanceLogging bufferUsageReportingInterval(@Nonnull Duration interval) {
            update(() -> perfLoggingPrefs, pl -> pl.setBufferUsageReportingInterval(interval.toMillis()));
            return this;
        }

        @Nonnull
        @Override
        public Logging log(@Nonnull LogType type, @Nonnull LogLevel level) {
            switch (type) {
                case BROWSER:
                    loggingPrefs.browser(level);
                    break;
                case CLIENT:
                    loggingPrefs.client(level);
                    break;
                case DRIVER:
                    loggingPrefs.driver(level);
                    break;
                case PERFORMANCE:
                    loggingPrefs.performance(level);
                    break;
                case PROFILER:
                    loggingPrefs.profiler(level);
                    break;
                case SERVER:
                    loggingPrefs.server(level);
                    break;
            }
            capabilities.capability(JSON_PROPERTY_GOOG_COLON_LOGGING_PREFS, loggingPrefs);
            return this;
        }
    }

    static class EdgeCapabilities extends ExtensionCapabilities implements Capabilities.Edge {

        private final EdgeOptions edgeOptions;

        EdgeCapabilities(Capabilities capabilities) {
            super(capabilities);
            this.edgeOptions = new EdgeOptions();
            capabilities
                    .browserName("MicrosoftEdge")
                    .capability(JSON_PROPERTY_MS_COLON_EDGE_OPTIONS, edgeOptions);
        }

        @Nonnull
        @Override
        public Edge args(@Nonnull String... args) {
            if (args.length > 0) {
                edgeOptions.setArgs(Arrays.asList(args));
            }
            return this;
        }

        @Nonnull
        @Override
        public Edge binary(@Nonnull String binary) {
            edgeOptions.setBinary(binary);
            return this;
        }

        @Nonnull
        @Override
        public Edge extensions(@Nonnull Path... extensions) {
            if (extensions.length > 0) {
                edgeOptions.setExtensions(serializeFiles(extensions));
            }
            return this;
        }
    }

    static class OperaCapabilities extends ExtensionCapabilities implements Capabilities.Opera {

        private final OperaOptions operaOptions;

        OperaCapabilities(Capabilities capabilities) {
            super(capabilities);
            this.operaOptions = new OperaOptions();
            capabilities
                    .browserName("opera")
                    .capability(JSON_PROPERTY_OPERA_OPTIONS, operaOptions);
        }

        @Nonnull
        @Override
        public Opera args(@Nonnull String... args) {
            if (args.length > 0) {
                operaOptions.setArgs(Arrays.asList(args));
            }
            return this;
        }

        @Nonnull
        @Override
        public Opera binary(@Nonnull String binary) {
            operaOptions.setBinary(binary);
            return this;
        }

        @Nonnull
        @Override
        public Opera extensions(@Nonnull Path... extensions) {
            if (extensions.length > 0) {
                operaOptions.setExtensions(serializeFiles(extensions));
            }
            return this;
        }
    }

    static class FirefoxCapabilities extends ExtensionCapabilities implements Capabilities.Firefox {

        private final FirefoxOptions firefoxOptions;

        FirefoxCapabilities(Capabilities capabilities) {
            super(capabilities);
            this.firefoxOptions = new FirefoxOptions();
            capabilities
                    .browserName("firefox")
                    .capability(JSON_PROPERTY_MOZ_COLON_FIREFOX_OPTIONS, firefoxOptions);
        }

        @Nonnull
        @Override
        public Firefox androidPackage(@Nonnull String pkg) {
            firefoxOptions.setAndroidPackage(pkg);
            return this;
        }

        @Nonnull
        @Override
        public Firefox androidActivity(@Nonnull String activity) {
            firefoxOptions.setAndroidActivity(activity);
            return this;
        }

        @Nonnull
        @Override
        public Firefox androidDeviceSerial(@Nonnull String serial) {
            firefoxOptions.setAndroidDeviceSerial(serial);
            return this;
        }

        @Nonnull
        @Override
        public Firefox androidIntentArguments(@Nonnull String... arguments) {
            if (arguments.length > 0) {
                firefoxOptions.setAndroidIntentArguments(Arrays.asList(arguments));
            }
            return this;
        }

        @Nonnull
        @Override
        public Firefox args(@Nonnull String... args) {
            if (args.length > 0) {
                firefoxOptions.setArgs(Arrays.asList(args));
            }
            return this;
        }

        @Nonnull
        @Override
        public Firefox binary(@Nonnull String binary) {
            firefoxOptions.setBinary(binary);
            return this;
        }

        @Nonnull
        @Override
        public Firefox log(@Nonnull FirefoxOptionsLog.LevelEnum level) {
            firefoxOptions.setLog(new FirefoxOptionsLog().level(level));
            return this;
        }

        @Nonnull
        @Override
        public Firefox environmentVariable(@Nonnull String key, @Nonnull String value) {
            firefoxOptions.putEnvItem(key, value);
            return this;
        }

        @Nonnull
        @Override
        public Firefox preference(@Nonnull String key, @Nonnull String value) {
            firefoxOptions.putPrefsItem(key, new PreferenceValue(value));
            return this;
        }

        @Nonnull
        @Override
        public Firefox preference(@Nonnull String key, boolean value) {
            firefoxOptions.putPrefsItem(key, new PreferenceValue(value));
            return this;
        }

        @Nonnull
        @Override
        public Firefox preference(@Nonnull String key, int value) {
            firefoxOptions.putPrefsItem(key, new PreferenceValue(value));
            return this;
        }

        @Nonnull
        @Override
        public Firefox profile(@Nonnull Path directory) {
            if (!Files.exists(directory)) {
                throw new WebDriverException(String.format("Profile directory does not exist: %s", directory.toAbsolutePath()));
            }
            String serializedProfile = encodeFileToBase64(zipDirectory(directory));
            firefoxOptions.profile(serializedProfile);
            return this;
        }
    }

    static class SelenoidCapabilities extends ExtensionCapabilities implements Capabilities.Selenoid {

        private final SelenoidOptions selenoidOptions;

        SelenoidCapabilities(Capabilities capabilities) {
            super(capabilities);
            this.selenoidOptions = new SelenoidOptions();
            capabilities.capability(JSON_PROPERTY_SELENOID_COLON_OPTIONS, selenoidOptions);
        }

        @Nonnull
        @Override
        public Capabilities.Selenoid enableLog() {
            selenoidOptions.setEnableLog(true);
            return this;
        }

        @Nonnull
        @Override
        public Capabilities.Selenoid enableVideo() {
            selenoidOptions.setEnableVideo(true);
            return this;
        }

        @Nonnull
        @Override
        public Capabilities.Selenoid enableVNC() {
            selenoidOptions.setEnableVNC(true);
            return this;
        }

        @Nonnull
        @Override
        public Capabilities.Selenoid environmentVariable(@Nonnull String key, @Nonnull String value) {
            selenoidOptions.addEnvItem(String.format("%s=%s", key, value));
            return this;
        }

        @Nonnull
        @Override
        public Capabilities.Selenoid logName(@Nonnull String name) {
            selenoidOptions.setLogName(name);
            return this;
        }

        @Nonnull
        @Override
        public Capabilities.Selenoid name(@Nonnull String name) {
            selenoidOptions.setName(name);
            return this;
        }

        @Nonnull
        @Override
        public Capabilities.Selenoid screenResolution(@Nonnull String screenResolution) {
            selenoidOptions.setScreenResolution(screenResolution);
            return this;
        }

        @Nonnull
        @Override
        public Capabilities.Selenoid sessionTimeout(@Nonnull Duration duration) {
            selenoidOptions.setSessionTimeout(ExtensionCapabilities.golangDuration(duration));
            return this;
        }

        @Nonnull
        @Override
        public Capabilities.Selenoid s3KeyPattern(@Nonnull String pattern) {
            selenoidOptions.setS3KeyPattern(pattern);
            return this;
        }

        @Nonnull
        @Override
        public Capabilities.Selenoid timeZone(@Nonnull String timeZone) {
            selenoidOptions.setTimeZone(timeZone);
            return this;
        }

        @Nonnull
        @Override
        public Capabilities.Selenoid videoFrameRate(int frameRate) {
            selenoidOptions.setVideoFrameRate((long) frameRate);
            return this;
        }

        @Nonnull
        @Override
        public Capabilities.Selenoid videoName(@Nonnull String videoName) {
            selenoidOptions.setVideoName(videoName);
            return this;
        }

        @Nonnull
        @Override
        public Capabilities.Selenoid videoScreenSize(@Nonnull String videoScreenSize) {
            selenoidOptions.setVideoScreenSize(videoScreenSize);
            return this;
        }
    }

    static class MoonCapabilities extends ExtensionCapabilities implements Moon, Moon.MobileDevice {

        private final MoonOptions moonOptions;
        private final MoonMobileDevice moonMobileDevice;

        MoonCapabilities(Capabilities capabilities) {
            super(capabilities);
            this.moonOptions = new MoonOptions();
            this.moonMobileDevice = new MoonMobileDevice();
            capabilities.capability(JSON_PROPERTY_MOON_COLON_OPTIONS, moonOptions);
        }

        @Nonnull
        @Override
        public Moon enableVNC() {
            moonOptions.setEnableVNC(true);
            return this;
        }

        @Nonnull
        @Override
        public Moon environmentVariable(@Nonnull String key, @Nonnull String value) {
            moonOptions.addEnvItem(String.format("%s=%s", key, value));
            return this;
        }

        @Nonnull
        @Override
        public Moon logName(@Nonnull String name) {
            moonOptions.setLogName(name);
            return this;
        }

        @Nonnull
        @Override
        public Moon name(@Nonnull String name) {
            moonOptions.setName(name);
            return this;
        }

        @Nonnull
        @Override
        public Moon screenResolution(@Nonnull String screenResolution) {
            moonOptions.setScreenResolution(screenResolution);
            return this;
        }

        @Nonnull
        @Override
        public Moon sessionTimeout(@Nonnull Duration duration) {
            moonOptions.setSessionTimeout(ExtensionCapabilities.golangDuration(duration));
            return this;
        }

        @Nonnull
        @Override
        public Moon s3KeyPattern(@Nonnull String pattern) {
            moonOptions.setS3KeyPattern(pattern);
            return this;
        }

        @Nonnull
        @Override
        public Moon timeZone(@Nonnull String timeZone) {
            moonOptions.setTimeZone(timeZone);
            return this;
        }

        @Nonnull
        @Override
        public Moon videoFrameRate(int frameRate) {
            moonOptions.setVideoFrameRate((long) frameRate);
            return this;
        }

        @Nonnull
        @Override
        public Moon videoName(@Nonnull String videoName) {
            moonOptions.setVideoName(videoName);
            return this;
        }

        @Nonnull
        @Override
        public Moon videoScreenSize(@Nonnull String videoScreenSize) {
            moonOptions.setVideoScreenSize(videoScreenSize);
            return this;
        }

        @Nonnull
        @Override
        public MobileDevice mobileDevice() {
            moonOptions.setMobileDevice(moonMobileDevice);
            return this;
        }

        @Nonnull
        @Override
        public MobileDevice deviceName(@Nonnull String name) {
            moonMobileDevice.setDeviceName(name);
            return this;
        }

        @Nonnull
        @Override
        public MobileDevice landscape() {
            moonMobileDevice.setOrientation(LANDSCAPE);
            return this;
        }
    }

    static class SafariCapabilities extends ExtensionCapabilities implements Safari {

        SafariCapabilities(Capabilities capabilities) {
            super(capabilities);
            capabilities.browserName("safari");
        }

        @Nonnull
        @Override
        public Safari automaticInspection() {
            capabilities.capability(JSON_PROPERTY_SAFARI_COLON_AUTOMATIC_INSPECTION, true);
            return this;
        }

        @Nonnull
        @Override
        public Safari automaticProfiling() {
            capabilities.capability(JSON_PROPERTY_SAFARI_COLON_AUTOMATIC_PROFILING, true);
            return this;
        }

    }

}
