package com.livraison.livraison.service;

import com.livraison.livraison.model.Livraison;
import org.apache.poi.ss.usermodel.*;

import java.io.InputStream;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

public class ExcelImportService {

    public static List<Livraison> importExcel(InputStream is) {

        List<Livraison> livraisons = new ArrayList<>();

        try {
            Workbook workbook = WorkbookFactory.create(is);
            Sheet sheet = workbook.getSheetAt(0);

            for (int i = 1; i <= sheet.getLastRowNum(); i++) {

                Row row = sheet.getRow(i);
                if (row == null) continue;

                Livraison l = new Livraison();

                // ⚠️ IMPORTANT : on lit TOUT en texte (même chiffres)
                l.setTiers(getCellSafe(row, 0));
                l.setNumeroBc(getCellSafe(row, 1));
                l.setClient(getCellSafe(row, 2));
                l.setTelephone(getCellSafe(row, 3));
                l.setLivreur(getCellSafe(row, 4));
                l.setVille(getCellSafe(row, 5));

                // 📅 DATE (simple et robuste)
                l.setDateLivraison(LocalDate.now());

                l.setStatut("EN_ATTENTE");

                livraisons.add(l);
            }

            workbook.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        System.out.println("NB lignes Excel = " + livraisons.size());

        return livraisons;
    }

    // 🔥 méthode robuste
    private static String getCell(Row row, int index) {
    Cell cell = row.getCell(index);
    if (cell == null) return "";

    switch (cell.getCellType()) {
        case STRING:
            return cell.getStringCellValue();

        case NUMERIC:
            if (DateUtil.isCellDateFormatted(cell)) {
                return cell.getDateCellValue().toString();
            } else {
                return String.valueOf((long) cell.getNumericCellValue());
            }

        case BOOLEAN:
            return String.valueOf(cell.getBooleanCellValue());

        default:
            return "";
    }
}

    // 🔥 méthode encore plus robuste (ne plante pas si la ligne est vide)
    private static String getCellSafe(Row row, int index) {
        try {
            return getCell(row, index);
        } catch (Exception e) {
            return "";
        }
    }
}                                       
