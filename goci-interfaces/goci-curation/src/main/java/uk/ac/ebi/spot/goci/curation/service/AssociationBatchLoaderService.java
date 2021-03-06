package uk.ac.ebi.spot.goci.curation.service;

import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import uk.ac.ebi.spot.goci.curation.model.SnpAssociationForm;

import java.io.File;
import java.util.Collection;


/**
 * @author emma
 *         <p/>
 *         This is a small program to upload a batch of SNPs from a .xlsx spreadsheet.
 *         Note that the spreadsheet must be of .xlsx format!
 *         <p/>
 *         Created from code originally written by Dani. Adapted to fit with new curation system.
 *         <p/>
 */

@Service
public class AssociationBatchLoaderService {

    public AssociationBatchLoaderService() {
    }

    // Returns an array list of new association forms, the controller will turn
    // these into associations and save
    public Collection<SnpAssociationForm> processData(String fileName) throws Exception {

        // Open and parse our spreadsheet file
        XSSFSheet sheet = null;
        OPCPackage pkg = OPCPackage.open(fileName);
        XSSFWorkbook current = new XSSFWorkbook(pkg);
        sheet = current.getSheetAt(0);
        AssociationSheetProcessor processor = null;
        try {
            processor = new AssociationSheetProcessor(sheet);
            Collection<SnpAssociationForm> associations = processor.getAllSnpAssociationForms();
            pkg.close();
            return associations;
        } catch (Exception e) {

            // TODO CREATE EXCEPTION IF THIS GOES WRONG
            e.printStackTrace();
            throw e;
        } finally {
            // Delete our file
            File fileToDelete = new File(fileName);
            fileToDelete.deleteOnExit();
        }
    }

}