package com.mouad.xml_project.controllers;

import com.mouad.xml_project.services.DocumentGenerationService;
import com.mouad.xml_project.utils.AlertUtil;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class MainController {

    @FXML private TextField CarteCne;
    @FXML private TextField ReuCne;
    @FXML private TextField InscCne;
    @FXML private CheckBox pdfinsc;
    @FXML private CheckBox htmlinsc;
    @FXML private CheckBox pdfreu;
    @FXML private CheckBox htmlreu;
    @FXML private CheckBox pdfcarte;
    @FXML private CheckBox htmlcarte;
    @FXML private CheckBox tousmod;
    @FXML private CheckBox unseulmod;
    @FXML private TextField CodeModule;
    @FXML private CheckBox EDTHTMLChoice;
    @FXML private CheckBox EDTPDFChoice;
    @FXML private CheckBox EDTExcelChoice;
    @FXML private CheckBox AffichageNotes;
    @FXML private CheckBox StudentAffichage;
    @FXML private TextField AffichageCne;

    private static final String BASE_OUTPUT_DIR = "Output";
    private static final String XML_BASE_PATH = "/com/mouad/xml_project/xml";
    private static final String XSLT_BASE_PATH = "/com/mouad/xml_project/xslt";

    /**
     * Validates CNE input
     */
    private boolean validateCNE(String cne) {
        return cne != null && !cne.trim().isEmpty() && cne.matches("[A-Z0-9]+");
    }

    /**
     * Gets resource URL and checks for null
     */
    private URL getResourceURL(String path, String resourceType) {
        URL url = getClass().getResource(path);
        if (url == null) {
            throw new RuntimeException("Unable to find " + resourceType + " resource: " + path);
        }
        return url;
    }

    /**
     * Generic method to handle document generation
     */
    private void generateDocument(String xmlPath, String xsltPath, String outputPath,
                                Map<String, String> params, String type) {
        try {
            switch (type.toLowerCase()) {
                case "pdf":
                    DocumentGenerationService.generatePdf(xmlPath, xsltPath, outputPath, params);
                    break;
                case "html":
                    DocumentGenerationService.generateHtml(xmlPath, xsltPath, outputPath, params);
                    break;
                case "excel":
                    DocumentGenerationService.generateExcel(xmlPath, xsltPath, outputPath, params);
                    break;
                default:
                    throw new IllegalArgumentException("Unsupported document type: " + type);
            }
            AlertUtil.showInfoAlert("Success", "Document generated successfully at: " + outputPath);
        } catch (Exception e) {
            AlertUtil.showErrorAlert("Error", "Failed to generate " + type + " document: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // --------------------------------------
    // 1) Handle the generation of EDT
    // --------------------------------------
    @FXML
    public void HandleGeneration() {
        try {
            if (EDTHTMLChoice.isSelected()) {
                generateEDTasHTML();
            }
            if (EDTPDFChoice.isSelected()) {
                generateEDTasPDF();
            }
            if (EDTExcelChoice.isSelected()) {
                generateEDTasExcel();
            }
            if (!EDTHTMLChoice.isSelected() && !EDTPDFChoice.isSelected() && !EDTExcelChoice.isSelected()) {
                AlertUtil.showWarningAlert("Warning", "Please select at least one output format.");
            }
        } catch (Exception e) {
            AlertUtil.showErrorAlert("Error", "Failed to generate EDT: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void generateEDTasPDF() {
        URL xsltUrl = getResourceURL(XSLT_BASE_PATH + "/EDT.xslt", "XSLT");
        URL xmlUrl = getResourceURL(XML_BASE_PATH + "/EDT.xml", "XML");

        String outputPDF = BASE_OUTPUT_DIR + "/PDF-EDT/EDT.pdf";
        generateDocument(xmlUrl.toExternalForm(), xsltUrl.toExternalForm(), outputPDF, null, "pdf");
    }

    private void generateEDTasExcel() {
        URL xsltUrl = getResourceURL(XSLT_BASE_PATH + "/EDT_Excel.xslt", "XSLT");
        URL xmlUrl = getResourceURL(XML_BASE_PATH + "/EDT.xml", "XML");

        String outputXLS = BASE_OUTPUT_DIR + "/Excel-EDT/EDT.xls";
        generateDocument(xmlUrl.toExternalForm(), xsltUrl.toExternalForm(), outputXLS, null, "excel");
    }

    private void generateEDTasHTML() {
        URL xsltUrl = getResourceURL(XSLT_BASE_PATH + "/EDT.xslt", "XSLT");
        URL xmlUrl = getResourceURL(XML_BASE_PATH + "/EDT.xml", "XML");

        String outputHTML = BASE_OUTPUT_DIR + "/HTML-EDT/EDT.html";
        generateDocument(xmlUrl.toExternalForm(), xsltUrl.toExternalForm(), outputHTML, null, "html");
    }

    // --------------------------------------
    // 2) Handle Affichage (Notes, Student notes)
    // --------------------------------------
    @FXML
    private void HandleAffichage() {
        try {
            if (AffichageNotes.isSelected()) {
                handleAllNotesDisplay();
            }
            if (StudentAffichage.isSelected()) {
                handleSingleStudentDisplay();
            }
            if (!AffichageNotes.isSelected() && !StudentAffichage.isSelected()) {
                AlertUtil.showWarningAlert("Warning", "Please select a display option.");
            }
        } catch (Exception e) {
            AlertUtil.showErrorAlert("Error", "Failed to display notes: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void handleAllNotesDisplay() {
        URL xmlUrl = getResourceURL(XML_BASE_PATH + "/S3S4notessmall.xml", "XML");
        URL xsltUrl = getResourceURL(XSLT_BASE_PATH + "/AffichageParEtu.xslt", "XSLT");

        String outputHTML = BASE_OUTPUT_DIR + "/HTMLEtudiant/AffichageClasse.html";
        generateDocument(xmlUrl.toExternalForm(), xsltUrl.toExternalForm(), outputHTML, null, "html");
    }

    private void handleSingleStudentDisplay() {
        String cne = AffichageCne.getText();
        if (!validateCNE(cne)) {
            AlertUtil.showErrorAlert("Input Error", "Please enter a valid CNE.");
            return;
        }

        URL xmlUrl = getResourceURL(XML_BASE_PATH + "/S3S4notessmall.xml", "XML");
        URL xsltUrl = getResourceURL(XSLT_BASE_PATH + "/AffichageParEtu.xslt", "XSLT");

        Map<String, String> params = new HashMap<>();
        params.put("studentCNE", cne);

        String outputHTML = BASE_OUTPUT_DIR + "/HTMLEtudiant/" + cne + "Affichage.html";
        generateDocument(xmlUrl.toExternalForm(), xsltUrl.toExternalForm(), outputHTML, params, "html");
    }

    // --------------------------------------
    // 3) Generate Attestation de Réussite
    // --------------------------------------
    @FXML
    public void GenererReu(ActionEvent actionEvent) {
        String cne = ReuCne.getText();
        if (!validateCNE(cne)) {
            AlertUtil.showErrorAlert("Input Error", "Please enter a valid CNE.");
            return;
        }

        try {
            if (pdfreu.isSelected()) {
                URL xmlUrl = getResourceURL(XML_BASE_PATH + "/etudiants.xml", "XML");
                URL xsltUrl = getResourceURL(XSLT_BASE_PATH + "/Attestation_reu_pdf.xsl", "XSLT");

                Map<String, String> params = new HashMap<>();
                params.put("cne", cne);

                String outputPDF = BASE_OUTPUT_DIR + "/PDF-Reussite/" + cne + "AttReussite.pdf";
                generateDocument(xmlUrl.toExternalForm(), xsltUrl.toExternalForm(), outputPDF, params, "pdf");
            }

            if (htmlreu.isSelected()) {
                URL xmlUrl = getResourceURL(XML_BASE_PATH + "/etudiants.xml", "XML");
                URL xsltUrl = getResourceURL(XSLT_BASE_PATH + "/AttestationReus.xslt", "XSLT");

                Map<String, String> params = new HashMap<>();
                params.put("cne", cne);

                String outputHTML = BASE_OUTPUT_DIR + "/HTML-Reussite/" + cne + "AttReussite.html";
                generateDocument(xmlUrl.toExternalForm(), xsltUrl.toExternalForm(), outputHTML, params, "html");
            }

            if (!pdfreu.isSelected() && !htmlreu.isSelected()) {
                AlertUtil.showWarningAlert("Warning", "Please select at least one output format.");
            }
        } catch (Exception e) {
            AlertUtil.showErrorAlert("Error", "Failed to generate attestation de réussite: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // --------------------------------------
    // 4) Generate Attestation d'Inscription
    // --------------------------------------
    @FXML
    public void GenererInsc(ActionEvent actionEvent) {
        String cne = InscCne.getText();
        if (!validateCNE(cne)) {
            AlertUtil.showErrorAlert("Input Error", "Please enter a valid CNE.");
            return;
        }

        try {
            if (pdfinsc.isSelected()) {
                URL xmlUrl = getResourceURL(XML_BASE_PATH + "/etudiants.xml", "XML");
                URL xsltUrl = getResourceURL(XSLT_BASE_PATH + "/Attestation_ins_pdf.xsl", "XSLT");

                Map<String, String> params = new HashMap<>();
                params.put("cne", cne);

                String outputPDF = BASE_OUTPUT_DIR + "/PDF-Inscription/" + cne + "AttInscription.pdf";
                generateDocument(xmlUrl.toExternalForm(), xsltUrl.toExternalForm(), outputPDF, params, "pdf");
            }

            if (htmlinsc.isSelected()) {
                URL xmlUrl = getResourceURL(XML_BASE_PATH + "/etudiants.xml", "XML");
                URL xsltUrl = getResourceURL(XSLT_BASE_PATH + "/AttestationInsc.xslt", "XSLT");

                Map<String, String> params = new HashMap<>();
                params.put("cne", cne);

                String outputHTML = BASE_OUTPUT_DIR + "/HTML-Inscription/" + cne + "AttInscription.html";
                generateDocument(xmlUrl.toExternalForm(), xsltUrl.toExternalForm(), outputHTML, params, "html");
            }

            if (!pdfinsc.isSelected() && !htmlinsc.isSelected()) {
                AlertUtil.showWarningAlert("Warning", "Please select at least one output format.");
            }
        } catch (Exception e) {
            AlertUtil.showErrorAlert("Error", "Failed to generate attestation d'inscription: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // --------------------------------------
    // 5) Generate Carte Etudiant
    // --------------------------------------
    @FXML
    public void GenererCarte(ActionEvent actionEvent) {
        String cne = CarteCne.getText();
        if (!validateCNE(cne)) {
            AlertUtil.showErrorAlert("Input Error", "Please enter a valid CNE.");
            return;
        }

        try {
            if (pdfcarte.isSelected()) {
                URL xmlUrl = getResourceURL(XML_BASE_PATH + "/etudiants.xml", "XML");
                URL xsltUrl = getResourceURL(XSLT_BASE_PATH + "/Carte_Etudiant.xsl", "XSLT");

                Map<String, String> params = new HashMap<>();
                params.put("cne", cne);

                String outputPDF = BASE_OUTPUT_DIR + "/PDF-CarteEtu/" + cne + "CarteEtudiant.pdf";
                generateDocument(xmlUrl.toExternalForm(), xsltUrl.toExternalForm(), outputPDF, params, "pdf");
            }

            if (htmlcarte.isSelected()) {
                URL xmlUrl = getResourceURL(XML_BASE_PATH + "/etudiants.xml", "XML");
                URL xsltUrl = getResourceURL(XSLT_BASE_PATH + "/CarteEtudiantxslt.xslt", "XSLT");

                Map<String, String> params = new HashMap<>();
                params.put("cne", cne);

                String outputHTML = BASE_OUTPUT_DIR + "/HTML-CarteEtu/" + cne + "CarteEtudiant.html";
                generateDocument(xmlUrl.toExternalForm(), xsltUrl.toExternalForm(), outputHTML, params, "html");
            }

            if (!pdfcarte.isSelected() && !htmlcarte.isSelected()) {
                AlertUtil.showWarningAlert("Warning", "Please select at least one output format.");
            }
        } catch (Exception e) {
            AlertUtil.showErrorAlert("Error", "Failed to generate student card: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // --------------------------------------
    // 6) Affichage par module
    // --------------------------------------
    @FXML
    public void AffichageMod(ActionEvent actionEvent) {
        try {
            if (tousmod.isSelected()) {
                handleAllModulesDisplay();
            }

            if (unseulmod.isSelected()) {
                handleSingleModuleDisplay();
            }

            if (!tousmod.isSelected() && !unseulmod.isSelected()) {
                AlertUtil.showWarningAlert("Warning", "Please select a module display option.");
            }
        } catch (Exception e) {
            AlertUtil.showErrorAlert("Error", "Failed to display modules: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void handleAllModulesDisplay() {
        URL xmlUrl = getResourceURL(XML_BASE_PATH + "/Modules.xml", "XML");
        URL xsltUrl = getResourceURL(XSLT_BASE_PATH + "/AffichageParModule.xslt", "XSLT");

        String outputHTML = BASE_OUTPUT_DIR + "/HTMLEtudiant/AffichageModules.html";
        generateDocument(xmlUrl.toExternalForm(), xsltUrl.toExternalForm(), outputHTML, null, "html");
    }

    private void handleSingleModuleDisplay() {
        String code = CodeModule.getText();
        if (code == null || code.trim().isEmpty()) {
            AlertUtil.showErrorAlert("Input Error", "Please enter a valid module code.");
            return;
        }

        URL xmlUrl = getResourceURL(XML_BASE_PATH + "/Modules.xml", "XML");
        URL xsltUrl = getResourceURL(XSLT_BASE_PATH + "/AffichageParModule.xslt", "XSLT");

        Map<String, String> params = new HashMap<>();
        params.put("codeModu", code);

        String outputHTML = BASE_OUTPUT_DIR + "/HTMLEtudiant/" + code + "Affichage.html";
        generateDocument(xmlUrl.toExternalForm(), xsltUrl.toExternalForm(), outputHTML, params, "html");
    }
}