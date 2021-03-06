package com.example.quartzdemo.config;

import com.example.quartzdemo.job.AJob;
import org.quartz.CronScheduleBuilder;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ConfigureJob {

    @Bean
    public JobDetail jobADetails() {
        return JobBuilder.newJob(AJob.class).withIdentity("sampleJobA")
                .storeDurably().build();
    }

    @Bean
    public Trigger jobATrigger(JobDetail jobADetails) {

        return TriggerBuilder.newTrigger().forJob(jobADetails)

                .withIdentity("sampleTriggerA")
                // online cron expression generator
                // https://www.freeformatter.com/cron-expression-generator-quartz.html
                // fire event every 5 seconds
                .withSchedule(CronScheduleBuilder.cronSchedule("0/5 * * ? * * *"))
                .build();
    }
}
