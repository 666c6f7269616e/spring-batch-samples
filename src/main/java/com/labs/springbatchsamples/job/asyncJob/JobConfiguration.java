package com.labs.springbatchsamples.job.asyncJob;

import com.labs.springbatchsamples.job.JobEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDateTime;

@Configuration("asyncJobConfiguration")
public class JobConfiguration {

    private final Logger LOG = LoggerFactory.getLogger(this.getClass());
    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;

    public JobConfiguration(JobBuilderFactory jobBuilderFactory, StepBuilderFactory stepBuilderFactory) {
        this.jobBuilderFactory = jobBuilderFactory;
        this.stepBuilderFactory = stepBuilderFactory;
    }

    @Bean
    public Job asyncJob(    JobExecutionListener jobListener) {
        return jobBuilderFactory.get(JobEnum.ASYNC_JOB.getJobName())
                .start(asyncStep())
                .listener(jobListener)
                .build();
    }

    private Step asyncStep() {
        return stepBuilderFactory.get("ASYNC_STEP")
                .tasklet((contribution, chunkContext) -> {
                    LOG.info("Start waiting " + LocalDateTime.now().toString());
                    Thread.sleep(10000);
                    LOG.info("Stop waiting " + LocalDateTime.now().toString());
                    return RepeatStatus.FINISHED;
                }).build();
    }
}