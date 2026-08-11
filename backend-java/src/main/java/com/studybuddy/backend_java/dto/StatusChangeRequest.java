package com.studybuddy.backend_java.dto;

import com.studybuddy.backend_java.model.EventStatus;

// Small class to configure a padronized response when changing status
public class StatusChangeRequest {

    private EventStatus newEventStatus;

    // SETTERS
    public void setNewEventStatus(EventStatus newEventStatus) {this.newEventStatus = newEventStatus;}

    // GETTERS
    public EventStatus getNewEventStatus() { return newEventStatus; }

}

