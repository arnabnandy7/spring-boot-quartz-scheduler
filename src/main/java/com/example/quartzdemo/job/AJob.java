package com.example.quartzdemo.job;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.ZonedDateTime;

public class AJob implements Job {

    private static final Logger logger = LoggerFactory.getLogger( AJob.class );

    @Override
    public void execute(JobExecutionContext context) {
        logger.info( "Scheduler output: " + ZonedDateTime.now().toString() );
    }
}
