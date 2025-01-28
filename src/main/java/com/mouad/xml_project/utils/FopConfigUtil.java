package com.mouad.xml_project.utils;

public class FopConfigUtil {
    public static final String FOP_CONFIG =
        "<?xml version=\"1.0\" encoding=\"utf-8\" ?>\n" +
        "<fop>\n" +
        "  <renderers>\n" +
        "    <renderer mime=\"application/pdf\">\n" +
        "      <fonts>\n" +
        "        <auto-detect/>\n" +
        "      </fonts>\n" +
        "    </renderer>\n" +
        "  </renderers>\n" +
        "</fop>";

    private FopConfigUtil() {
    }
}