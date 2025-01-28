package com.mouad.xml_project.services;

import com.mouad.xml_project.utils.FopConfigUtil;
import org.apache.fop.apps.*;
import javax.xml.transform.*;
import javax.xml.transform.sax.SAXResult;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import java.io.*;
import java.util.Map;

public class DocumentGenerationService {

    /**
     * Ensures that the directory for the output file exists, creating it if necessary.
     *
     * @param filePath Path to the output file
     */
    private static void ensureDirectoryExists(String filePath) {
        File directory = new File(filePath).getParentFile();
        if (directory != null && !directory.exists()) {
            boolean created = directory.mkdirs();
            if (!created) {
                throw new RuntimeException("Failed to create directory: " + directory.getAbsolutePath());
            }
        }
    }

    /**
     * Generate PDF using FOP + XSL-FO from an XML and XSLT.
     *
     * @param inputXML   String path or URI to the input XML file
     * @param xsltPath   String path or URI to the XSLT file
     * @param outputPDF  Local path where PDF should be saved
     * @param parameters Optional XSLT parameters
     */
    public static void generatePdf(
        String inputXML,
        String xsltPath,
        String outputPDF,
        Map<String, String> parameters
    ) {
        try {
            // Ensure output directory exists
            ensureDirectoryExists(outputPDF);

            // 1) Set up the XSLT transformation
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            StreamSource xsltStreamSource = new StreamSource(xsltPath);
            Transformer transformer = transformerFactory.newTransformer(xsltStreamSource);

            // 2) Pass XSLT parameters if any
            if (parameters != null) {
                for (Map.Entry<String, String> entry : parameters.entrySet()) {
                    transformer.setParameter(entry.getKey(), entry.getValue());
                }
            }

            // 3) Set up FOP with config
            InputStream configSource = new ByteArrayInputStream(FopConfigUtil.FOP_CONFIG.getBytes());
            FopFactory fopFactory = FopFactory.newInstance(new File(".").toURI(), configSource);
            FOUserAgent foUserAgent = fopFactory.newFOUserAgent();

            // 4) Create the output stream for the resulting PDF
            try (OutputStream out = new FileOutputStream(outputPDF)) {
                Fop fop = fopFactory.newFop(MimeConstants.MIME_PDF, foUserAgent, out);

                // 5) Transform XML -> PDF
                StreamSource xmlSource = new StreamSource(inputXML);
                Result res = new SAXResult(fop.getDefaultHandler());
                transformer.transform(xmlSource, res);
            }

            System.out.println("PDF generated successfully at: " + outputPDF);

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to generate PDF: " + e.getMessage(), e);
        }
    }

    /**
     * Generate HTML from XML and XSLT.
     *
     * @param inputXML    String path/URI to the input XML
     * @param xsltPath    String path/URI to the XSLT
     * @param outputHTML  Local path to the output HTML file
     * @param parameters  Optional XSLT parameters
     */
    public static void generateHtml(
        String inputXML,
        String xsltPath,
        String outputHTML,
        Map<String, String> parameters
    ) throws IOException {
        try {
            // Ensure output directory exists
            ensureDirectoryExists(outputHTML);

            // Use the strings directly (in case they're "file:/..." URIs)
            StreamSource xmlInput = new StreamSource(inputXML);
            StreamSource xsltInput = new StreamSource(xsltPath);
            Result htmlOutput = new StreamResult(outputHTML);

            TransformerFactory factory = TransformerFactory.newInstance();
            Transformer transformer = factory.newTransformer(xsltInput);

            // Pass XSLT parameters if any
            if (parameters != null) {
                for (Map.Entry<String, String> entry : parameters.entrySet()) {
                    transformer.setParameter(entry.getKey(), entry.getValue());
                }
            }

            // Transform XML -> HTML
            transformer.transform(xmlInput, htmlOutput);
            System.out.println("HTML file generated at: " + outputHTML);

        } catch (TransformerException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to generate HTML: " + e.getMessage(), e);
        }
    }

    /**
     * Generate Excel-like output from XML using XSLT (.xls or .xlsx).
     *
     * @param inputXML   String path/URI to the XML
     * @param xsltPath   String path/URI to the XSLT
     * @param outputXLS  Local path to output the generated Excel
     * @param parameters Optional XSLT parameters
     */
    public static void generateExcel(
        String inputXML,
        String xsltPath,
        String outputXLS,
        Map<String, String> parameters
    ) throws IOException {
        // Ensure output directory exists
        ensureDirectoryExists(outputXLS);

        // 1) Create output stream for the generated .xls
        try (FileOutputStream excelOutput = new FileOutputStream(outputXLS)) {
            // 2) Set up the XSLT transformation
            TransformerFactory factory = TransformerFactory.newInstance();
            StreamSource xmlInput = new StreamSource(inputXML);
            StreamSource xsltInput = new StreamSource(xsltPath);
            Transformer transformer = factory.newTransformer(xsltInput);

            // 3) Pass XSLT parameters if any
            if (parameters != null) {
                for (Map.Entry<String, String> entry : parameters.entrySet()) {
                    transformer.setParameter(entry.getKey(), entry.getValue());
                }
            }

            // 4) Transform XML -> XLS
            transformer.transform(xmlInput, new StreamResult(excelOutput));
            System.out.println("Excel file generated at: " + outputXLS);

        } catch (TransformerException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to generate Excel file: " + e.getMessage(), e);
        }
    }
}