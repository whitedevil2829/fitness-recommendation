package com.fitness.activityservice.Services;

import com.fitness.activityservice.DTO.ActivityRequest;
import com.fitness.activityservice.DTO.ActivityResponse;
import com.fitness.activityservice.Repository.ActivityRepository;
import com.fitness.activityservice.model.Activity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
@Slf4j
public class ActivityService {

    private final ActivityRepository activityRepository;
    private final userValidationService validationService;
    private final RabbitTemplate rabbitTemplate;

    @Value("${rabbitmq.exchange.name}")
    private String exchange;
    @Value("${rabbitmq.routing.key}")
    private String routingKey;


    public ActivityResponse trackActivity(ActivityRequest request) {

       Boolean isUserValid=validationService.validateUser(request.getUserId());
       if(!isUserValid){
           throw new RuntimeException("Invalid User: "+request.getUserId());
       }
        Activity activity=Activity.builder()
                .userId(request.getUserId())
                .type(request.getType())
                .duration(request.getDuration())
                .startTime(request.getStartTime())
                .additionalMetrics(request.getAdditionalMetrics())
                .CalorieBurned(request.getCalorieBurned())
                .build();

            Activity savedActivity=activityRepository.save(activity);

            //Publish to rabbitMQ for AI response
            try{
                log.info("Publishing activity to RabbitMQ: {}", savedActivity);
                rabbitTemplate.convertAndSend(exchange,routingKey,savedActivity);
            }catch(Exception e){
                log.error("Failed to publish activity ",e);
            }
            return mapToResponse(savedActivity);
    }

    private ActivityResponse mapToResponse(Activity savedActivity){
        ActivityResponse response= new ActivityResponse();
        response.setId(savedActivity.getId());
        response.setUserId(savedActivity.getUserId());
        response.setType(savedActivity.getType());
        response.setDuration(savedActivity.getDuration());
        response.setCalorieBurned(savedActivity.getCalorieBurned());
        response.setStartTime(savedActivity.getStartTime());
        response.setCreatedAt(savedActivity.getCreatedAt());
        response.setUpdatedAt(savedActivity.getUpdatedAt());
        response.setAdditionalMetrics(savedActivity.getAdditionalMetrics());
        return response;
    }

    public List<ActivityResponse> getUserActivities(String userId) {
        List<Activity> activities= activityRepository.findByUserId(userId);

        //map to activityResponse
        return activities.stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public ActivityResponse getActivityById(String activityId) {

        return activityRepository.findById(activityId)
                .map(this::mapToResponse)
                .orElseThrow(()-> new RuntimeException("Activity not found with "+activityId));
    }
}
