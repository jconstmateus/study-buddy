package com.studybuddy.backend_java.controller;

import com.studybuddy.backend_java.dto.SummarizeSubjectRequest;
import com.studybuddy.backend_java.exceptions.NotAuthorizedException;
import com.studybuddy.backend_java.model.Event;
import com.studybuddy.backend_java.model.StudyGoal;
import com.studybuddy.backend_java.model.User;
import com.studybuddy.backend_java.service.AiService;
import com.studybuddy.backend_java.service.EventService;
import com.studybuddy.backend_java.service.StudyGoalService;
import com.studybuddy.backend_java.service.UserService;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/ai")
public class AiController {

    private final AiService aiService;
    private final UserService userService;
    private final EventService eventService;
    private final StudyGoalService studyGoalService;

    public AiController(AiService aiService, UserService userService, EventService eventService,
                        StudyGoalService studyGoalService) {
        this.aiService = aiService;
        this.userService = userService;
        this.eventService = eventService;
        this.studyGoalService = studyGoalService;
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
}
