package quiz_peach.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import quiz_peach.domain.dto.*;
import quiz_peach.domain.enumeration.AnsweredStatus;
import quiz_peach.domain.enumeration.DifficultyLevel;
import quiz_peach.service.QuestionService;

import java.util.List;

@RestController
@RequestMapping("/api/question")
@RequiredArgsConstructor
public class QuestionController {
    private final QuestionService questionService;

    @PostMapping
    public ResponseEntity<Void> createQuestion(@AuthenticationPrincipal CurrentUser user,
                                               @RequestBody @Valid CreateQuestionDTO request) {
        questionService.createQuestion(request, user);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/answer")
    public ResponseEntity<AnswerQuestionResponseDTO> submitAnswer(@AuthenticationPrincipal CurrentUser user,
                                                                  @RequestBody AnswerQuestionRequestDTO request) {
        return ResponseEntity.ok(questionService.submitAnswer(request, user));
    }

    @GetMapping("/{id}/details")
    public ResponseEntity<QuestionDTO> getQuestionDetails(@AuthenticationPrincipal CurrentUser user,
                                                          @PathVariable Long id) {
        return ResponseEntity.ok(questionService.getQuestionDetails(id, user));
    }

    @GetMapping("/{id}/similars")
    public ResponseEntity<List<QuestionDTO>> getSimilarQuestions(@PathVariable Long id) {
        return ResponseEntity.ok(questionService.getSimilarQuestions(id));
    }

    @GetMapping
    public ResponseEntity<List<FilteredQuestionDTO>> getFilteredQuestions(@RequestParam(required = false) String name,
                                                                          @RequestParam(required = false) DifficultyLevel level,
                                                                          @RequestParam(required = false) AnsweredStatus answeredStatus,
                                                                          @AuthenticationPrincipal CurrentUser user) {
        return ResponseEntity.ok(questionService.getFilteredQuestions(name, level, answeredStatus, user));
    }

    @GetMapping("/followed/questions")
    public ResponseEntity<List<QuestionDTO>> getFollowedUsersQuestions(
            @AuthenticationPrincipal CurrentUser user) {
        return ResponseEntity.ok(questionService.getFollowedUsersQuestions(user));
    }

}