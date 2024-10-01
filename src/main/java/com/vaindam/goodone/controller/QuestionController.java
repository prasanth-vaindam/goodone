package com.vaindam.goodone.controller;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;



import com.vaindam.goodone.model.Question;
import com.vaindam.goodone.service.ExcelQuestionExtractor;



@RestController
@RequestMapping("/api/questions")
public class QuestionController {

    
    @Autowired
    private ExcelQuestionExtractor excelQuestionExtractor;

    @PostMapping("/upload")
    public ResponseEntity<List<Question>> uploadQuestions(@RequestParam("file") MultipartFile file) {
        try {
            List<Question> savedQuestions = excelQuestionExtractor.extractAndSaveQuestionsFromExcel(file);
            return ResponseEntity.ok(savedQuestions);
        } catch (IOException e) {
            return ResponseEntity.badRequest().build();
        }
    }
}