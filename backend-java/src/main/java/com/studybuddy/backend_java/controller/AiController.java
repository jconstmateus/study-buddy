package com.studybuddy.backend_java.controller;

import com.studybuddy.backend_java.dto.ChatMessageResponse;
import com.studybuddy.backend_java.dto.SummarizeSubjectRequest;
import com.studybuddy.backend_java.exceptions.NotAuthorizedException;
import com.studybuddy.backend_java.model.*;
import com.studybuddy.backend_java.service.*;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/ai")
public class AiController {

    private final AiService aiService;
    private final UserService userService;
    private final EventService eventService;
    private final StudyGoalService studyGoalService;
    private final ChatMessageService chatMessageService;

    public AiController(AiService aiService, UserService userService, EventService eventService,
                        StudyGoalService studyGoalService, ChatMessageService chatMessageService) {
        this.aiService = aiService;
        this.userService = userService;
        this.eventService = eventService;
        this.studyGoalService = studyGoalService;
        this.chatMessageService = chatMessageService;
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
    @PostMapping(value = "/new-message/{eventId}")
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



}
