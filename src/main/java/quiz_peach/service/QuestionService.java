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
import quiz_peach.domain.enumeration.DifficultyLevel;
import quiz_peach.exceptions.InputInvalidException;
import quiz_peach.exceptions.ResourceNotFoundException;
import quiz_peach.repository.AnsweredQuestionUserRepository;
import quiz_peach.repository.QuestionRepository;
import quiz_peach.repository.TagRepository;
import quiz_peach.repository.UserRepository;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class QuestionService {
    private final QuestionRepository questionRepository;
    private final TagRepository tagRepository;
    private final AnsweredQuestionUserRepository answeredQuestionUserRepository;
    private final UserRepository userRepository;

    @Transactional
    public void createQuestion(CreateQuestionDTO dto, CurrentUser user) {
        Tag tag = tagRepository.findByName(dto.tagName())
                .orElseThrow(() -> new ResourceNotFoundException("Tag not found"));

        Question question = Question.builder()
                .creator(user.getUser())
                .name(dto.name())
                .question(dto.question())
                .option1(dto.option1())
                .option2(dto.option2())
                .option3(dto.option3())
                .option4(dto.option4())
                .correctOption(dto.correctOption())
                .level(DifficultyLevel.getDifficultyLevelById(dto.level()))
                .answerCount(0)
                .correctAnswerCount(0)
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

    public List<FilteredQuestionDTO> getFilteredQuestions(String name, DifficultyLevel level, AnsweredStatus answeredStatus, CurrentUser user) {
        List<Question> questions = questionRepository.findByFilters(name, level);
        return questions.stream()
                .filter(q -> answeredStatus == null || filterByAnsweredStatus(q, user.getUser().getId(), answeredStatus))
                .map(q -> new FilteredQuestionDTO(q.getId(), q.getName(), q.getLevel().getId(), q.getTag().getName()))
                .collect(Collectors.toList());
    }

    public QuestionDTO getQuestionDetails(Long id, CurrentUser user) {
        Question question = questionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Question not found"));

        boolean answered = answeredQuestionUserRepository.existsByUserIdAndQuestionId(user.getUser().getId(), id);

        return getQuestionDTO(question, answered);
    }

    @Transactional
    public AnswerQuestionResponseDTO submitAnswer(AnswerQuestionRequestDTO dto, CurrentUser user) {
        Question question = questionRepository.findById(dto.questionId())
                .orElseThrow(() -> new ResourceNotFoundException("Question not found"));

        if (dto.option() > 4 || dto.option() < 1) {
            throw new InputInvalidException("Option must be an integer between 1 and 4.");
        }
        boolean isCorrect = Objects.equals(question.getCorrectOption(), dto.option());
        AnsweredStatus answeredStatus = isCorrect ? AnsweredStatus.CORRECT : AnsweredStatus.INCORRECT;

        User currentUser = user.getUser();
        if (answeredQuestionUserRepository.existsByUserIdAndQuestionId(currentUser.getId(), dto.questionId())) {
            throw new InputInvalidException("You have already answered this question.");
        }

        AnsweredQuestionUser answeredQuestionUser = new AnsweredQuestionUser(question, currentUser, answeredStatus);
        answeredQuestionUserRepository.save(answeredQuestionUser);

        if (isCorrect) {
            question.incrementCorrectAnswerCount();
            currentUser.incrementScore();
            userRepository.save(currentUser);
        }
        question.incrementAnswerCount();

        return new AnswerQuestionResponseDTO(isCorrect, isCorrect ? "Correct answer!" : "Incorrect answer.");
    }

    public List<QuestionDTO> getSimilarQuestions(Long id) {
        Question question = questionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Question not found"));
        return questionRepository.findByCreator_Id(question.getCreator().getId())
                .stream()
                .map(q -> getQuestionDTO(q, null))
                .toList();
    }

    public List<QuestionDTO> getFollowedUsersQuestions(CurrentUser user) {
        List<User> followedUsers = userRepository.findFollowedUsers(user.getUser().getId());
        List<Question> questions = followedUsers.stream()
                .flatMap(followedUser -> questionRepository.findByCreator_Id(followedUser.getId()).stream())
                .toList();
        return questions.stream()
                .map(q -> getQuestionDTO(q, null)) // Assuming 'answered' is not relevant here
                .toList();
    }

    private boolean filterByAnsweredStatus(Question question, Long userId, AnsweredStatus answeredStatus) {
        return answeredQuestionUserRepository.findByQuestionIdAndUserId(question.getId(), userId)
                .map(a -> a.getAnsweredStatus() == answeredStatus)
                .orElse(false);
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
                question.getLevel().getId(),
                question.getAnswerCount(),
                question.getCorrectAnswerCount(),
                question.getTag().getName(),
                answered
        );
    }
}