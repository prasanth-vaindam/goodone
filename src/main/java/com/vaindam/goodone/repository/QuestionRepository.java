package com.vaindam.goodone.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.vaindam.goodone.model.Question;





public interface QuestionRepository extends MongoRepository<Question, String> {
}
