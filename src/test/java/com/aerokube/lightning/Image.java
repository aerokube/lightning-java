package com.aerokube.lightning;

public enum Image {
    CHROME("browsers/chrome:94.0"),
    EDGE("browsers/edge:95.0"),
    FIREFOX("browsers/firefox:93.0"),
    OPERA("browsers/opera:80.0"),
    SAFARI("browsers/safari:14.0");

    private final String ref;

    Image(String ref) {
        this.ref = ref;
    }

    public String getRef() {
        return ref;
    }
}
