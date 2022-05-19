package com.aerokube.lightning;

public enum Image {
    CHROME("browsers/chrome:101.0"),
    EDGE("browsers/edge:101.0"),
    FIREFOX("browsers/firefox:100.0"),
    OPERA("browsers/opera:86.0"),
    SAFARI("browsers/safari:15.0");

    private final String ref;

    Image(String ref) {
        this.ref = ref;
    }

    public String getRef() {
        return ref;
    }
}
