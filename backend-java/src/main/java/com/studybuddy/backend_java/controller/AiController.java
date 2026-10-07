package com.studybuddy.backend_java.controller;

import com.studybuddy.backend_java.dto.*;
import com.studybuddy.backend_java.exceptions.NotAuthorizedException;
import com.studybuddy.backend_java.model.*;
import com.studybuddy.backend_java.service.*;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/ai")
public class AiController {

    private final AiService aiService;
    private final UserService userService;
    private final EventService eventService;
    private final StudyGoalService studyGoalService;
    private final ChatMessageService chatMessageService;
    private final TestService testService;
    private final QuestionService questionService;


    public AiController(AiService aiService, UserService userService, EventService eventService,
                        StudyGoalService studyGoalService, ChatMessageService chatMessageService,
                        TestService testService, QuestionService questionService) {
        this.aiService = aiService;
        this.userService = userService;
        this.eventService = eventService;
        this.studyGoalService = studyGoalService;
        this.chatMessageService = chatMessageService;
        this.testService = testService;
        this.questionService = questionService;
    }

    // POST (create summary with potential dto, and extracted title from Event
    @PostMapping(value = "/summarize/{eventId}", produces = MediaType.TEXT_PLAIN_VALUE)
    public String summarize(@PathVariable Long eventId,
                            @RequestBody(required = false) SummarizeSubjectRequest summarizeSubject,
                            Authentication authentication) {

        User user = userService.getCurrentUser(authentication);
        Event event = eventService.findById(eventId);

        // Verifiy authorization
        if (user.getId().equals(event.getCourse().getUser().getId())) {

            StudyGoal studyGoal = studyGoalService.findByEvent(event);
            String context = (summarizeSubject != null) ? summarizeSubject.getContext() : "";
            boolean hasContext = context != null && !context.isBlank();

            // Already have a summary and no additional context: return it without calling the AI
            if (studyGoal.getSummary() != null && !hasContext) {
                return studyGoal.getSummary();
            }

            // Otherwise (re)generate: pass the previous summary so the AI can refine it with the new context
            String previousSummary = (studyGoal.getSummary() != null) ? studyGoal.getSummary() : "";
            String newSummary = aiService.summarize(event.getTitle(), hasContext ? context : "", previousSummary);

            studyGoal.setSummary(newSummary);
            studyGoalService.save(studyGoal);
            return newSummary;

        } else {
            throw new NotAuthorizedException("Not Authorized to Summarize This Event");
        }
    }

    // GET (get the entirety chat message)
    @GetMapping("/chat/{eventId}")
    public List<ChatMessageResponse> getChat(@PathVariable Long eventId, Authentication authentication) {
        User user = userService.getCurrentUser(authentication);
        Event event = eventService.findById(eventId);

        // Verifiy authorization
        if (user.getId().equals(event.getCourse().getUser().getId())) {

            StudyGoal studyGoal = studyGoalService.findByEvent(event);

            return chatMessageService.findByStudyGoal(studyGoal)
                    .stream()
                    .map(ChatMessageResponse::from)
                    .toList();

        } else {
            throw new NotAuthorizedException("Not Authorized to View This Chat");
        }
    }

    // POST (post the new message to get response form AI)
    @PostMapping("/new-message/{eventId}")
    public ChatMessageResponse createResponse (@PathVariable Long eventId, @RequestBody String newMessage,
                                               Authentication authentication) {
        User user = userService.getCurrentUser(authentication);
        Event event = eventService.findById(eventId);

        // Verifiy authorization
        if (user.getId().equals(event.getCourse().getUser().getId())) {

            StudyGoal studyGoal = studyGoalService.findByEvent(event);

            // Save the message sent from user
            ChatMessage requestChatMessage = new ChatMessage();
            requestChatMessage.setStudyGoal(studyGoal);
            requestChatMessage.setAuthor(MessageAuthor.USER);
            requestChatMessage.setText(newMessage);
            chatMessageService.save(requestChatMessage);

            // Get summary and updated chat with new request
            String summary = studyGoal.getSummary();
            List<ChatMessage> chat = chatMessageService.findByStudyGoal(studyGoal);
            String aiText = aiService.createResponse(summary, chat); // Send request

            // Save the response
            ChatMessage responseChatMessage = new ChatMessage();
            responseChatMessage.setStudyGoal(studyGoal);
            responseChatMessage.setAuthor(MessageAuthor.AI);
            responseChatMessage.setText(aiText);
            ChatMessage saved = chatMessageService.save(responseChatMessage);

            return ChatMessageResponse.from(saved);

        } else {
            throw new NotAuthorizedException("Not Authorized to Add new Message");
        }
    }

