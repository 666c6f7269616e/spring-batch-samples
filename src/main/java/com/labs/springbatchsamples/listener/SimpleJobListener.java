package com.labs.springbatchsamples.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.stereotype.Component;

@Component
public class SimpleJobListener implements JobExecutionListener {

    private final Logger LOG = LoggerFactory.getLogger(this.getClass().getName());
    private String jobName;

    @Override
    public void beforeJob(JobExecution jobExecution) {

        jobName = jobExecution.getJobInstance().getJobName();

        LOG.info("\n==============================\n" +
                "\tLAUNCHING " + jobName +
                "\n==============================\n");
    }

    @Override
    public void afterJob(JobExecution jobExecution) {
        LOG.info("\n\n==============================\n" +
                "\tEND OF " + jobName +
                "\n==============================\n");
    }
}
