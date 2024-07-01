package com.eric.quiz.controller;

import com.eric.quiz.model.Question;
import com.eric.quiz.service.IQuestionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/quizzes")
public class QuestionController {
  private final IQuestionService iQuestionService;

  @PostMapping("/create-new-question")
  public ResponseEntity<Question> createQuestion(@Valid @RequestBody Question question){
    Question createdQuestion = iQuestionService.createQuestion(question);
    return ResponseEntity.status(HttpStatus.CREATED).body(createdQuestion);
  }
  @GetMapping("/all-questions")
  public ResponseEntity<List<Question>> getAllQuestions(){
    List<Question> questions = iQuestionService.getAllQuestions();
    return ResponseEntity.ok(questions);
  }

  @GetMapping("/question/{id}")
  public ResponseEntity<Question> getQuestionById(@PathVariable Long id) throws ChangeSetPersister.NotFoundException {
    Optional<Question> theQuestion = iQuestionService.getQuestionById(id);
    if (theQuestion.isPresent()){
      return ResponseEntity.ok(theQuestion.get());
    }else {
      throw new ChangeSetPersister.NotFoundException();
    }
  }
  @PutMapping("/question/{id}/update")
  public ResponseEntity<Question> updateQuestion(@PathVariable Long id, @RequestBody Question question) throws ChangeSetPersister.NotFoundException {
    Question upDatedQuestion = iQuestionService.updateQuestion(id,question);
    return ResponseEntity.ok(upDatedQuestion);
  }
  @DeleteMapping("/question/{id}/delete")
  public ResponseEntity<Void> deleteQuestion(@PathVariable Long id){
    iQuestionService.deleteQuestion(id);
    return ResponseEntity.noContent().build();
  }
  @GetMapping("/subjects")
  public ResponseEntity<List<String>> getAllSubjects(){
    List<String> subjects = iQuestionService.getAllSubjects();
    return ResponseEntity.ok(subjects);
  }
  @GetMapping("/quiz/fetch-questions-for-user")
  public ResponseEntity<List<Question>> getQuestionsForUser(@RequestParam Integer numOfQuestions,@RequestParam String subject){
    List<Question> allQuestions = iQuestionService.getQuestionsForUser(numOfQuestions, subject);
    List<Question> mutableQuestions = new ArrayList<>(allQuestions);
    Collections.shuffle(mutableQuestions);

    int availableQuestions = Math.min(numOfQuestions, mutableQuestions.size());
    List<Question> randomQuestions = mutableQuestions.subList(0, availableQuestions);
    return ResponseEntity.ok(randomQuestions);
  }

}
