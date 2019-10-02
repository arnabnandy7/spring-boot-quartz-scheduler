package com.example.quartzdemo.controller;

import com.example.quartzdemo.job.RestJob;
import com.example.quartzdemo.payload.ScheduleRequest;
import com.example.quartzdemo.payload.ScheduleResponse;
import org.quartz.JobBuilder;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SimpleScheduleBuilder;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.UUID;

@RestController
public class RestJobSchedulerController {
    private static final Logger logger = LoggerFactory.getLogger(RestJobSchedulerController.class);

    private final Scheduler scheduler;

    public RestJobSchedulerController(Scheduler scheduler) {
        this.scheduler = scheduler;
    }

    @PostMapping("/scheduleJob")
    public ResponseEntity<ScheduleResponse> scheduleEmail(@Valid @RequestBody ScheduleRequest scheduleRequest) {
        try {
            ZonedDateTime dateTime = ZonedDateTime.of(scheduleRequest.getDateTime(), scheduleRequest.getTimeZone());
            if(dateTime.isBefore(ZonedDateTime.now())) {
                ScheduleResponse scheduleResponse = new ScheduleResponse(false,
                        "dateTime must be after current time");
                return ResponseEntity.badRequest().body(scheduleResponse);
            }

            JobDetail jobDetail = buildJobDetail(scheduleRequest);
            Trigger trigger = buildJobTrigger(jobDetail, dateTime);
            scheduler.scheduleJob(jobDetail, trigger);

            ScheduleResponse scheduleResponse = new ScheduleResponse(true,
                    jobDetail.getKey().getName(), jobDetail.getKey().getGroup(), "Scheduled Successfully!");
            return ResponseEntity.ok(scheduleResponse);
        } catch (SchedulerException ex) {
            logger.error("Error scheduling email", ex);

            ScheduleResponse scheduleResponse = new ScheduleResponse(false,
                    "Error scheduling email. Please try later!");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(scheduleResponse);
        }
    }

    private JobDetail buildJobDetail(ScheduleRequest scheduleRequest) {

        JobDataMap jobDataMap = new JobDataMap();

        return JobBuilder.newJob(RestJob.class)
                .withIdentity(UUID.randomUUID().toString(), "rest-jobs")
                .withDescription("Send Rest Job")
                .usingJobData(jobDataMap)
                .storeDurably()
                .build();
    }

    private Trigger buildJobTrigger(JobDetail jobDetail, ZonedDateTime startAt) {
        return TriggerBuilder.newTrigger()
                .forJob(jobDetail)
                .withIdentity(jobDetail.getKey().getName(), "email-triggers")
                .withDescription("Send Rest Trigger")
                .startAt(Date.from(startAt.toInstant()))
                .withSchedule(SimpleScheduleBuilder.simpleSchedule().withMisfireHandlingInstructionFireNow())
                .build();
    }
}