    // POST (get the list of questions for the quizz)
    @PostMapping("/quizz/{eventId}")
    @Transactional // If the AI call or saveAll fails, the new Test is rolled back too (no empty test left behind)
    public QuizzResponse quizz(@PathVariable Long eventId, Authentication authentication) {
        User user = userService.getCurrentUser(authentication);
        Event event = eventService.findById(eventId);

        // Verifiy authorization
        if (user.getId().equals(event.getCourse().getUser().getId())) {
            // If two requests arrive at the same time, the second on waits here (prevent race condition)
            StudyGoal studyGoal = studyGoalService.findByEventForUpdate(event);
            Test test = testService.findLatestByStudyGoal(studyGoal);

            if (test == null) { // If test does not already exists
                return createNewTestAndQuestions(studyGoal);

            } else if (Boolean.TRUE.equals(test.getPassed())) {  // Test already exists, verify if passed. If true:
                QuizzResponse quizzResponse = new QuizzResponse();
                quizzResponse.setStatus(QuizzStatus.PASSED); // Return only the status as PASSED
                return quizzResponse;

            } else { // Exists, but passed is not true

                List<QuestionResponse> questions = questionService.findByTest(test)
                        .stream()
                        .map(QuestionResponse::new)
                        .toList();

                QuizzResponse quizzResponse = new QuizzResponse();
                quizzResponse.setStatus(QuizzStatus.PENDING);
                quizzResponse.setQuestions(questions);
                return quizzResponse;
            }

        } else {
            throw new NotAuthorizedException("Not Authorized to View Quizz");
        }
    }

    // POST (force-create a brand new quiz, even if one was already passed)
    @PostMapping("/quizz/{eventId}/new")
    @Transactional
    public QuizzResponse newQuizz(@PathVariable Long eventId, Authentication authentication) {
        User user = userService.getCurrentUser(authentication);
        Event event = eventService.findById(eventId);

        // Verifiy authorization
        if (!user.getId().equals(event.getCourse().getUser().getId())) {
            throw new NotAuthorizedException("Not Authorized to View Quizz");
        }

        StudyGoal studyGoal = studyGoalService.findByEventForUpdate(event);
        return createNewTestAndQuestions(studyGoal);
    }

    // Creates a new Test for the study goal, generates its questions with the AI and saves everything
    private QuizzResponse createNewTestAndQuestions(StudyGoal studyGoal) {
        Test newTest = new Test();
        newTest.setStudyGoal(studyGoal);
        newTest.setPassed(false);
        newTest = testService.save(newTest); // Create and save new test

        // Create the new question with API call (IA)
        List<Question> newQuestions = aiService.generateQuestionsForQuizz(newTest);

        // Save new questions into the new test
        questionService.saveAll(newQuestions);

        // Convert into dto QuestionResponse
        List<QuestionResponse> questions = newQuestions.stream()
                .map(QuestionResponse::new)
                .toList();

        // Create dto QuizzResponse with newquestions turned into QuestionResponse
        QuizzResponse quizzResponse = new QuizzResponse();
        quizzResponse.setStatus(QuizzStatus.PENDING);
        quizzResponse.setQuestions(questions);
        return quizzResponse;
    }

    // POST (submit the chosen answers, grade them server-side, save and return the score)
    @PostMapping("/quizz/{eventId}/submit")
    @Transactional
    public QuizzResultResponse submitQuizz(@PathVariable Long eventId,
                                           @RequestBody Map<Long, String> answers,
                                           Authentication authentication) {
        User user = userService.getCurrentUser(authentication);
        Event event = eventService.findById(eventId);

        // Verify authorization
        if (!user.getId().equals(event.getCourse().getUser().getId())) {
            throw new NotAuthorizedException("Not Authorized to Submit This Quizz");
        }

        StudyGoal studyGoal = studyGoalService.findByEvent(event);
        Test test = testService.findLatestByStudyGoal(studyGoal);
        if (test == null) {
            throw new IllegalStateException("There is no quiz to submit for this study goal.");
        }

        List<Question> questions = questionService.findByTest(test);

        int correct = 0;
        List<QuestionResultResponse> results = new ArrayList<>();

        for (Question question : questions) {
            String chosen = answers.get(question.getId());                 // Null if left unanswered
            boolean isCorrect = question.getCorrectAnswer().equals(chosen);
            if (isCorrect) {
                correct++;
            }
            results.add(new QuestionResultResponse(question.getId(), isCorrect));
        }

        int score = questions.isEmpty() ? 0 : correct * 100 / questions.size();
        boolean passed = score >= 80; // CHANGE HERE PASSING GRADE IF NECESSARY

        // Store the outcome on the Test
        test.setScoreObtained(score);
        test.setPassed(passed);
        test.setTakenAt(LocalDateTime.now());
        testService.save(test);

        QuizzResultResponse response = new QuizzResultResponse();
        response.setScore(score);
        response.setPassed(passed);
        response.setResults(results);
        return response;
    }

}
