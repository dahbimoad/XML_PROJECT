package com.mouad.xml_project.services;


import com.mouad.xml_project.utils.FopConfigUtil;
import org.apache.fop.apps.FOUserAgent;
import org.apache.fop.apps.Fop;
import org.apache.fop.apps.FopFactory;
import org.apache.fop.apps.MimeConstants;
import javax.xml.transform.*;
import javax.xml.transform.sax.SAXResult;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import java.io.*;
import java.util.Map;

public class DocumentGenerationService {

    /**
     * Generate PDF using FOP + XSL-FO from an XML and XSLT.
     *
     * @param inputXML   Path to the input XML file
     * @param xsltPath   Path to the XSLT file
     * @param outputPDF  Path to the output PDF file
     * @param parameters Optional parameters to pass to the XSLT
     */
    public static void generatePdf(
        String inputXML,
        String xsltPath,
        String outputPDF,
        Map<String, String> parameters
    ) {
        try {
            // Set up the XSLT transformation
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            StreamSource xsltStreamSource = new StreamSource(new File(xsltPath));
            Transformer transformer = transformerFactory.newTransformer(xsltStreamSource);

            // Pass parameters to the XSLT if provided
            if (parameters != null) {
                for (Map.Entry<String, String> entry : parameters.entrySet()) {
                    transformer.setParameter(entry.getKey(), entry.getValue());
                }
            }

            // Set up the FOP processor with custom config
            InputStream configSource =
                new ByteArrayInputStream(FopConfigUtil.FOP_CONFIG.getBytes());
            FopFactory fopFactory =
                FopFactory.newInstance(new File(".").toURI(), configSource);

            FOUserAgent foUserAgent = fopFactory.newFOUserAgent();

            // Set up the output stream for the PDF
            try (OutputStream out = new FileOutputStream(outputPDF)) {
                Fop fop = fopFactory.newFop(MimeConstants.MIME_PDF, foUserAgent, out);

                // Set up the XSL-FO transformation
                Result res = new SAXResult(fop.getDefaultHandler());

                // Perform the transformation
                transformer.transform(new StreamSource(new File(inputXML)), res);
            }
            System.out.println("PDF generated successfully at: " + outputPDF);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Generate HTML from XML and XSLT.
     *
     * @param inputXML    Path to the input XML file
     * @param xsltPath    Path to the XSLT file
     * @param outputHTML  Path to the output HTML file
     * @param parameters  Optional parameters for the XSLT
     */
    public static void generateHtml(
        String inputXML,
        String xsltPath,
        String outputHTML,
        Map<String, String> parameters
    ) throws IOException {
        try {
            Source xmlInput = new StreamSource(inputXML);
            Source xslt = new StreamSource(xsltPath);
            Result htmlOutput = new StreamResult(outputHTML);

            TransformerFactory factory = TransformerFactory.newInstance();
            Transformer transformer = factory.newTransformer(xslt);

            if (parameters != null) {
                for (Map.Entry<String, String> entry : parameters.entrySet()) {
                    transformer.setParameter(entry.getKey(), entry.getValue());
                }
            }

            transformer.transform(xmlInput, htmlOutput);
            System.out.println("HTML file generated at: " + outputHTML);

        } catch (TransformerException e) {
            e.printStackTrace();
        }
    }

    /**
     * Generate Excel-like output from XML using XSLT (typically .xls).
     *
     * @param inputXML   Path to XML
     * @param xsltPath   Path to XSLT
     * @param outputXLS  Path to output XLS
     * @param parameters Optional parameters
     */
    public static void generateExcel(
        String inputXML,
        String xsltPath,
        String outputXLS,
        Map<String, String> parameters
    ) throws IOException {
        try (FileOutputStream excelOutput = new FileOutputStream(new File(outputXLS))) {
            Source xmlInput = new StreamSource(inputXML);
            Source xslt = new StreamSource(xsltPath);

            TransformerFactory factory = TransformerFactory.newInstance();
            Transformer transformer = factory.newTransformer(xslt);

            // Set parameters if any
            if (parameters != null) {
                for (Map.Entry<String, String> entry : parameters.entrySet()) {
                    transformer.setParameter(entry.getKey(), entry.getValue());
                }
            }

            transformer.transform(xmlInput, new StreamResult(excelOutput));
            System.out.println("Excel file generated at: " + outputXLS);

        } catch (TransformerException e) {
            e.printStackTrace();
        }
    }
}
