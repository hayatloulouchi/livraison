package com.livraison.livraison.service;

import com.livraison.livraison.model.Livraison;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.InputStream;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

public class ExcelImportService {

    public static List<Livraison> importExcel(InputStream is) throws Exception {

        Workbook workbook = new XSSFWorkbook(is);
        Sheet sheet = workbook.getSheetAt(0);

        List<Livraison> livraisons = new ArrayList<>();

        DataFormatter formatter = new DataFormatter();

        for (Row row : sheet) {

            // ignorer la première ligne (entêtes)
            if (row.getRowNum() == 0) continue;

            Livraison l = new Livraison();

l.setTiers(formatter.formatCellValue(row.getCell(0)));
l.setNumeroBc(formatter.formatCellValue(row.getCell(1)));
l.setClient(formatter.formatCellValue(row.getCell(2)));
l.setTelephone(formatter.formatCellValue(row.getCell(3)));
l.setLivreur(formatter.formatCellValue(row.getCell(4)));
l.setVille(formatter.formatCellValue(row.getCell(5)));

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

        return livraisons;
    }
}