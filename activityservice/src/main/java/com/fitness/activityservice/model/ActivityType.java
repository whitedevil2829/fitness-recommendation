package com.fitness.activityservice.model;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

public enum ActivityType {
    RUNNING,
    WALKING,
    CYCLING,
    SWIMMING,
    YOGA,
    WEIGHT_TRAINING,
    CARDIO,
    STRECHING,
    OTHERS
}
