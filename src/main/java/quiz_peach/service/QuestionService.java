package quiz_peach.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import quiz_peach.domain.dto.*;
import quiz_peach.domain.entities.AnsweredQuestionUser;
import quiz_peach.domain.entities.Question;
import quiz_peach.domain.entities.Tag;
import quiz_peach.domain.entities.User;
import quiz_peach.domain.enumeration.AnsweredStatus;
import quiz_peach.repository.AnsweredQuestionUserRepository;
import quiz_peach.repository.QuestionRepository;
import quiz_peach.repository.TagRepository;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class QuestionService {
    private final QuestionRepository questionRepository;
    private final TagRepository tagRepository;
    private final AnsweredQuestionUserRepository answeredQuestionUserRepository;

    @Transactional
    public void createQuestion(CreateQuestionDTO dto, User user) {
        Tag tag = tagRepository.findByName(dto.tagName())
                               .orElseThrow(() -> new RuntimeException("Tag not found"));

        Question question = Question.builder()
                                    .creator(user)
                                    .name(dto.name())
                                    .question(dto.question())
                                    .option1(dto.option1())
                                    .option2(dto.option2())
                                    .option3(dto.option3())
                                    .option4(dto.option4())
                                    .correctOption(dto.correctOption())
                                    .level(dto.level())
                                    .tag(tag)
                                    .build();

        tag.incrementQuestionNumber();
        tagRepository.save(tag);

        if (dto.relatedIds() != null && !dto.relatedIds().isEmpty()) {
            List<Question> allById = questionRepository.findAllById(dto.relatedIds());
            question.setRelatedQuestions(allById);
        }
        questionRepository.save(question);
    }

    public List<FilteredQuestionDTO> getFilteredQuestions(String name, String level, AnsweredStatus answeredStatus, User user) {
        List<Question> questions = questionRepository.findByFilters(name, level);
        return questions.stream()
                        .filter(q -> answeredStatus == null || filterByAnsweredStatus(q, user.getId(), answeredStatus))
                        .map(q -> new FilteredQuestionDTO(q.getId(), q.getName(), q.getLevel(), q.getTag().getName()))
                        .collect(Collectors.toList());
    }

    private boolean filterByAnsweredStatus(Question question, Long userId, AnsweredStatus answeredStatus) {
        return answeredQuestionUserRepository.findByQuestionIdAndUserId(question.getId(), userId)
                                             .map(a -> a.getAnsweredStatus() == answeredStatus)
                                             .orElse(false);
    }


    public QuestionDTO getQuestionDetails(Long id, User user) {
        Question question = questionRepository.findById(id)
                                              .orElseThrow(() -> new RuntimeException("Question not found"));

        boolean answered = answeredQuestionUserRepository.existsByUserIdAndQuestionId(user.getId(), id);

        return getQuestionDTO(question, answered);
    }

    @Transactional
    public AnswerQuestionResponseDTO submitAnswer(AnswerQuestionRequestDTO dto, User user) {
        Question question = questionRepository.findById(dto.questionId())
                                              .orElseThrow(() -> new RuntimeException("Question not found"));

        if (dto.option() > 4 || dto.option() < 1) {
            throw new RuntimeException("Option must be an integer between 1 and 4.");
        }
        boolean isCorrect = Objects.equals(question.getCorrectOption(), dto.option());
        AnsweredStatus answeredStatus = isCorrect ? AnsweredStatus.CORRECT : AnsweredStatus.INCORRECT;

        if (answeredQuestionUserRepository.existsByUserIdAndQuestionId(user.getId(), dto.questionId())) {
            throw new RuntimeException("You have already answered this question.");
        }

        AnsweredQuestionUser answeredQuestionUser = new AnsweredQuestionUser(question, user, answeredStatus);
        answeredQuestionUserRepository.save(answeredQuestionUser);

        if (isCorrect) {
            question.incrementCorrectAnswerCount();
        }
        question.incrementAnswerCount();

        return new AnswerQuestionResponseDTO(isCorrect, isCorrect ? "Correct answer!" : "Incorrect answer.");
    }

    public List<QuestionDTO> getSimilarQuestions(Long id) {
        Question question = questionRepository.findById(id)
                                              .orElseThrow(() -> new RuntimeException("Question not found"));
        return questionRepository.findByCreator_Id(question.getCreator().getId())
                                 .stream()
                                 .map(q -> getQuestionDTO(q, null))
                                 .toList();
    }

    private QuestionDTO getQuestionDTO(Question question, Boolean answered) {
        return new QuestionDTO(
                question.getId(),
                question.getName(),
                question.getQuestion(),
                question.getOption1(),
                question.getOption2(),
                question.getOption3(),
                question.getOption4(),
                question.getLevel(),
                question.getAnswerCount(),
                question.getCorrectAnswerCount(),
                question.getTag().getName(),
                answered
        );
    }
}