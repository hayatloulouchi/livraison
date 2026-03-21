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

                l.setTiers(getCell(row, 0));
                l.setNumeroBc(getCell(row, 1));
                l.setClient(getCell(row, 2));
                l.setTelephone(getCell(row, 3));
                l.setLivreur(getCell(row, 4));
                l.setVille(getCell(row, 5));

                // 📅 DATE
                Cell dateCell = row.getCell(6);
                if (dateCell != null && dateCell.getCellType() == CellType.NUMERIC) {

                    LocalDate date = dateCell.getDateCellValue()
                            .toInstant()
                            .atZone(ZoneId.systemDefault())
                            .toLocalDate();

                    l.setDateLivraison(date);
                }

                l.setStatut("EN_ATTENTE");

                livraisons.add(l);
            }

            workbook.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return livraisons;
    }

    private static String getCell(Row row, int index) {
        Cell cell = row.getCell(index);
        if (cell == null) return "";
        return cell.toString();
    }
}