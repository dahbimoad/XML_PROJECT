package com.mouad.xml_project.utils;

public class FopConfigUtil {
    public static final String FOP_CONFIG =
        "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
        "<fop version=\"1.0\">\n" +
        "  <strict-configuration>true</strict-configuration>\n" +
        "  <base>.</base>\n" +
        "  <renderers>\n" +
        "    <renderer mime=\"application/pdf\">\n" +
        "      <fonts>\n" +
        "        <!-- Register all fonts found in this directory and sub directories -->\n" +
        "        <auto-detect/>\n" +
        "      </fonts>\n" +
        "      <filterList>\n" +
        "        <!-- Prevents borders from being drawn at the PDF boundaries -->\n" +
        "        <value>null</value>\n" +
        "      </filterList>\n" +
        "    </renderer>\n" +
        "  </renderers>\n" +
        "</fop>";

    private FopConfigUtil() {
        // Private constructor to prevent instantiation
    }
}