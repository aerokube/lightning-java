package com.aerokube.lightning;

public enum Image {
    CHROME("browsers/chrome:97.0"),
    EDGE("browsers/edge:97.0"),
    FIREFOX("browsers/firefox:96.0"),
    OPERA("browsers/opera:82.0"),
    SAFARI("browsers/safari:15.0");

    private final String ref;

    Image(String ref) {
        this.ref = ref;
    }

    public String getRef() {
        return ref;
    }
}
