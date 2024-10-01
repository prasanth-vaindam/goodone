package com.vaindam.goodone.service;


import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.vaindam.goodone.model.Question;
import com.vaindam.goodone.repository.QuestionRepository;





@Service
public class ExcelQuestionExtractor {

    @Autowired
    private QuestionRepository questionRepository;

    public List<Question> extractAndSaveQuestionsFromExcel(MultipartFile file) throws IOException {
        List<Question> questions = new ArrayList<>();

        try (InputStream is = file.getInputStream();
             Workbook workbook = new XSSFWorkbook(is)) {
            Sheet sheet = workbook.getSheetAt(0);
            Iterator<Row> rows = sheet.iterator();

            // Skip header row
            if (rows.hasNext()) {
                rows.next();
            }

            while (rows.hasNext()) {
                Row currentRow = rows.next();
                Question question = new Question();

                question.setQuestionText(getCellValueAsString(currentRow.getCell(0)));
                question.setOptionA(getCellValueAsString(currentRow.getCell(1)));
                question.setOptionB(getCellValueAsString(currentRow.getCell(2)));
                question.setOptionC(getCellValueAsString(currentRow.getCell(3)));
                question.setOptionD(getCellValueAsString(currentRow.getCell(4)));
                question.setCorrectAnswer(getCellValueAsString(currentRow.getCell(5)));

                questions.add(question);
            }
        }

        // Save all extracted questions to MongoDB
        return questionRepository.saveAll(questions);
    }

    private String getCellValueAsString(Cell cell) {
        if (cell == null) {
            return "";
        }
        switch (cell.getCellType()) {
            case STRING:
                return cell.getStringCellValue();
            case NUMERIC:
                return String.valueOf(cell.getNumericCellValue());
            case BOOLEAN:
                return String.valueOf(cell.getBooleanCellValue());
            default:
                return "";
        }
    }
}
