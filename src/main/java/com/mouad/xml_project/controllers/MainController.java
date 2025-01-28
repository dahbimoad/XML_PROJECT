package com.mouad.xml_project.controllers;

import com.mouad.xml_project.services.DocumentGenerationService;
import com.mouad.xml_project.utils.AlertUtil;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import java.io.IOException;
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

    // 1) Handle the generation of ED (HTML, PDF, Excel)
    @FXML
    public void HandleGeneration() {
        if (EDTHTMLChoice.isSelected()) {
            AlertUtil.showInfoAlert("HTML Generation", "HTML generation requested.");
            generateEDTasHTML();
        }
        if (EDTPDFChoice.isSelected()) {
            AlertUtil.showInfoAlert("PDF Generation", "PDF generation requested.");
            generateEDTasPDF();
        }
        if (EDTExcelChoice.isSelected()) {
            AlertUtil.showInfoAlert("Excel Generation", "Excel generation requested.");
            generateEDTasExcel();
        }
    }

    private void generateEDTasPDF() {
        AlertUtil.showInfoAlert("PDF Generation", "EDT demandée");
        // Example usage
        String inputXML = "../Xsl/EDT.xml";
        String xsltFile = "../Xsl/EDT.xsl";
        String outputPDF = "../Output/PDF-EDT/EDT.pdf";

        DocumentGenerationService.generatePdf(inputXML, xsltFile, outputPDF, null);
    }

    private void generateEDTasExcel() {
        AlertUtil.showInfoAlert("Excel Generation", "EDT demandée (Excel).");
        String inputXML = "../Xslt/EDT.xml";
        String xsltFile = "../Xslt/EDT_Excel.xslt";
        String outputXLS = "../Output/Excel-EDT/EDT.xls";

        try {
            DocumentGenerationService.generateExcel(inputXML, xsltFile, outputXLS, null);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void generateEDTasHTML() {
        AlertUtil.showInfoAlert("HTML Generation", "EDT demandée (HTML).");
        String inputXML = "../Xslt/EDT.xml";
        String xsltFile = "../Xslt/EDT.xslt";
        String outputHTML = "../Output/HTML-EDT/EDT.html";

        try {
            DocumentGenerationService.generateHtml(inputXML, xsltFile, outputHTML, null);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    // 2) Handle Affichage (Notes, Student notes)
    @FXML
    private void HandleAffichage() {
        if (AffichageNotes.isSelected()) {
            AlertUtil.showInfoAlert("Affichage Generation", "Affichage des notes demandé.");
            try {
                String inputXML = "../Xslt/S3S4notessmall.xml";
                String xsltFile = "../Xslt/AffichageParEtu.xslt";
                String outputHTML = "../Output/HTMLEtudiant/AffichageClasse.html";

                DocumentGenerationService.generateHtml(inputXML, xsltFile, outputHTML, null);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (StudentAffichage.isSelected()) {
            String cne = AffichageCne.getText();
            AlertUtil.showInfoAlert("Affichage Generation", "Affichage des notes pour le CNE = " + cne);
            try {
                String inputXML = "../Xslt/S3S4notessmall.xml";
                String xsltFile = "../Xslt/AffichageParEtu.xslt";
                String outputHTML = "../Output/HTMLEtudiant/" + cne + "Affichage.html";

                // Pass the cne as a parameter to the XSLT
                Map<String, String> params = new HashMap<>();
                params.put("studentCNE", cne);

                DocumentGenerationService.generateHtml(inputXML, xsltFile, outputHTML, params);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    // 3) Generate Attestation de Réussite
    @FXML
    public void GenererReu(ActionEvent actionEvent) {
        String cne = ReuCne.getText();

        if (pdfreu.isSelected()) {
            AlertUtil.showInfoAlert("PDF Generation", "Attestation de Réussite demandée (PDF).");
            String inputXML = "../Xsl/etudiants.xml";
            String xsltFile = "../Xsl/Attestation_reu_pdf.xsl";
            String outputPDF = "../Output/PDF-Reussite/" + cne + "AttReussite.pdf";

            Map<String, String> params = new HashMap<>();
            params.put("cne", cne);

            DocumentGenerationService.generatePdf(inputXML, xsltFile, outputPDF, params);
        }

        if (htmlreu.isSelected()) {
            AlertUtil.showInfoAlert("HTML Generation", "Attestation de Réussite demandée (HTML).");
            String inputXML = "../Xslt/etudiants.xml";
            String xsltFile = "../Xslt/AttestationReus.xslt";
            String outputHTML = "../Output/HTML-Reussite/" + cne + "AttReussite.html";

            Map<String, String> params = new HashMap<>();
            params.put("cne", cne);

            try {
                DocumentGenerationService.generateHtml(inputXML, xsltFile, outputHTML, params);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    // 4) Generate Attestation d'Inscription
    @FXML
    public void GenererInsc(ActionEvent actionEvent) {
        String cne = InscCne.getText();

        if (pdfinsc.isSelected()) {
            AlertUtil.showInfoAlert("PDF Generation", "Attestation d'Inscription demandée (PDF).");
            String inputXML = "../Xsl/etudiants.xml";
            String xsltFile = "../Xsl/Attestation_ins_pdf.xsl";
            String outputPDF = "../Output/PDF-Inscription/" + cne + "AttInscription.pdf";

            Map<String, String> params = new HashMap<>();
            params.put("cne", cne);

            DocumentGenerationService.generatePdf(inputXML, xsltFile, outputPDF, params);
        }

        if (htmlinsc.isSelected()) {
            AlertUtil.showInfoAlert("HTML Generation", "Attestation d'Inscription demandée (HTML).");
            String inputXML = "../Xslt/etudiants.xml";
            String xsltFile = "../Xslt/AttestationInsc.xslt";
            String outputHTML = "../Output/HTML-Inscription/" + cne + "AttInscription.html";

            Map<String, String> params = new HashMap<>();
            params.put("cne", cne);

            try {
                DocumentGenerationService.generateHtml(inputXML, xsltFile, outputHTML, params);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    // 5) Generate Carte Etudiant
    @FXML
    public void GenererCarte(ActionEvent actionEvent) {
        String cne = CarteCne.getText();

        if (pdfcarte.isSelected()) {
            AlertUtil.showInfoAlert("PDF Generation", "Carte d'étudiant demandée (PDF).");
            String inputXML = "../Xsl/etudiants.xml";
            String xsltFile = "../Xsl/Carte_Etudiant.xsl";
            String outputPDF = "../Output/PDF-CarteEtu/" + cne + "CarteEtudiant.pdf";

            Map<String, String> params = new HashMap<>();
            params.put("cne", cne);

            DocumentGenerationService.generatePdf(inputXML, xsltFile, outputPDF, params);
        }

        if (htmlcarte.isSelected()) {
            AlertUtil.showInfoAlert("HTML Generation", "Carte d'étudiant demandée (HTML).");
            String inputXML = "../Xslt/etudiants.xml";
            String xsltFile = "../Xslt/CarteEtudiantxslt.xslt";
            String outputHTML = "../Output/HTML-CarteEtu/" + cne + "CarteEtudiant.html";

            Map<String, String> params = new HashMap<>();
            params.put("cne", cne);

            try {
                DocumentGenerationService.generateHtml(inputXML, xsltFile, outputHTML, params);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    // 6) Affichage par module
    @FXML
    public void AffichageMod(ActionEvent actionEvent) {
        if (tousmod.isSelected()) {
            AlertUtil.showInfoAlert("Affichage Generation", "Affichage des notes par module (tous) demandé.");
            String inputXML = "../Xslt/Modules.xml";
            String xsltFile = "../Xslt/AffichageParModule.xslt";
            String outputHTML = "../Output/HTMLEtudiant/AffichageModules.html";

            try {
                DocumentGenerationService.generateHtml(inputXML, xsltFile, outputHTML, null);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if (unseulmod.isSelected()) {
            String code = CodeModule.getText();
            AlertUtil.showInfoAlert("Affichage Generation", "Affichage des notes pour module = " + code);
            String inputXML = "../Xslt/Modules.xml";
            String xsltFile = "../Xslt/AffichageParModule.xslt";
            String outputHTML = "../Output/HTMLEtudiant/" + code + "Affichage.html";

            Map<String, String> params = new HashMap<>();
            params.put("codeModu", code);

            try {
                DocumentGenerationService.generateHtml(inputXML, xsltFile, outputHTML, params);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}